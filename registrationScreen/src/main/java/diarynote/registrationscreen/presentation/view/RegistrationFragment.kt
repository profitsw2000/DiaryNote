package diarynote.registrationscreen.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import diarynote.core.view.CoreFragment
import diarynote.registrationScreen.R
import diarynote.registrationScreen.databinding.FragmentRegistrationBinding
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

        binding.loginButton.setOnClickListener {
            val login = binding.loginInputEditText.text.toString()
            val email = binding.emailInputEditText.text.toString()
            val password = binding.passwordInputEditText.text.toString()
            val confirmPassword = binding.confirmPasswordInputEditText.text.toString()

            registrationViewModel.registerUser(login, email, password, confirmPassword)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        fun newInstance() = RegistrationFragment()
    }
}