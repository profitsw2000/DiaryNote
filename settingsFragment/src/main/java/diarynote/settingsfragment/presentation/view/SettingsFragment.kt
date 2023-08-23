package diarynote.settingsfragment.presentation.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import diarynote.data.model.SettingsMenuItemModel
import diarynote.navigator.Navigator
import diarynote.settingsfragment.R
import diarynote.settingsfragment.databinding.FragmentSettingsBinding
import diarynote.settingsfragment.presentation.view.adapter.SettingsAdapter
import diarynote.settingsfragment.presentation.viewmodel.SettingsViewModel
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
        settingsViewModel.getSettingsMenuItemList()
    }

    private fun openFragmentById(itemId: Int) {
        Toast.makeText(requireContext(), "$itemId", Toast.LENGTH_SHORT).show()
    }

    private fun initViews() = with(binding) {
        settingsMenuRecyclerView.adapter = adapter
    }

    private fun observeData() {
        val observer = Observer<List<SettingsMenuItemModel>>() { renderData(it) }
        settingsViewModel.settingsLiveData.observe(viewLifecycleOwner, observer)
    }

    private fun renderData(settingsMenuItemModelList: List<SettingsMenuItemModel>) {
        adapter.setData(settingsMenuItemModelList)
    }
}