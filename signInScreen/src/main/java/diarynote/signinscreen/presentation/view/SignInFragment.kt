package diarynote.signinscreen.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import diarynote.core.view.CoreFragment
import diarynote.signinscreen.R
import diarynote.signinscreen.databinding.FragmentSignInBinding

class SignInFragment : CoreFragment(R.layout.fragment_sign_in) {

    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignInBinding.bind(inflater.inflate(R.layout.fragment_sign_in, container, false))
        return binding.root
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