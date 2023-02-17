package diarynote.signinscreen.presentation.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import diarynote.core.common.Controller
import diarynote.core.utils.*
import diarynote.core.view.CoreFragment
import diarynote.data.model.UserModel
import diarynote.signinscreen.R
import diarynote.signinscreen.databinding.FragmentSignInBinding
import diarynote.signinscreen.model.LoginState
import diarynote.signinscreen.presentation.viewmodel.SignInViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SignInFragment : CoreFragment(R.layout.fragment_sign_in) {

    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!
    private val controller by lazy { activity as Controller }
    private val signInViewModel: SignInViewModel by viewModel()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (activity !is Controller) {
            throw IllegalStateException("Activity должна наследоваться от Controller")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignInBinding.bind(inflater.inflate(R.layout.fragment_sign_in, container, false))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()

        val observer = Observer<LoginState> { renderData(it) }
        signInViewModel.loginResultLiveData.observe(viewLifecycleOwner, observer)
    }

    private fun initViews() = with(binding) {
        registrationButton.setOnClickListener {
            controller.openRegistrationFragment()
        }

        forgotPasswordTextView.setOnClickListener {
            controller.openPasswordRecoveryFragment()
        }

        loginButton.setOnClickListener {
            val login = loginInputEditText.text.toString()
            val password = passwordInputEditText.text.toString()
            loginTextInputLayout.error = null
            passwordTextInputLayout.error = null
            signInViewModel.signIn(login, password)
        }
    }

    private fun renderData(loginState: LoginState) {
        when(loginState) {
            is LoginState.Loading -> {binding.progressBar.visibility = View.VISIBLE}
            is LoginState.LoginSuccess -> enterApp()
            is LoginState.Error -> setErrorMessage(loginState.errorCode)
        }
    }

    private fun setErrorMessage(errorCode: Int) = with(binding) {
            if((1 shl LOGIN_BIT_NUMBER) and errorCode != 0) loginTextInputLayout.error = "Не менее 4 буквенных или цифровых символов"
            if((1 shl INVALID_PASSWORD_BIT_NUMBER) and errorCode != 0) setSignInErrorMessage()
            if((1 shl PASSWORD_BIT_NUMBER) and errorCode != 0) passwordTextInputLayout.error = "Не менее 8 буквенных или цифровых символов"
    }

    private fun setSignInErrorMessage() = with(binding) {
        progressBar.visibility = View.GONE
        loginErrorTextTextView.visibility = View.VISIBLE
        loginErrorTextTextView.text = "Неверный логин и/или пароль"
    }

    private fun enterApp() {
        binding.progressBar.visibility = View.GONE
        binding.loginErrorTextTextView.visibility = View.GONE
        Toast.makeText(context, "Успешный вход!", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = SignInFragment()
    }
}