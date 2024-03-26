package diarynote.settingsfragment.presentation.view.account

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.Observer
import diarynote.core.common.dialog.data.DialogerImpl
import diarynote.core.utils.BACKUP_BIT_NUMBER
import diarynote.core.utils.FileHelper
import diarynote.core.utils.INVALID_FILE_EXTENSION_BIT_NUMBER
import diarynote.core.utils.RESTORE_BIT_NUMBER
import diarynote.core.utils.listener.OnDialogPositiveButtonClickListener
import diarynote.data.model.state.BackupState
import diarynote.navigator.Navigator
import diarynote.settingsfragment.R
import diarynote.settingsfragment.databinding.FragmentBackupRestoreBinding
import diarynote.settingsfragment.presentation.view.dialog.PasswordDialogFragment
import diarynote.settingsfragment.presentation.view.dialog.RestorePasswordDialogFragment
import diarynote.settingsfragment.presentation.viewmodel.SettingsViewModel
import diarynote.template.utils.OnSetPasswordButtonClickListener
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File


private const val DEFAULT_EXPORT_TITLE = "BackupDatabase.db"
private const val DIALOG_FRAGMENT = "Dialog fragment"
const val BACKUP_PASSWORD_KEY = "backup_password"
const val BACKUP_PASSWORD_STRING = "backup_password_string"
const val RESTORE_PASSWORD_KEY = "restore_password"
const val RESTORE_PASSWORD_STRING = "restore_password_string"

class BackupRestoreFragment() : Fragment() {

    private var _binding: FragmentBackupRestoreBinding? = null
    private val binding get() = _binding!!
    private val settingsViewModel: SettingsViewModel by viewModel()
    private val navigator: Navigator by inject()
    private var backupPassword = ""
    private lateinit var uri: Uri
    private val createFile = registerForActivityResult(ActivityResultContracts.CreateDocument()) {
        if (it != null) {
            settingsViewModel.exportDB(it, backupPassword)
        }
    }

