package diarynote.settingsfragment.presentation.view

import android.os.Bundle
import android.util.Log
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

private const val FIRST_SEARCH_FIELD_INDEX = 0
private const val SECOND_SEARCH_FIELD_INDEX = 1
private const val THIRD_SEARCH_FIELD_INDEX = 2

class GeneralSettingsFragment : Fragment() {

    private var _binding: FragmentGeneralSettingsBinding? = null
    private val binding get() = _binding!!
    private val settingsViewModel: SettingsViewModel by viewModel()
    private val searchFieldsList by lazy {
        requireContext().resources.getStringArray(diarynote.core.R.array.note_search_fields_strings)
    }
    //Adapters
    private val firstSearchPriorityFormAdapter by lazy {
        ArrayAdapter<String>(requireContext(), R.layout.drop_down_item)
    }

    private val secondSearchPriorityFormAdapter by lazy {
        ArrayAdapter<String>(requireContext(), R.layout.drop_down_item)
    }

    private val thirdSearchPriorityFormAdapter by lazy {
        ArrayAdapter<String>(requireContext(), R.layout.drop_down_item)
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
        initSearchFieldForms()
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

    private fun initSearchFieldForms() {
        initFirstSearchFieldForm()
        initSecondSearchFieldForm()
        initThirdSearchFieldForm()
    }

    private fun setPasswordRequiredImage() = with(binding) {
        if (settingsViewModel.isPasswordRequired()) {
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

    private fun populateFirstSearchFieldForm() = with(binding){
        firstItemToSearchPickerAutoCompleteTextView.setText(
            settingsViewModel.getSearchPriorityStringsList(searchFieldsList.toList())[FIRST_SEARCH_FIELD_INDEX],
            false
        )
        firstSearchPriorityFormAdapter.clear()
        firstSearchPriorityFormAdapter.addAll(searchFieldsList.toList())
        firstItemToSearchPickerAutoCompleteTextView.setAdapter(firstSearchPriorityFormAdapter)
    }

    private fun populateSecondSearchFieldForm() = with(binding){
        secondItemToSearchPickerAutoCompleteTextView.setText(
            settingsViewModel.getSearchPriorityStringsList(searchFieldsList.toList())[SECOND_SEARCH_FIELD_INDEX],
            false
        )
        secondSearchPriorityFormAdapter.clear()
        secondSearchPriorityFormAdapter.addAll(searchFieldsList.toList())
        secondItemToSearchPickerAutoCompleteTextView.setAdapter(secondSearchPriorityFormAdapter)
    }

    private fun populateThirdSearchFieldForm() = with(binding){
        thirdItemToSearchPickerAutoCompleteTextView.setText(
            settingsViewModel.getSearchPriorityStringsList(searchFieldsList.toList())[THIRD_SEARCH_FIELD_INDEX],
            false
        )
        thirdSearchPriorityFormAdapter.clear()
        thirdSearchPriorityFormAdapter.addAll(searchFieldsList.toList())
        thirdItemToSearchPickerAutoCompleteTextView.setAdapter(thirdSearchPriorityFormAdapter)
    }

    private fun initFirstSearchFieldForm() = with(binding) {
        firstItemToSearchPickerAutoCompleteTextView.setOnItemClickListener { adapterView, _, item, _ ->
            val currentFirstItem = settingsViewModel.getSearchPriorityStringsList(searchFieldsList.toList())[FIRST_SEARCH_FIELD_INDEX]
            val pickedFirstItem = adapterView.adapter.getItem(item).toString()
            val secondItem = secondItemToSearchPickerAutoCompleteTextView.text.toString()
            val thirdItem = thirdItemToSearchPickerAutoCompleteTextView.text.toString()

            when {
                pickedFirstItem == secondItem -> {
/*                    secondItemToSearchPickerAutoCompleteTextView.setText(currentFirstItem)
                    secondSearchPriorityFormAdapter.clear()
                    secondSearchPriorityFormAdapter.addAll(searchFieldsList.toList())
                    secondItemToSearchPickerAutoCompleteTextView.setAdapter(secondSearchPriorityFormAdapter)*/
                    settingsViewModel.saveSearchPriorityList(
                        searchFieldsList.toList(),
                        listOf(pickedFirstItem, currentFirstItem, thirdItem)
                    )
                    populateFirstSearchFieldForm()
                    populateSecondSearchFieldForm()
                }
                pickedFirstItem == thirdItem -> {
/*                    thirdItemToSearchPickerAutoCompleteTextView.setText(currentFirstItem)
                    thirdSearchPriorityFormAdapter.clear()
                    thirdSearchPriorityFormAdapter.addAll(searchFieldsList.toList())
                    thirdItemToSearchPickerAutoCompleteTextView.setAdapter(thirdSearchPriorityFormAdapter)*/
                    settingsViewModel.saveSearchPriorityList(
                        searchFieldsList.toList(),
                        listOf(pickedFirstItem, secondItem, currentFirstItem)
                    )
                    populateFirstSearchFieldForm()
                    populateThirdSearchFieldForm()
                }
            }
        }
    }

    private fun initSecondSearchFieldForm() = with(binding) {
        secondItemToSearchPickerAutoCompleteTextView.setOnItemClickListener { adapterView, _, item, _ ->
            val currentSecondItem = settingsViewModel.getSearchPriorityStringsList(searchFieldsList.toList())[SECOND_SEARCH_FIELD_INDEX]
            val firstItem = firstItemToSearchPickerAutoCompleteTextView.text.toString()
            val pickedSecondItem = adapterView.adapter.getItem(item).toString()
            val thirdItem = thirdItemToSearchPickerAutoCompleteTextView.text.toString()

            when {
                pickedSecondItem == firstItem -> {
/*                    firstItemToSearchPickerAutoCompleteTextView.setText(currentSecondItem)
                    firstSearchPriorityFormAdapter.clear()
                    firstSearchPriorityFormAdapter.addAll(searchFieldsList.toList())
                    firstItemToSearchPickerAutoCompleteTextView.setAdapter(firstSearchPriorityFormAdapter)*/
                    settingsViewModel.saveSearchPriorityList(
                        searchFieldsList.toList(),
                        listOf(currentSecondItem, pickedSecondItem, thirdItem)
                    )
                    populateFirstSearchFieldForm()
                    populateSecondSearchFieldForm()
                }
                pickedSecondItem == thirdItem -> {
/*                    thirdItemToSearchPickerAutoCompleteTextView.setText(currentSecondItem)
                    thirdSearchPriorityFormAdapter.clear()
                    thirdSearchPriorityFormAdapter.addAll(searchFieldsList.toList())
                    thirdItemToSearchPickerAutoCompleteTextView.setAdapter(thirdSearchPriorityFormAdapter)*/
                    settingsViewModel.saveSearchPriorityList(
                        searchFieldsList.toList(),
                        listOf(firstItem, pickedSecondItem, currentSecondItem)
                    )
                    populateSecondSearchFieldForm()
                    populateThirdSearchFieldForm()
                }
            }
        }
    }

    private fun initThirdSearchFieldForm() = with(binding) {
        thirdItemToSearchPickerAutoCompleteTextView.setOnItemClickListener { adapterView, _, item, _ ->
            val currentThirdItem = settingsViewModel.getSearchPriorityStringsList(searchFieldsList.toList())[THIRD_SEARCH_FIELD_INDEX]
            val firstItem = firstItemToSearchPickerAutoCompleteTextView.text.toString()
            val secondItem = secondItemToSearchPickerAutoCompleteTextView.text.toString()
            val pickedThirdItem = adapterView.adapter.getItem(item).toString()

            when {
                pickedThirdItem == firstItem -> {
/*                    firstItemToSearchPickerAutoCompleteTextView.setText(currentThirdItem)
                    firstSearchPriorityFormAdapter.clear()
                    firstSearchPriorityFormAdapter.addAll(searchFieldsList.toList())
                    firstItemToSearchPickerAutoCompleteTextView.setAdapter(firstSearchPriorityFormAdapter)*/
                    settingsViewModel.saveSearchPriorityList(
                        searchFieldsList.toList(),
                        listOf(currentThirdItem, secondItem, pickedThirdItem)
                    )
                    populateFirstSearchFieldForm()
                    populateThirdSearchFieldForm()
                }
                pickedThirdItem == secondItem -> {
/*                    secondItemToSearchPickerAutoCompleteTextView.setText(currentThirdItem)
                    secondSearchPriorityFormAdapter.clear()
                    secondSearchPriorityFormAdapter.addAll(searchFieldsList.toList())
                    secondItemToSearchPickerAutoCompleteTextView.setAdapter(secondSearchPriorityFormAdapter)*/
                    settingsViewModel.saveSearchPriorityList(
                        searchFieldsList.toList(),
                        listOf(firstItem, currentThirdItem, pickedThirdItem)
                    )
                    populateSecondSearchFieldForm()
                    populateThirdSearchFieldForm()
                }
            }
        }
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