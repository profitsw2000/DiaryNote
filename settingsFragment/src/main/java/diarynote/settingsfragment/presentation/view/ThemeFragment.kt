package diarynote.settingsfragment.presentation.view

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import diarynote.data.appsettings.APP_THEME_DARK
import diarynote.data.appsettings.APP_THEME_LIGHT
import diarynote.settingsfragment.R
import diarynote.settingsfragment.databinding.FragmentThemeBinding
import diarynote.settingsfragment.presentation.viewmodel.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ThemeFragment : Fragment() {

    private var _binding: FragmentThemeBinding? = null
    private val binding get() = _binding!!
    private val settingsViewModel: SettingsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentThemeBinding.bind(inflater.inflate(R.layout.fragment_theme, container, false))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observeData()
    }

    private fun initViews() {
        binding.darkThemeButton.setOnClickListener {
            if (settingsViewModel.getCurrentTheme() == APP_THEME_LIGHT) {
                settingsViewModel.setCurrentTheme(APP_THEME_DARK)
                activity?.recreate()
            }
        }
        binding.lightThemeButton.setOnClickListener {
            if (settingsViewModel.getCurrentTheme() == APP_THEME_DARK) {
                settingsViewModel.setCurrentTheme(APP_THEME_LIGHT)
                activity?.recreate()
            }
        }
    }

    private fun observeData() {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}