    private val openFile = registerForActivityResult(ActivityResultContracts.OpenDocument()) {
        if (it != null) {
            //check file for right extension and check if it encrypted or not
            uri = it
            settingsViewModel.checkPickedFile(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        childFragmentManager.setFragmentResultListener(BACKUP_PASSWORD_KEY, this) { _, bundle ->
            backupPassword = bundle.getString(BACKUP_PASSWORD_STRING) ?: ""
            createFile.launch(DEFAULT_EXPORT_TITLE)
        }

        childFragmentManager.setFragmentResultListener(RESTORE_PASSWORD_KEY, this) { _, bundle ->
            backupPassword = bundle.getString(RESTORE_PASSWORD_STRING) ?: ""
            settingsViewModel.importEncryptedDB(uri, backupPassword)
        }

        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                exitBackupRestoreFragment()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            exitBackupRestoreFragment()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true)
        _binding = FragmentBackupRestoreBinding.bind(inflater.inflate(R.layout.fragment_backup_restore, container, false))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observeData()
    }

    private fun initViews() = with(binding) {
        createBackupButton.setOnClickListener {
            if (defaultThemePickSwitch.isChecked) {
                //launch dialog
                showPasswordDialog()
            } else createFile.launch(DEFAULT_EXPORT_TITLE)
        }
        restoreButton.setOnClickListener {
            openFile.launch(arrayOf("application/octet-stream"))
        }
    }

    private fun observeData() {
        val observer = Observer<BackupState?> { renderData(it) }
        settingsViewModel.backupLiveData.observe(viewLifecycleOwner, observer)
    }

    private fun renderData(backupState: BackupState?) {
        when(backupState) {
            is BackupState.Error -> handleError(backupState.message, backupState.errorCode)
            BackupState.Idle -> setProgressBarVisible(false)
            BackupState.Loading -> setProgressBarVisible(true)
            BackupState.SuccessBackup -> handleBackupSuccess()
            BackupState.SuccessRestore -> handleRestoreSuccess()
            is BackupState.DbState -> importDB(backupState.isEncrypted, backupState.uri)
            else -> {}
        }
    }

    private fun importDB(isEncrypted: Boolean, uri: Uri) {
        settingsViewModel.setBackupIdle()
        if (isEncrypted) {
            importEncryptedDB(uri)
        } else {
            settingsViewModel.importDB(uri)
        }
    }

    private fun importEncryptedDB(uri: Uri) {
        val restorePasswordDialog = RestorePasswordDialogFragment()
        restorePasswordDialog.show(childFragmentManager, DIALOG_FRAGMENT)
    }

    private fun handleError(message: String, errorCode: Int) {
        val dialoger = DialogerImpl(requireActivity())
        setProgressBarVisible(false)

        val dialogMessage: String = when(errorCode) {
            (1 shl BACKUP_BIT_NUMBER) and errorCode -> getString(diarynote.core.R.string.db_save_error_dialog_message)
            (1 shl RESTORE_BIT_NUMBER) and errorCode -> getString(diarynote.core.R.string.db_restore_error_dialog_message)
            (1 shl INVALID_FILE_EXTENSION_BIT_NUMBER) and errorCode -> getString(diarynote.core.R.string.db_wrong_file_extension_error_message_text)
            else -> message
        }

        dialoger.showAlertDialog(getString(diarynote.core.R.string.error_dialog_title_text),
            dialogMessage,
            getString(diarynote.core.R.string.dialog_button_ok_text))
    }

    private fun setProgressBarVisible(visible: Boolean) = with(binding) {
        if (visible) {
            progressBar.visibility = View.VISIBLE
            mainGroup.visibility = View.GONE
        } else {
            progressBar.visibility = View.GONE
            mainGroup.visibility = View.VISIBLE
        }
    }

    private fun handleBackupSuccess() {
        val dialoger = DialogerImpl(requireActivity())

        setProgressBarVisible(false)

        dialoger.showAlertDialog(
            getString(diarynote.core.R.string.db_save_success_dialog_title),
            getString(diarynote.core.R.string.db_save_success_dialog_message),
            getString(diarynote.core.R.string.dialog_button_ok_text))
        backupPassword = ""
        settingsViewModel.setBackupIdle()
    }

    private fun handleRestoreSuccess() {
        val dialoger = DialogerImpl(requireActivity(),
            object : OnDialogPositiveButtonClickListener {
                override fun onClick() {
                    settingsViewModel.setDefaultUserId()
                    requireActivity().finish()
                    System.exit(0)
                }
            })

        dialoger.showAlertDialog(
            getString(diarynote.core.R.string.db_restore_success_dialog_title),
            getString(diarynote.core.R.string.db_restore_success_dialog_message),getString(diarynote.core.R.string.dialog_button_ok_text)
        )
    }

    private fun showPasswordDialog() {
        val passwordDialog = PasswordDialogFragment()
        passwordDialog.show(childFragmentManager, DIALOG_FRAGMENT)
    }

    private fun exitBackupRestoreFragment() {
        if (settingsViewModel.backupLiveData.value == BackupState.Loading) {
            val dialoger =
                DialogerImpl(requireActivity(), object : OnDialogPositiveButtonClickListener {
                    override fun onClick() {
                        navigator.navigateUp()
                    }
                })

            dialoger.showTwoButtonDialog(getString(diarynote.core.R.string.exit_note_creation_dialog_title_text), getString(
                diarynote.core.R.string.exit_backup_restore_dialog_message_text), getString(
                diarynote.core.R.string.dialog_button_yes_text), getString(
                diarynote.core.R.string.dialog_button_no_text))
            settingsViewModel.setBackupIdle()
            settingsViewModel.clearDisposable()
        } else {
            navigator.navigateUp()
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}