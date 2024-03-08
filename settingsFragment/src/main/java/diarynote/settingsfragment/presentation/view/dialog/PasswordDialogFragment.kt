package diarynote.settingsfragment.presentation.view.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import diarynote.core.utils.CONFIRM_PASSWORD_BIT_NUMBER
import diarynote.core.utils.PASSWORD_BIT_NUMBER
import diarynote.core.utils.PASSWORD_MIN_LENGTH
import diarynote.settingsfragment.R
import diarynote.settingsfragment.databinding.FragmentPasswordDialogBinding
import diarynote.settingsfragment.presentation.viewmodel.SettingsViewModel
import diarynote.template.utils.OnSetPasswordButtonClickListener
import org.koin.androidx.viewmodel.ext.android.viewModel

class PasswordDialogFragment(
    private val onSetPasswordButtonClickListener: OnSetPasswordButtonClickListener
) : DialogFragment() {

    private var _binding: FragmentPasswordDialogBinding? = null
    private val binding get() = _binding!!
    private val settingsViewModel: SettingsViewModel by viewModel()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        _binding = FragmentPasswordDialogBinding.bind(requireActivity().layoutInflater.inflate(R.layout.fragment_password_dialog, null))

        val contentView = binding.root

        val builder = MaterialAlertDialogBuilder(requireContext())
            .setTitle("Ввод пароля")
            .setView(contentView)
            .setPositiveButton("Установить пароль") {dialog, _ ->
                    setPassword(
                        binding.passwordInputEditText.text.toString(),
                        binding.confirmPasswordInputEditText.text.toString(),
                        dialog
                    )
                }

        return builder.create()
    }

    private fun setPassword(enteredPassword: String, confirmedPassword: String, dialog: DialogInterface) {
        val validationCode = settingsViewModel.backupPasswordEncryptionValidationCode(enteredPassword, confirmedPassword)

        if((1 shl PASSWORD_BIT_NUMBER) and validationCode != 0) binding.passwordTextInputLayout.error = getString(
            diarynote.core.R.string.password_input_error_message, PASSWORD_MIN_LENGTH.toString())
        if((1 shl CONFIRM_PASSWORD_BIT_NUMBER) and validationCode != 0) binding.confirmPasswordTextInputLayout.error = getString(
            diarynote.core.R.string.password_not_confirmed_error_message)
        if(validationCode == 0) {
            dialog.dismiss()
            onSetPasswordButtonClickListener.onClick(enteredPassword)
        }
    }
}