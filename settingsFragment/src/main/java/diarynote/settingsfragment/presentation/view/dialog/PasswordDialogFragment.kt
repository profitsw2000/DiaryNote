package diarynote.settingsfragment.presentation.view.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import diarynote.core.utils.CONFIRM_PASSWORD_BIT_NUMBER
import diarynote.core.utils.PASSWORD_BIT_NUMBER
import diarynote.core.utils.PASSWORD_MIN_LENGTH
import diarynote.settingsfragment.R
import diarynote.settingsfragment.databinding.FragmentPasswordDialogBinding
import diarynote.settingsfragment.presentation.view.account.BACKUP_PASSWORD_KEY
import diarynote.settingsfragment.presentation.view.account.BACKUP_PASSWORD_STRING
import diarynote.settingsfragment.presentation.viewmodel.SettingsViewModel
import diarynote.template.utils.OnSetPasswordButtonClickListener
import org.koin.androidx.viewmodel.ext.android.viewModel

class PasswordDialogFragment(
    //private val onSetPasswordButtonClickListener: OnSetPasswordButtonClickListener
) : DialogFragment() {

    private var _binding: FragmentPasswordDialogBinding? = null
    private val binding get() = _binding!!
    private val settingsViewModel: SettingsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPasswordDialogBinding.bind(inflater.inflate(R.layout.fragment_password_dialog, null))
        binding.setPasswordTextView.setOnClickListener {
            setPassword(
                binding.passwordInputEditText.text.toString(),
                binding.confirmPasswordInputEditText.text.toString()
            )
        }
        isCancelable = true
        //dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return binding.root
    }

    private fun setPassword(enteredPassword: String, confirmedPassword: String) {
        val validationCode = settingsViewModel.backupPasswordEncryptionValidationCode(enteredPassword, confirmedPassword)

        if((1 shl PASSWORD_BIT_NUMBER) and validationCode != 0) binding.passwordTextInputLayout.error = getString(
            diarynote.core.R.string.password_input_error_message, PASSWORD_MIN_LENGTH.toString())
        else binding.passwordTextInputLayout.error = null

        if((1 shl CONFIRM_PASSWORD_BIT_NUMBER) and validationCode != 0) binding.confirmPasswordTextInputLayout.error = getString(
            diarynote.core.R.string.password_not_confirmed_error_message)
        else binding.confirmPasswordTextInputLayout.error = null

        if(validationCode == 0) {
            //onSetPasswordButtonClickListener.onClick(enteredPassword)
            setFragmentResult(BACKUP_PASSWORD_KEY, bundleOf(BACKUP_PASSWORD_STRING to enteredPassword))
            binding.passwordTextInputLayout.error = null
            binding.confirmPasswordTextInputLayout.error = null
            dismiss()
        }
    }
}