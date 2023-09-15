package diarynote.settingsfragment.presentation.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import diarynote.settingsfragment.R
import diarynote.settingsfragment.databinding.FragmentGeneralSettingsBinding
import diarynote.settingsfragment.presentation.viewmodel.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class GeneralSettingsFragment : Fragment() {

    private var _binding: FragmentGeneralSettingsBinding? = null
    private val binding get() = _binding!!
    private val settingsViewModel: SettingsViewModel by viewModel()

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
        initPasswordRequiredField()
    }

    private fun initPasswordRequiredField() = with(binding) {
        setPasswordRequiredImage()
        usePasswordConstraintLayout.setOnClickListener {
            settingsViewModel.isPasswordRequired()?.let {
                    it1 -> settingsViewModel.setPasswordRequired(!it1)
            }
            setPasswordRequiredImage()
        }
    }

    private fun setPasswordRequiredImage() = with(binding) {
        if (settingsViewModel.isPasswordRequired() == true) {
            subSettingsItemImageView.setImageResource(R.drawable.checked_item)
        } else {
            subSettingsItemImageView.setImageResource(R.drawable.unchecked_item)
        }
    }
}