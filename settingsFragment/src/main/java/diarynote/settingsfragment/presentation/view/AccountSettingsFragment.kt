package diarynote.settingsfragment.presentation.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import diarynote.data.model.SettingsMenuItemModel
import diarynote.navigator.Navigator
import diarynote.settingsfragment.R
import diarynote.settingsfragment.databinding.FragmentAccountSettingsBinding
import diarynote.settingsfragment.presentation.view.adapter.SubSettingsAdapter
import diarynote.settingsfragment.presentation.viewmodel.SettingsViewModel
import diarynote.template.utils.OnSettingsMenuItemClickListener
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class AccountSettingsFragment : Fragment() {

    private var _binding: FragmentAccountSettingsBinding? = null
    private val binding get() = _binding!!
    private val settingsViewModel: SettingsViewModel by viewModel()
    private val navigator: Navigator by inject()
    private val adapter = SubSettingsAdapter(object : OnSettingsMenuItemClickListener{
        override fun onItemClick(itemId: Int) {

        }
    })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAccountSettingsBinding.bind(inflater.inflate(R.layout.fragment_account_settings, container, false))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observeData()
        settingsViewModel.getAccountSettingsMenuItemList()
    }

    private fun initViews() = with(binding) {
        accountSettingsRecyclerView.adapter = adapter
    }

    private fun observeData() {
        val accountSettingsObserver = Observer<List<SettingsMenuItemModel>> { renderAccountSettingsMenuData(it) }
        settingsViewModel.settingsLiveData.observe(viewLifecycleOwner, accountSettingsObserver)
    }

    private fun renderAccountSettingsMenuData(accountSettingsMenuItemModelList: List<SettingsMenuItemModel>) {
        adapter.setData(accountSettingsMenuItemModelList)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}