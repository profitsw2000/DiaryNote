package diarynote.settingsfragment.presentation.view.account

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import diarynote.core.common.dialog.data.DialogerImpl
import diarynote.core.utils.EMAIL_ALREADY_EXIST_BIT_NUMBER
import diarynote.core.utils.EMAIL_BIT_NUMBER
import diarynote.core.utils.LOGIN_ALREADY_EXIST_BIT_NUMBER
import diarynote.core.utils.LOGIN_BIT_NUMBER
import diarynote.core.utils.LOGIN_MIN_LENGTH
import diarynote.core.utils.NAME_BIT_NUMBER
import diarynote.core.utils.NAME_MIN_LENGTH
import diarynote.core.utils.ROOM_BIT_NUMBER
import diarynote.core.utils.SURNAME_BIT_NUMBER
import diarynote.core.utils.listener.OnDialogPositiveButtonClickListener
import diarynote.data.model.UserModel
import diarynote.settingsfragment.R
import diarynote.settingsfragment.databinding.FragmentChangeUserInfoBinding
import diarynote.settingsfragment.presentation.viewmodel.SettingsViewModel
import diarynote.data.model.state.UserState
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChangeUserInfoFragment : Fragment() {

    private var _binding: FragmentChangeUserInfoBinding? = null
    private val binding get() = _binding!!
    private val settingsViewModel: SettingsViewModel by viewModel()
    private var isChangeUserInfo = false
    private lateinit var userModel: UserModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentChangeUserInfoBinding.bind(inflater.inflate(R.layout.fragment_change_user_info, container, false))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //initViews()
        observeData()
        settingsViewModel.getCurrentUserInfo()
    }

    private fun initViews() = with(binding) {
        nameInputEditText.setText(userModel.name)
        surnameInputEditText.setText(userModel.surname)
        loginInputEditText.setText(userModel.login)
        emailInputEditText.setText(userModel.email)

        changeUserInfoButton.setOnClickListener {
            settingsViewModel.changeUserInfo(nameInputEditText.text.toString(),
                                            surnameInputEditText.text.toString(),
                                            loginInputEditText.text.toString(),
                                            emailInputEditText.text.toString(),
                                            userModel
                )
        }
    }

    private fun observeData() {
        val observer = Observer<UserState?> { renderData(it) }
        settingsViewModel.userLiveData.observe(viewLifecycleOwner, observer)
    }

    private fun renderData(userState: UserState?) {
        when(userState) {
            is UserState.Error -> handleError(userState.errorCode, userState.message)
            is UserState.Loading -> setProgressBarVisible(true)
            is UserState.Success -> handleSuccess(userState.userModel)
            else -> {}
        }
    }

    private fun handleError(code: Int, message: String) = with(binding){
        val dialoger = DialogerImpl(requireActivity())
        setProgressBarVisible(false)

        if((1 shl NAME_BIT_NUMBER) and code != 0) nameTextInputLayout.error = getString(
            diarynote.core.R.string.name_input_error_message, NAME_MIN_LENGTH.toString())
        if((1 shl SURNAME_BIT_NUMBER) and code != 0) surnameTextInputLayout.error = getString(
            diarynote.core.R.string.name_input_error_message, NAME_MIN_LENGTH.toString())
        if((1 shl LOGIN_BIT_NUMBER) and code != 0) loginTextInputLayout.error = getString(
            diarynote.core.R.string.login_input_error_message, LOGIN_MIN_LENGTH.toString())
        if((1 shl EMAIL_BIT_NUMBER) and code != 0) emailTextInputLayout.error = getString(
            diarynote.core.R.string.invalid_email_input_message)
        if((1 shl ROOM_BIT_NUMBER) and code != 0) dialoger.showAlertDialog(getString(diarynote.core.R.string.error_dialog_title_text), getString(diarynote.core.R.string.user_registration_error_message), getString(diarynote.core.R.string.dialog_button_ok_text))
        if((1 shl LOGIN_ALREADY_EXIST_BIT_NUMBER) and code != 0) dialoger.showAlertDialog(getString(diarynote.core.R.string.error_dialog_title_text), getString(diarynote.core.R.string.user_already_exist_error_message), getString(diarynote.core.R.string.dialog_button_ok_text))
        if((1 shl EMAIL_ALREADY_EXIST_BIT_NUMBER) and code != 0) dialoger.showAlertDialog(getString(diarynote.core.R.string.error_dialog_title_text), getString(diarynote.core.R.string.email_already_exist_error_message), getString(diarynote.core.R.string.dialog_button_ok_text))
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
        if (isChangeUserInfo) {
            successfullUserInfoChanging()
        } else {
            this.userModel = userModel
            isChangeUserInfo = true
            initViews()
        }
    }

    private fun successfullUserInfoChanging() {

        val dialoger = DialogerImpl(requireActivity(),
            object : OnDialogPositiveButtonClickListener {
                override fun onClick() {
                    clearInputForms()
                    requireActivity().onBackPressed()
                }
            })

        setProgressBarVisible(false)
        dialoger.showAlertDialog(
            getString(diarynote.core.R.string.change_user_info_successfull_dialog_title),
            getString(diarynote.core.R.string.change_user_info_successfull_dialog_message),getString(diarynote.core.R.string.dialog_button_ok_text)
        )
    }

    private fun clearInputForms() = with(binding) {
        nameInputEditText.setText("")
        surnameInputEditText.setText("")
        loginInputEditText.setText("")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}