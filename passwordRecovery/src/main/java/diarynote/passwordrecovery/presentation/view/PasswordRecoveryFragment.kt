package diarynote.passwordrecovery.presentation.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import diarynote.core.view.CoreFragment
import diarynote.passwordrecovery.presentation.viewmodel.PasswordRecoveryViewModel
import diarynote.passwordrecovery.R

class PasswordRecoveryFragment : CoreFragment(R.layout.fragment_password_recovery) {

    companion object {
        fun newInstance() = PasswordRecoveryFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_password_recovery, container, false)
    }
}