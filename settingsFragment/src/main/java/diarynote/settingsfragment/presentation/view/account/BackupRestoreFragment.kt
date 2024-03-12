package diarynote.settingsfragment.presentation.view.account

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import diarynote.core.common.dialog.data.DialogerImpl
import diarynote.core.utils.BACKUP_BIT_NUMBER
import diarynote.core.utils.RESTORE_BIT_NUMBER
import diarynote.core.utils.listener.OnDialogPositiveButtonClickListener
import diarynote.navigator.Navigator
import diarynote.settingsfragment.R
import diarynote.settingsfragment.databinding.FragmentBackupRestoreBinding
import diarynote.settingsfragment.presentation.viewmodel.SettingsViewModel
import diarynote.data.model.state.BackupState
import diarynote.settingsfragment.presentation.view.dialog.PasswordDialogFragment
import diarynote.template.utils.OnSetPasswordButtonClickListener
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

private const val DEFAULT_EXPORT_TITLE = "BackupDatabase.db"
private const val DIALOG_FRAGMENT = "Dialog fragment"

class BackupRestoreFragment() : Fragment() {

    private var _binding: FragmentBackupRestoreBinding? = null
    private val binding get() = _binding!!
    private val settingsViewModel: SettingsViewModel by viewModel()
    private val navigator: Navigator by inject()
    private var backupPassword = ""
    private val createFile = registerForActivityResult(ActivityResultContracts.CreateDocument()) {
        if (it != null) {
            settingsViewModel.exportDB(it, backupPassword)
            //check file extension
            //if it .db then check if it encrypted or not
            if(MimeTypeMap.getFileExtensionFromUrl(it.toString()) == "db") {

            } else {
                handleError("Неверное расширение файла", 0)
            }
        }
    }

    private val openFile = registerForActivityResult(ActivityResultContracts.OpenDocument()) {
        if (it != null) {
            settingsViewModel.importDB(it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
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
            BackupState.Idle -> {}
            BackupState.Loading -> setProgressBarVisible(true)
            BackupState.SuccessBackup -> handleBackupSuccess()
            BackupState.SuccessRestore -> handleRestoreSuccess()
            else -> {}
        }
    }

    private fun handleError(message: String, errorCode: Int) {
        val dialoger = DialogerImpl(requireActivity())
        setProgressBarVisible(false)

        val dialogMessage: String = when(errorCode) {
            (1 shl BACKUP_BIT_NUMBER) and errorCode -> getString(diarynote.core.R.string.db_save_error_dialog_message)
            (1 shl RESTORE_BIT_NUMBER) and errorCode -> getString(diarynote.core.R.string.db_restore_error_dialog_message)
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
        settingsViewModel.setBackupIdle()
    }

    private fun handleRestoreSuccess() {
        val dialoger = DialogerImpl(requireActivity(),
            object : OnDialogPositiveButtonClickListener {
                override fun onClick() {
                    navigator.navigateToViewModelCleaner()
                }
            })

        dialoger.showAlertDialog(
            getString(diarynote.core.R.string.db_restore_success_dialog_title),
            getString(diarynote.core.R.string.db_restore_success_dialog_message),getString(diarynote.core.R.string.dialog_button_ok_text)
        )
    }

    private fun showPasswordDialog() {
        val passwordDialog = PasswordDialogFragment(object : OnSetPasswordButtonClickListener{
            override fun onClick(password: String) {
                backupPassword = password
                createFile.launch(DEFAULT_EXPORT_TITLE)
            }
        })
        passwordDialog.show(childFragmentManager, DIALOG_FRAGMENT)
    }

}