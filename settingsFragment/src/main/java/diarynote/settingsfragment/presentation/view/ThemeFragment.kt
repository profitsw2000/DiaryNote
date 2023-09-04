package diarynote.settingsfragment.presentation.view

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
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
    }

    private fun initViews() = with(binding) {
        initSwitch()
        initDayThemeButton()
        initDarkThemeButton()
    }

    private fun initDarkThemeButton() {
        binding.darkThemeButton.setOnClickListener {
            changeFromDefaultDeviceMode(APP_THEME_LIGHT)
            changeTheme(APP_THEME_LIGHT, APP_THEME_DARK)
        }
    }

    private fun initDayThemeButton() {
        binding.lightThemeButton.setOnClickListener {
            changeFromDefaultDeviceMode(APP_THEME_DARK)
            changeTheme(APP_THEME_DARK, APP_THEME_LIGHT)
        }
    }

    private fun initSwitch() {
        binding.defaultThemePickSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                settingsViewModel.setFromDefaultDeviceMode(true)
                when(requireContext().resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
                    Configuration.UI_MODE_NIGHT_YES -> changeTheme(APP_THEME_LIGHT, APP_THEME_DARK)
                    Configuration.UI_MODE_NIGHT_NO -> changeTheme(APP_THEME_DARK, APP_THEME_LIGHT)
                    else -> {}
                }
            } else {
                settingsViewModel.setFromDefaultDeviceMode(false)
            }
        }
    }

    private fun changeTheme(fromTheme: Int, toTheme: Int) {
        if (settingsViewModel.getCurrentTheme() == fromTheme) {
            settingsViewModel.setCurrentTheme(toTheme)
            activity?.recreate()
        }
    }

    private fun changeFromDefaultDeviceMode(theme: Int) {
        if (settingsViewModel.getFromDefaultDeviceMode() && settingsViewModel.getCurrentTheme() == theme) {
            //settingsViewModel.setFromDefaultDeviceMode(false)
            binding.defaultThemePickSwitch.isChecked = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}