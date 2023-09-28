package diarynote.settingsfragment.presentation.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import diarynote.data.appsettings.ENGLISH_LANGUAGE_ABBR
import diarynote.data.appsettings.ENGLISH_LANGUAGE_ID
import diarynote.data.appsettings.RUSSIAN_LANGUAGE_ABBR
import diarynote.data.appsettings.RUSSIAN_LANGUAGE_ID
import diarynote.data.appsettings.appLanguageIdList
import diarynote.data.appsettings.createSettingsMenuList
import diarynote.settingsfragment.R
import diarynote.settingsfragment.databinding.FragmentLanguageBinding
import diarynote.settingsfragment.presentation.view.adapter.SubSettingsAdapter
import diarynote.settingsfragment.presentation.viewmodel.SettingsViewModel
import diarynote.template.utils.OnSettingsMenuItemClickListener
import org.koin.androidx.viewmodel.ext.android.viewModel

class LanguageFragment : Fragment() {

    private var _binding: FragmentLanguageBinding? = null
    private val binding get() = _binding!!
    private val settingsViewModel: SettingsViewModel by viewModel()
    private val adapter = SubSettingsAdapter(object : OnSettingsMenuItemClickListener{
        override fun onItemClick(itemId: Int) {
            if (settingsViewModel.getCurrentLanguageId() != itemId){
                setLanguageById(itemId)
                settingsViewModel.setCurrentLanguageId(itemId)
                activity?.recreate()
            }
        }
    })

    private fun setLanguageById(itemId: Int) {
        when(itemId) {
            RUSSIAN_LANGUAGE_ID -> settingsViewModel.setCurrentLanguage(RUSSIAN_LANGUAGE_ABBR)
            ENGLISH_LANGUAGE_ID -> settingsViewModel.setCurrentLanguage(ENGLISH_LANGUAGE_ABBR)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentLanguageBinding.bind(inflater.inflate(R.layout.fragment_language, container, false))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        val languageSettingsList = createSettingsMenuList(
            appLanguageIdList,
            resources.getStringArray(diarynote.core.R.array.language_settings_strings)
        )
        binding.languageListRecyclerView.adapter = adapter
        settingsViewModel.getCurrentLanguageId()?.let { adapter.setData(languageSettingsList, it) }
    }
}