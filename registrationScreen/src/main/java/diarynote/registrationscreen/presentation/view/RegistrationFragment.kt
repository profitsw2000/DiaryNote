package diarynote.registrationscreen.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
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
        registrationViewModel.registrationLiveData.observe(this, observer)
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
            is RegState.Loading -> showProgreessBar()
            is RegState.Error -> handleError(regState.code)
            else -> {}
        }
    }

    private fun showSuccessMessage(userModel: UserModel) = with(binding) {
        progressBar.visibility = View.GONE
        Toast.makeText(
            requireContext(),
            "Пользователь ${userModel.login} был успешно создан.",
            Toast.LENGTH_SHORT)
            .show()
    }

    private fun handleError(code: Int) = with(binding) {
        progressBar.visibility = View.GONE

        if((1 shl LOGIN_BIT_NUMBER) and code != 0) loginTextInputLayout.error = "Не менее 4 буквенных или цифровых символов"
        if((1 shl EMAIL_BIT_NUMBER) and code != 0) emailTextInputLayout.error = "Неверный email"
        if((1 shl PASSWORD_BIT_NUMBER) and code != 0) passwordTextInputLayout.error = "Не менее 7 буквенных или цифровых символов"
        if((1 shl CONFIRM_PASSWORD_BIT_NUMBER) and code != 0) confirmPasswordTextInputLayout.error = "Пароль не подтверждён"
        if((1 shl ROOM_BIT_NUMBER) and code != 0) Toast.makeText(
            requireContext(),
            "Ошибка добавления пользователя в базу данных.",
            Toast.LENGTH_SHORT)
            .show()
        if((1 shl LOGIN_ALREADY_EXIST_BIT_NUMBER) and code != 0) Toast.makeText(
        requireContext(),
        "Пользователь с таким login уже существует.",
        Toast.LENGTH_SHORT)
        .show()
        if((1 shl EMAIL_ALREADY_EXIST_BIT_NUMBER) and code != 0) Toast.makeText(
            requireContext(),
            "Пользователь с таким email уже существует.",
            Toast.LENGTH_SHORT)
            .show()
    }

    private fun showProgreessBar() = with(binding) {
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