package diarynote.settingsfragment.presentation.view.account

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import diarynote.core.common.dialog.data.DialogerImpl
import diarynote.core.utils.CONFIRM_PASSWORD_BIT_NUMBER
import diarynote.core.utils.CURRENT_PASSWORD_BIT_NUMBER
import diarynote.core.utils.PASSWORD_BIT_NUMBER
import diarynote.core.utils.PASSWORD_MIN_LENGTH
import diarynote.core.utils.ROOM_BIT_NUMBER
import diarynote.core.utils.listener.OnDialogPositiveButtonClickListener
import diarynote.data.model.UserModel
import diarynote.navigator.Navigator
import diarynote.settingsfragment.R
import diarynote.settingsfragment.databinding.FragmentChangePasswordBinding
import diarynote.settingsfragment.presentation.viewmodel.SettingsViewModel
import diarynote.template.model.UserState
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChangePasswordFragment : Fragment() {

    private var _binding: FragmentChangePasswordBinding? = null
    private val binding get() = _binding!!
    private val settingsViewModel: SettingsViewModel by viewModel()
    private lateinit var userModel: UserModel
    private var isChangePassword = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentChangePasswordBinding.bind(inflater.inflate(R.layout.fragment_change_password, container, false))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeData()
        settingsViewModel.getCurrentUserInfo()
    }

    private fun initViews() = with(binding) {

        initButton()

        currentPasswordEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) currentPasswordInputLayout.error = null
        }

        passwordInputEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) passwordTextInputLayout.error = null
        }

        confirmPasswordInputEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) confirmPasswordTextInputLayout.error = null
        }
    }

    private fun initButton() = with(binding) {
        changePasswordButton.setOnClickListener {
            settingsViewModel.changeUserPassword(
                currentPasswordEditText.text.toString(),
                passwordInputEditText.text.toString(),
                confirmPasswordInputEditText.text.toString(),
                userModel
            )
        }
    }

    private fun observeData() {
        val observer = Observer<UserState?>{ renderData(it) }
        settingsViewModel.userLiveData.observe(viewLifecycleOwner, observer)
    }

    private fun renderData(userState: UserState) {
        when(userState) {
            is UserState.Error -> handleError(userState.errorCode, userState.message)
            is UserState.Loading -> setProgressBarVisible(true)
            is UserState.Success -> handleSuccess(userState.userModel)
        }
    }

    private fun handleError(code: Int, message: String) = with(binding){
        setProgressBarVisible(false)

        if((1 shl CURRENT_PASSWORD_BIT_NUMBER) and code != 0) currentPasswordInputLayout.error = getString(
                diarynote.core.R.string.invalid_current_password_text)
        if((1 shl PASSWORD_BIT_NUMBER) and code != 0) passwordTextInputLayout.error = getString(
            diarynote.core.R.string.password_input_error_message, PASSWORD_MIN_LENGTH.toString())
        if((1 shl CONFIRM_PASSWORD_BIT_NUMBER) and code != 0) confirmPasswordTextInputLayout.error = getString(
            diarynote.core.R.string.password_not_confirmed_error_message)
        if((1 shl ROOM_BIT_NUMBER) and code != 0) setErrorMessage(message)
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

    private fun handleSuccess(userModel: UserModel) {
        setProgressBarVisible(false)
        if (isChangePassword) {
            successfullPasswordChanging()
        } else {
            this.userModel = userModel
            isChangePassword = true
            initViews()
        }
    }

    private fun successfullPasswordChanging() {
        val dialoger = DialogerImpl(requireActivity(),
            object : OnDialogPositiveButtonClickListener{
                override fun onClick() {
                    clearInputForms()
                    requireActivity().onBackPressed()
                }
            })

        setProgressBarVisible(false)
        dialoger.showAlertDialog(
            getString(diarynote.core.R.string.change_password_successfull_dialog_title),
            getString(diarynote.core.R.string.change_password_successfull_dialog_message),getString(diarynote.core.R.string.dialog_button_ok_text)
            )
    }

    private fun setErrorMessage(message: String) = with(binding) {
        mainGroup.visibility = View.GONE
        progressBar.visibility = View.GONE
        errorMessageTextView.visibility = View.VISIBLE
        errorMessageTextView.text = getString(diarynote.core.R.string.change_password_error_message_text, message)
    }

    private fun clearInputForms() = with(binding) {
        currentPasswordEditText.setText("")
        passwordInputEditText.setText("")
        confirmPasswordInputEditText.setText("")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}