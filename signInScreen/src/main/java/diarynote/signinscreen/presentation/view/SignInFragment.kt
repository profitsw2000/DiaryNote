package diarynote.signinscreen.presentation.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import diarynote.core.common.Controller
import diarynote.core.utils.LOGIN_MIN_LENGTH
import diarynote.core.utils.LOGIN_PATTERN
import diarynote.core.utils.PASSWORD_MIN_LENGTH
import diarynote.core.utils.PASSWORD_PATTERN
import diarynote.core.view.CoreFragment
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

        binding.registrationButton.setOnClickListener {
            controller.openRegistrationFragment()
        }

        binding.forgotPasswordTextView.setOnClickListener {
            controller.openPasswordRecoveryFragment()
        }

        binding.loginButton.setOnClickListener {
            val login = binding.loginInputEditText.text.toString()
            val password = binding.passwordInputEditText.text.toString()

            signInViewModel.signIn(login, password)
        }

        val observer = Observer<LoginState> { renderData(it) }
        signInViewModel.loginResultLiveData.observe(this, observer)
    }

    private fun renderData(loginState: LoginState) {
        when(loginState) {
            is LoginState.Loading -> {}
            is LoginState.LoginSuccess -> {binding.loginErrorTextTextView.visibility = View.GONE}
            is LoginState.Error -> setErrorMessage(loginState.message)
        }
    }

    private fun setErrorMessage(message: String) {
        binding.loginErrorTextTextView.visibility = View.VISIBLE
        binding.loginErrorTextTextView.text = message
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