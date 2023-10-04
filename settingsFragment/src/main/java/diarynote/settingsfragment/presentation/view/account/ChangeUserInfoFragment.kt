package diarynote.settingsfragment.presentation.view.account

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import diarynote.data.model.UserModel
import diarynote.settingsfragment.R
import diarynote.settingsfragment.databinding.FragmentChangeUserInfoBinding
import diarynote.settingsfragment.presentation.viewmodel.SettingsViewModel
import diarynote.template.model.UserState
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

        changeUserInfoButton.setOnClickListener {
            settingsViewModel.changeUserInfo(nameInputEditText.text.toString(),
                                            surnameInputEditText.text.toString(),
                                            loginInputEditText.text.toString(),
                                            userModel
                )
        }
    }

    private fun observeData() {
        val observer = Observer<UserState?> { renderData(it) }
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

/*        if((1 shl CURRENT_PASSWORD_BIT_NUMBER) and code != 0) currentPasswordInputLayout.error = getString(
            diarynote.core.R.string.invalid_current_password_text)
        if((1 shl PASSWORD_BIT_NUMBER) and code != 0) passwordTextInputLayout.error = getString(
            diarynote.core.R.string.password_input_error_message, PASSWORD_MIN_LENGTH.toString())
        if((1 shl CONFIRM_PASSWORD_BIT_NUMBER) and code != 0) confirmPasswordTextInputLayout.error = getString(
            diarynote.core.R.string.password_not_confirmed_error_message)
        if((1 shl ROOM_BIT_NUMBER) and code != 0) setErrorMessage(message)*/

    }

    private fun setErrorMessage(message: String) = with(binding) {
        mainGroup.visibility = View.GONE
        progressBar.visibility = View.GONE
        errorMessageTextView.visibility = View.VISIBLE
        errorMessageTextView.text = getString(diarynote.core.R.string.change_user_info_error_message_text, message)
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