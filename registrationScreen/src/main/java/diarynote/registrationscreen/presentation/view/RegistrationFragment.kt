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

        binding.loginButton.setOnClickListener {
            val login = binding.loginInputEditText.text.toString()
            val email = binding.emailInputEditText.text.toString()
            val password = binding.passwordInputEditText.text.toString()
            val confirmPassword = binding.confirmPasswordInputEditText.text.toString()

            registrationViewModel.registerUser(login, email, password, confirmPassword)
        }

        val observer = Observer<RegState> { renderData(it) }
        registrationViewModel.registrationLiveData.observe(this, observer)
    }

    private fun renderData(regState: RegState?) {
        when(regState) {
            is RegState.Success -> showSuccessMessage(regState.userModel)
            is RegState.Loading -> showProgreessBar()
            is RegState.Error -> handleError(regState.code)
            else -> {}
        }
    }

    private fun showSuccessMessage(userModel: UserModel) {
        Toast.makeText(
            requireContext(),
            "Пользователь ${userModel.login} был успешно создан.",
            Toast.LENGTH_SHORT)
            .show()
    }

    private fun handleError(code: Int) {
        when(code) {
            (1 shl LOGIN_BIT_NUMBER) and code -> {}
            (1 shl EMAIL_BIT_NUMBER) and code -> {}
            (1 shl PASSWORD_BIT_NUMBER) and code -> {}
            (1 shl CONFIRM_PASSWORD_BIT_NUMBER) and code -> {}
            (1 shl ROOM_BIT_NUMBER) and code -> {}
        }
    }

    private fun showProgreessBar() {
        TODO("Not yet implemented")
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        fun newInstance() = RegistrationFragment()
    }
}