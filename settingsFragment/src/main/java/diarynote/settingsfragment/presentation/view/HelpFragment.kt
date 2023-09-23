package diarynote.settingsfragment.presentation.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import diarynote.navigator.Navigator
import diarynote.settingsfragment.R
import diarynote.settingsfragment.databinding.FragmentHelpBinding
import diarynote.settingsfragment.presentation.view.adapter.HelpAdapter
import diarynote.template.utils.OnSettingsMenuItemClickListener
import org.koin.android.ext.android.inject

class HelpFragment : Fragment() {

    private var _binding: FragmentHelpBinding? = null
    private val binding get() = _binding!!
    private val navigator: Navigator by inject()
    private val adapter = HelpAdapter(object : OnSettingsMenuItemClickListener{
        override fun onItemClick(itemId: Int) {
            openFragmentById()
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
    }

    private fun initViews() {
        adapter.setData(resources.getStringArray(diarynote.core.R.array.help_menu_strings).toList())
        binding.helpItemsListRecyclerView.adapter = adapter
    }

    private fun openFragmentById() {

    }
}