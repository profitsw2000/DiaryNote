package diarynote.passwordrecovery.presentation.view

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import diarynote.core.common.Controller
import diarynote.core.view.CoreFragment
import diarynote.passwordrecovery.presentation.viewmodel.PasswordRecoveryViewModel
import diarynote.passwordrecovery.R
import diarynote.passwordrecovery.databinding.FragmentPasswordRecoveryBinding

class PasswordRecoveryFragment : CoreFragment(R.layout.fragment_password_recovery) {

    private var _binding: FragmentPasswordRecoveryBinding? = null
    private val binding = _binding!!

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (activity !is Controller) {
            throw IllegalStateException(getString(diarynote.core.R.string.not_controller_activity_exception))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_password_recovery, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        fun newInstance() = PasswordRecoveryFragment()
    }
}