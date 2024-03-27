package diarynote.settingsfragment.presentation.view.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import diarynote.core.utils.PASSWORD_BIT_NUMBER
import diarynote.core.utils.PASSWORD_MIN_LENGTH
import diarynote.settingsfragment.R
import diarynote.settingsfragment.databinding.FragmentRestorePasswordDialogBinding
import diarynote.settingsfragment.presentation.view.account.BACKUP_PASSWORD_KEY
import diarynote.settingsfragment.presentation.view.account.BACKUP_PASSWORD_STRING
import diarynote.settingsfragment.presentation.view.account.RESTORE_PASSWORD_KEY
import diarynote.settingsfragment.presentation.view.account.RESTORE_PASSWORD_STRING
import diarynote.settingsfragment.presentation.viewmodel.SettingsViewModel
import diarynote.template.utils.OnSetPasswordButtonClickListener
import org.koin.androidx.viewmodel.ext.android.viewModel

class RestorePasswordDialogFragment : DialogFragment() {

    private var _binding: FragmentRestorePasswordDialogBinding? = null
    private val binding get() = _binding!!
    private val settingsViewModel: SettingsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRestorePasswordDialogBinding.bind(inflater.inflate(R.layout.fragment_restore_password_dialog, null))
        binding.setPasswordTextView.setOnClickListener {
            setPassword(binding.passwordInputEditText.text.toString())
        }
        isCancelable = true
        return binding.root
    }

    private fun setPassword(enteredPassword: String) {
        val validationCode = settingsViewModel.recoveryPasswordValidationCode(enteredPassword)

        if((1 shl PASSWORD_BIT_NUMBER) and validationCode != 0) binding.passwordTextInputLayout.error = getString(
            diarynote.core.R.string.password_input_error_message, PASSWORD_MIN_LENGTH.toString())
        if(validationCode == 0) {
            setFragmentResult(RESTORE_PASSWORD_KEY, bundleOf(RESTORE_PASSWORD_STRING to enteredPassword))
            binding.passwordTextInputLayout.error = null
            dismiss()
        }
    }
}