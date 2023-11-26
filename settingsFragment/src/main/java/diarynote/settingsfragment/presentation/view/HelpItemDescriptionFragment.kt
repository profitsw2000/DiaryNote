package diarynote.settingsfragment.presentation.view

import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.style.ImageSpan
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import diarynote.data.domain.HELP_ITEM_ID_BUNDLE
import diarynote.data.model.HelpItemModel
import diarynote.settingsfragment.R
import diarynote.settingsfragment.databinding.FragmentHelpItemDescriptionBinding
import diarynote.settingsfragment.presentation.viewmodel.SettingsViewModel
import diarynote.template.model.HelpState
import org.koin.androidx.viewmodel.ext.android.viewModel

class HelpItemDescriptionFragment : Fragment() {

    private var _binding: FragmentHelpItemDescriptionBinding? = null
    private val binding get() = _binding!!
    private val settingsViewModel: SettingsViewModel by viewModel()
    private val itemId: Int? by lazy { arguments?.getInt(HELP_ITEM_ID_BUNDLE) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHelpItemDescriptionBinding.bind(inflater.inflate(R.layout.fragment_help_item_description, container, false))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeData()
    }

    private fun observeData() {
        val observer = Observer<HelpState?> { renderData(it) }
        settingsViewModel.helpLiveData.observe(viewLifecycleOwner, observer)
    }

    private fun renderData(helpState: HelpState) {
        when(helpState) {
            is HelpState.Error -> handleError(helpState.message)
            HelpState.Loading -> setProgressBarVisible(true)
            is HelpState.Success -> handleSuccess(helpState.helpItemModelList)
        }
    }

    private fun handleError(message: String) {
        setProgressBarVisible(false)
        Snackbar.make(binding.helpItemDescriptionRootLayout, message, Snackbar.LENGTH_INDEFINITE)
            .setAction(getString(diarynote.core.R.string.reload_notes_list_text)) { settingsViewModel.getHelpItemsList(requireContext()) }
            .show()
    }

    private fun setProgressBarVisible(visible: Boolean) = with(binding) {
        if (visible) {
            progressBar.visibility = View.VISIBLE
            helpItemTitleTextView.visibility = View.GONE
            helpItemContentTextView.visibility = View.GONE
        } else {
            progressBar.visibility = View.GONE
            helpItemTitleTextView.visibility = View.VISIBLE
            helpItemContentTextView.visibility = View.VISIBLE
        }
    }

    private fun handleSuccess(helpItemsModelList: List<HelpItemModel>) {
        setProgressBarVisible(false)
        itemId?.let {
            setHelpText(helpItemsModelList[it].title,
                helpItemsModelList[it].description)
        }
    }

    private fun setHelpText(title: String, content: String) = with(binding) {
        //getStringWithImage(content)

        helpItemTitleTextView.text = title
        helpItemContentTextView.text = getStringWithImage(content)
    }

    private fun getStringWithImage(contentText: String): SpannableStringBuilder {
        val ssb = SpannableStringBuilder()
        val stringParts = contentText.split("{", "}")

        stringParts.forEach {
            when(it) {
                "create_note_icon" -> ssb.setSpan(ImageSpan(requireContext(), R.drawable.add_note_icon),
                    ssb.length - 1,
                    ssb.length,
                    0)
                "search_note_icon" -> ssb.setSpan(ImageSpan(requireContext(), diarynote.core.R.drawable.search_icon_24),
                    ssb.length - 1,
                    ssb.length,
                    0)
                "delete_note_icon" -> ssb.setSpan(ImageSpan(requireContext(), diarynote.core.R.drawable.delete_icon_outline_24),
                    ssb.length - 1,
                    ssb.length,
                    0)
                "edit_note_icon" -> ssb.setSpan(ImageSpan(requireContext(), diarynote.core.R.drawable.edit_icon_outlined_24),
                    ssb.length - 1,
                    ssb.length,
                    0)
                "add_category_icon" -> ssb.setSpan(ImageSpan(requireContext(), R.drawable.add_category_icon),
                    ssb.length - 1,
                    ssb.length,
                    0)
                "add_category_icon_grey" -> ssb.setSpan(ImageSpan(requireContext(), R.drawable.add_category_icon_grey),
                    ssb.length - 1,
                    ssb.length,
                    0)
                else -> ssb.append(it)
            }
        }
        return ssb
    }
}