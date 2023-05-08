package diarynote.registrationscreen.presentation.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import diarynote.core.common.Controller
import diarynote.core.common.view.Dialoger
import diarynote.core.utils.*
import diarynote.core.view.CoreFragment
import diarynote.data.model.UserModel
import diarynote.registrationScreen.R
import diarynote.registrationScreen.databinding.FragmentRegistrationBinding
import diarynote.registrationscreen.model.RegState
import diarynote.registrationscreen.presentation.viewmodel.RegistrationViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegistrationFragment : CoreFragment(R.layout.fragment_registration) {

    private var _binding: FragmentRegistrationBinding? = null
    private val binding get() = _binding!!
    private val registrationViewModel: RegistrationViewModel by viewModel()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (activity !is Controller) {
            throw IllegalStateException(getString(diarynote.core.R.string.not_controller_activity_exception))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegistrationBinding.bind(inflater.inflate(R.layout.fragment_registration, container, false))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observeData()
    }

    private fun observeData() {
        val observer = Observer<RegState> { renderData(it) }
        registrationViewModel.registrationLiveData.observe(viewLifecycleOwner, observer)
    }

    private fun initViews() = with(binding) {
        loginButton.setOnClickListener {
            val login = binding.loginInputEditText.text.toString()
            val email = binding.emailInputEditText.text.toString()
            val password = binding.passwordInputEditText.text.toString()
            val confirmPassword = binding.confirmPasswordInputEditText.text.toString()

            loginTextInputLayout.error = null
            emailTextInputLayout.error = null
            passwordTextInputLayout.error = null
            confirmPasswordTextInputLayout.error = null

            registrationViewModel.registerUser(login, email, password, confirmPassword)
        }

        passwordInputEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) passwordTextInputLayout.error = null
        }

        confirmPasswordInputEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) confirmPasswordTextInputLayout.error = null
        }

    }

    private fun renderData(regState: RegState?) {
        when(regState) {
            is RegState.Success -> showSuccessMessage(regState.userModel)
            is RegState.Loading -> showProgressBar()
            is RegState.Error -> handleError(regState.code)
            else -> {}
        }
    }

    private fun showSuccessMessage(userModel: UserModel) = with(binding) {
        val dialoger = Dialoger(requireActivity())

        progressBar.visibility = View.GONE
        dialoger.showAlertDialog(getString(diarynote.core.R.string.registration_successful_dialog_title_text),
            getString(diarynote.core.R.string.registration_successful_dialog_text,userModel.login))
        //записать категории по умолчанию в базу
        registrationViewModel.insertDefaultCategories(userModel)
        registrationViewModel.insertDefaultNotes(userModel)
    }

    private fun handleError(code: Int) = with(binding) {
        val dialoger = Dialoger(requireActivity())
        progressBar.visibility = View.GONE

        if((1 shl LOGIN_BIT_NUMBER) and code != 0) loginTextInputLayout.error = getString(
            diarynote.core.R.string.login_input_error_message, LOGIN_MIN_LENGTH.toString())
        if((1 shl EMAIL_BIT_NUMBER) and code != 0) emailTextInputLayout.error = getString(diarynote.core.R.string.invalid_email_input_message)
        if((1 shl PASSWORD_BIT_NUMBER) and code != 0) passwordTextInputLayout.error = getString(
            diarynote.core.R.string.password_input_error_message, PASSWORD_MIN_LENGTH.toString())
        if((1 shl CONFIRM_PASSWORD_BIT_NUMBER) and code != 0) confirmPasswordTextInputLayout.error = getString(
                    diarynote.core.R.string.password_not_confirmed_error_message)
        if((1 shl ROOM_BIT_NUMBER) and code != 0) dialoger.showAlertDialog(getString(diarynote.core.R.string.error_dialog_title_text), getString(diarynote.core.R.string.user_registration_error_message))
        if((1 shl LOGIN_ALREADY_EXIST_BIT_NUMBER) and code != 0) dialoger.showAlertDialog(getString(diarynote.core.R.string.error_dialog_title_text), getString(diarynote.core.R.string.user_already_exist_error_message))
        if((1 shl EMAIL_ALREADY_EXIST_BIT_NUMBER) and code != 0) dialoger.showAlertDialog(getString(diarynote.core.R.string.error_dialog_title_text), getString(diarynote.core.R.string.email_already_exist_error_message))
    }

    private fun showProgressBar() = with(binding) {
        progressBar.visibility = View.VISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        fun newInstance() = RegistrationFragment()
    }
}