package diarynote.signinscreen.presentation.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import diarynote.core.common.Controller
import diarynote.core.view.CoreFragment
import diarynote.signinscreen.R
import diarynote.signinscreen.databinding.FragmentSignInBinding

class SignInFragment : CoreFragment(R.layout.fragment_sign_in) {

    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!
    private val controller by lazy { activity as Controller }

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
            val login = binding.loginInputEditText.text
            val password = binding.passwordInputEditText.text

            val loginIsRight = !(login?.contains(Regex("[^A-Za-z0-9]")))!! && (login.length > 3)
            val passwordIsRight = !(password?.contains(Regex("[^A-Za-z0-9]")))!! && (password.length > 7)

            if (loginIsRight && passwordIsRight) {
                binding.loginErrorTextTextView.visibility = View.GONE
            } else {
                binding.loginErrorTextTextView.visibility = View.VISIBLE
                binding.loginErrorTextTextView.text = "Неверный логин или пароль."
            }
        }
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