package diarynote.settingsfragment.presentation.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import diarynote.settingsfragment.R
import diarynote.settingsfragment.databinding.FragmentGeneralSettingsBinding

class GeneralSettingsFragment : Fragment() {

    private var _binding: FragmentGeneralSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentGeneralSettingsBinding.bind(inflater.inflate(R.layout.fragment_general_settings, container, false))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() = with(binding) {

    }
}