package diarynote.settingsfragment.presentation.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import diarynote.core.common.dialog.data.DialogerImpl
import diarynote.core.utils.listener.OnDialogItemClickListener
import diarynote.settingsfragment.R
import diarynote.settingsfragment.databinding.FragmentGeneralSettingsBinding
import diarynote.settingsfragment.presentation.viewmodel.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class GeneralSettingsFragment : Fragment() {

    private var _binding: FragmentGeneralSettingsBinding? = null
    private val binding get() = _binding!!
    private val settingsViewModel: SettingsViewModel by viewModel()
    //Adapters
    private val firstFieldSearchPriorityAdapter by lazy {
        ArrayAdapter.createFromResource(requireContext(), diarynote.core.R.array.note_search_fields_strings, R.layout.drop_down_item)
    }
    private val secondFieldSearchPriorityAdapter by lazy {
        ArrayAdapter.createFromResource(requireContext(), diarynote.core.R.array.note_search_fields_strings, R.layout.drop_down_item)
    }
    private val thirdFieldSearchPriorityAdapter by lazy {
        ArrayAdapter.createFromResource(requireContext(), diarynote.core.R.array.note_search_fields_strings, R.layout.drop_down_item)
    }

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

    private fun initViews() {
        initPasswordRequiredField()
        initAccountQuitTimeField()
        populateSearchPrioritySettingsForms()
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

    private fun populateSearchPrioritySettingsForms() {
        populateFirstSearchFieldForm()
        populateSecondSearchFieldForm()
        populateThirdSearchFieldForm()
    }

    private fun populateFirstSearchFieldForm() {
        binding.firstItemToSearchPickerAutoCompleteTextView.setText(
            settingsViewModel.getSearchPriorityList()[0]
        )
        binding.firstItemToSearchPickerAutoCompleteTextView.setAdapter(firstFieldSearchPriorityAdapter)
    }

    private fun populateSecondSearchFieldForm() {
        binding.secondItemToSearchPickerAutoCompleteTextView.setText(
            settingsViewModel.getSearchPriorityList()[1]
        )
        binding.secondItemToSearchPickerAutoCompleteTextView.setAdapter(secondFieldSearchPriorityAdapter)
    }

    private fun populateThirdSearchFieldForm() {
        binding.thirdItemToSearchPickerAutoCompleteTextView.setText(
            settingsViewModel.getSearchPriorityList()[2]
        )
        binding.thirdItemToSearchPickerAutoCompleteTextView.setAdapter(thirdFieldSearchPriorityAdapter)
    }

    private fun initAccountQuitTimeField() = with(binding) {

        val items = resources.getStringArray(diarynote.core.R.array.unactive_time_period_strings)

        val dialoger = DialogerImpl(
            requireActivity(),
            object : OnDialogItemClickListener{
                override fun onItemClick(timePeriod: String?) {
                    var chosen = 0

                    when(timePeriod){
                        items[0] -> {
                            quitAccountSelectedTimeText.text = timePeriod
                            chosen = 0
                        }
                        items[1] -> {
                            quitAccountSelectedTimeText.text = timePeriod
                            chosen = 1
                        }
                        items[2] -> {
                            quitAccountSelectedTimeText.text = timePeriod
                            chosen = 2
                        }
                        items[3] -> {
                            quitAccountSelectedTimeText.text = timePeriod
                            chosen = 3
                        }
                    }
                    settingsViewModel.setCurrentInactiveTimePeriodIndex(chosen)
                }
            }
        )
        quitAccountSelectedTimeText.text = items[settingsViewModel.getCurrentInactiveTimePeriodIndex()]
        quitAccountSelectedTimeText.setOnClickListener {
            dialoger.showDialogWithSingleChoice(
                getString(diarynote.core.R.string.quit_inactive_account_text),
                getString(diarynote.core.R.string.dialog_button_cancel_text),
                items,
                settingsViewModel.getCurrentInactiveTimePeriodIndex()
            )
        }
    }
}