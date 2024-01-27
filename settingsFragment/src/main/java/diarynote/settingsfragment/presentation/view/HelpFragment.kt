package diarynote.settingsfragment.presentation.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import diarynote.data.domain.HELP_ITEM_ID_BUNDLE
import diarynote.data.model.HelpItemModel
import diarynote.navigator.Navigator
import diarynote.settingsfragment.R
import diarynote.settingsfragment.databinding.FragmentHelpBinding
import diarynote.settingsfragment.presentation.view.adapter.HelpAdapter
import diarynote.settingsfragment.presentation.viewmodel.SettingsViewModel
import diarynote.data.model.state.HelpState
import diarynote.template.utils.OnSettingsMenuItemClickListener
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class HelpFragment : Fragment() {

    private var _binding: FragmentHelpBinding? = null
    private val binding get() = _binding!!
    private val settingsViewModel: SettingsViewModel by viewModel()
    private val navigator: Navigator by inject()
    private val adapter = HelpAdapter(object : OnSettingsMenuItemClickListener{
        override fun onItemClick(itemId: Int) {
            openFragmentById(itemId)
        }
    })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHelpBinding.bind(inflater.inflate(R.layout.fragment_help, container, false))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observeData()
        if (savedInstanceState == null) settingsViewModel.getHelpItemsList(requireContext())
    }

    private fun initViews() {
        adapter.setData(resources.getStringArray(diarynote.core.R.array.help_menu_strings).toList())
        binding.helpItemsListRecyclerView.adapter = adapter
    }

    private fun observeData() {
        val observer = Observer<HelpState?> { renderData(it) }
        settingsViewModel.helpLiveData.observe(viewLifecycleOwner, observer)
    }

    private fun renderData(helpState: HelpState) {
        when(helpState) {
            is HelpState.Error -> handleError("Не удалось загрузить справочную информацию")
            HelpState.Loading -> setProgressBarVisible(true)
            is HelpState.Success -> handleSuccess(helpState.helpItemModelList)
        }
    }

    private fun handleError(message: String) {
        setProgressBarVisible(false)
        Snackbar.make(binding.helpFragmentRootLayout, message, Snackbar.LENGTH_INDEFINITE)
            .setAction(getString(diarynote.core.R.string.reload_notes_list_text)) { settingsViewModel.getHelpItemsList(requireContext()) }
            .show()
    }

    private fun setProgressBarVisible(visible: Boolean) = with(binding) {
        if (visible) {
            progressBar.visibility = View.VISIBLE
            helpItemsListRecyclerView.visibility = View.GONE
        } else {
            progressBar.visibility = View.GONE
            helpItemsListRecyclerView.visibility = View.VISIBLE
        }
    }

    private fun handleSuccess(helpItemsModelList: List<HelpItemModel>) {
        setProgressBarVisible(false)
        adapter.setData(getHelpItemsTitles(helpItemsModelList))
    }

    private fun getHelpItemsTitles(helpItemsModelList: List<HelpItemModel>) : List<String> {
        return mutableListOf<String>().apply {
            helpItemsModelList.forEach {
                this.add(it.title)
            }
        }
    }

    private fun openFragmentById(itemId: Int) {

        val bundle = Bundle().apply {
            putInt(HELP_ITEM_ID_BUNDLE, itemId)
        }

        this@HelpFragment.arguments = bundle
        navigator.navigateToHelpDescription(bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}