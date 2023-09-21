package diarynote.settingsfragment.presentation.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import diarynote.data.appsettings.SETTINGS_ABOUT_ID
import diarynote.data.appsettings.SETTINGS_ACCOUNT_ID
import diarynote.data.appsettings.SETTINGS_GENERAL_ID
import diarynote.data.appsettings.SETTINGS_HELP_ID
import diarynote.data.appsettings.SETTINGS_LANGUAGE_ID
import diarynote.data.appsettings.SETTINGS_THEME_ID
import diarynote.data.model.SettingsMenuItemModel
import diarynote.data.model.UserModel
import diarynote.navigator.Navigator
import diarynote.settingsfragment.R
import diarynote.settingsfragment.databinding.FragmentSettingsBinding
import diarynote.settingsfragment.presentation.view.adapter.SettingsAdapter
import diarynote.settingsfragment.presentation.viewmodel.SettingsViewModel
import diarynote.template.model.UserState
import diarynote.template.utils.OnSettingsMenuItemClickListener
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private val settingsViewModel: SettingsViewModel by viewModel()
    private val navigator: Navigator by inject()
    private val adapter= SettingsAdapter(object : OnSettingsMenuItemClickListener{
        override fun onItemClick(itemId: Int) {
            openFragmentById(itemId)
        }
    })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSettingsBinding.bind(inflater.inflate(R.layout.fragment_settings, container, false))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observeData()
        settingsViewModel.getSettingsMenuItemList(requireContext())
        settingsViewModel.getCurrentUserInfo()
    }

    private fun openFragmentById(itemId: Int) {
        when(itemId) {
            SETTINGS_ACCOUNT_ID -> navigator.navigateToAccountSettings()
            SETTINGS_THEME_ID -> navigator.navigateToThemeSettings()
            SETTINGS_LANGUAGE_ID -> navigator.navigateToLanguageSettings()
            SETTINGS_GENERAL_ID -> navigator.navigateToGeneralSettings()
            SETTINGS_HELP_ID -> navigator.navigateToHelpScreen()
            SETTINGS_ABOUT_ID -> navigator.navigateToAboutScreen()
        }
    }

    private fun initViews() = with(binding) {
        settingsMenuRecyclerView.adapter = adapter
    }

    private fun observeData() {
        val settingsMenuObserver = Observer<List<SettingsMenuItemModel>> { renderSettingsMenuData(it) }
        settingsViewModel.settingsLiveData.observe(viewLifecycleOwner, settingsMenuObserver)
        val userObserver = Observer<UserState> { renderUserData(it) }
        settingsViewModel.userLiveData.observe(viewLifecycleOwner, userObserver)
    }

    private fun renderSettingsMenuData(settingsMenuItemModelList: List<SettingsMenuItemModel>) {
        adapter.setData(settingsMenuItemModelList)
    }

    private fun renderUserData(userState: UserState) {
        when(userState) {
            is UserState.Success -> { setUserData(userState.userModel) }
            is UserState.Error -> { setErrorUserData() }
            is UserState.Loading -> { setProgressBarVisible(true) }
        }
    }

    private fun setUserData(userModel: UserModel) = with(binding) {
        setProgressBarVisible(false)
        accountLoginTextView.text = userModel.login
        emailTextView.text = userModel.email
    }

    private fun setErrorUserData() = with(binding) {
        setProgressBarVisible(false)
        accountLoginTextView.text = ""
        emailTextView.text = ""
    }

    private fun setProgressBarVisible(visible: Boolean) = with(binding) {
        if (visible) {
            progressBar.visibility = View.VISIBLE
            accountLoginTextView.visibility = View.GONE
            emailTextView.visibility = View.GONE
        } else {
            progressBar.visibility = View.GONE
            accountLoginTextView.visibility = View.VISIBLE
            emailTextView.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}