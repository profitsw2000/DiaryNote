package diarynote.passwordrecovery.presentation.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import diarynote.core.common.Controller
import diarynote.core.common.view.Dialoger
import diarynote.core.utils.EMAIL_BIT_NUMBER
import diarynote.core.utils.LOGIN_BIT_NUMBER
import diarynote.core.utils.LOGIN_MIN_LENGTH
import diarynote.core.utils.ROOM_BIT_NUMBER
import diarynote.core.view.CoreFragment
import diarynote.passwordrecovery.presentation.viewmodel.PasswordRecoveryViewModel
import diarynote.passwordrecovery.R
import diarynote.passwordrecovery.databinding.FragmentPasswordRecoveryBinding
import diarynote.passwordrecovery.presentation.viewmodel.model.RecoveryState
import org.koin.androidx.viewmodel.ext.android.viewModel

class PasswordRecoveryFragment : CoreFragment(R.layout.fragment_password_recovery) {

    private var _binding: FragmentPasswordRecoveryBinding? = null
    private val binding get() = _binding!!
    private val passwordRecoveryViewModel: PasswordRecoveryViewModel by viewModel()

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
        _binding = FragmentPasswordRecoveryBinding.bind(inflater.inflate(R.layout.fragment_password_recovery, container, false))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        observeData()
    }

    private fun observeData() {
        val observer = Observer<RecoveryState> { renderData(it) }
        passwordRecoveryViewModel.recoveryState.observe(viewLifecycleOwner, observer)
    }

    private fun renderData(recoveryState: RecoveryState?) {
        when(recoveryState) {
            is RecoveryState.Success -> showSuccessMessage()
            is RecoveryState.Loading -> showProgressBar()
            is RecoveryState.Error -> handleError(recoveryState.code)
            else -> {}
        }
    }

    private fun handleError(code: Int) = with(binding) {
        val dialoger = Dialoger(requireActivity())
        progressBar.visibility = View.GONE

        if((1 shl EMAIL_BIT_NUMBER) and code != 0) emailTextInputLayout.error = getString(
            diarynote.core.R.string.invalid_email_input_message)
        if((1 shl ROOM_BIT_NUMBER) and code != 0) dialoger.showAlertDialog(getString(diarynote.core.R.string.error_dialog_title_text),
            getString(diarynote.core.R.string.email_not_exist_in_db_error_message)
        )

    }

    private fun showProgressBar() = with(binding) {
        progressBar.visibility = View.VISIBLE
    }

    private fun showSuccessMessage() = with(binding) {
        val dialoger = Dialoger(requireActivity())
        progressBar.visibility = View.GONE

        dialoger.showAlertDialog(getString(diarynote.core.R.string.password_recovery_success_dialog_title_text),
            getString(diarynote.core.R.string.email_with_password_was_send)
        )
        emailInputEditText.setText("")
    }

    private fun initViews() = with(binding) {
        sendMailButton.setOnClickListener {
            emailTextInputLayout.error = null
            passwordRecoveryViewModel.passwordRecovery(emailInputEditText.text.toString())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        fun newInstance() = PasswordRecoveryFragment()
    }
}