package diarynote.registrationscreen

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import diarynote.core.view.CoreFragment
import diarynote.registrationScreen.R

class RegistrationFragment : CoreFragment(R.layout.fragment_registration) {

    companion object {
        fun newInstance() = RegistrationFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_registration, container, false)
    }

}