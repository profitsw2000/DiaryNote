package diarynote.calendarfragment.presentation.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.util.Pair
import androidx.lifecycle.Observer
import com.google.android.material.chip.Chip
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import diarynote.calendarfragment.R
import diarynote.calendarfragment.databinding.FragmentCalendarBinding
import diarynote.calendarfragment.presentation.viewmodel.CalendarViewModel
import diarynote.data.domain.NOTE_MODEL_BUNDLE
import diarynote.data.model.NoteModel
import diarynote.data.model.state.NotesState
import diarynote.navigator.Navigator
import diarynote.template.presentation.adapter.NotesPagedListAdapter
import diarynote.template.utils.OnNoteItemClickListener
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Date

class CalendarFragment : Fragment() {

    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding!!
    private val calendarViewModel: CalendarViewModel by viewModel()
    private val navigator: Navigator by inject()
    private var isCreated = true
    private val adapter = NotesPagedListAdapter(object : OnNoteItemClickListener{
        override fun onItemClick(noteModel: NoteModel) {
            val bundle = Bundle().apply {
                putParcelable(NOTE_MODEL_BUNDLE, noteModel)
            }
            this@CalendarFragment.arguments = bundle
            navigator.navigateToNoteRead(bundle)
        }
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCreated = (savedInstanceState == null)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentCalendarBinding.bind(inflater.inflate(R.layout.fragment_calendar, container, false))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observeData()
        observeChipText()
    }

    private fun initViews() = with(binding) {
        setChipOnClickListeners()
        if(isCreated) calendarViewModel.setSelectPeriodChipDefaultText(
            resources.getString(diarynote.core.R.string.select_period_notes_chip_text),
            pickDateChipGroup.checkedChipId == selectPeriodNotesChip.id//selectPeriodNotesChip.isSelected
        )
        pickedDateNotesRecyclerView.adapter = adapter
        pickedDateNotesRecyclerView.setHasFixedSize(false)
    }

    private fun observeData() {

        calendarViewModel.notesPagedList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        calendarViewModel.notesState.observe(viewLifecycleOwner) {
            when(it) {
                is NotesState.Error -> handleError(it.message)
                NotesState.Loaded -> setProgressBarVisible(false)
                NotesState.Loading -> setProgressBarVisible(true)
                is NotesState.Success -> setProgressBarVisible(false)
            }
        }
    }

    private fun observeChipText() {
        val chipTextObserver = Observer<String> { setSelectPeriodChipText(it) }
        calendarViewModel.selectPeriodChipTextLiveData.observe(viewLifecycleOwner, chipTextObserver)
    }

    private fun setChipOnClickListeners() = with(binding) {
        allTimeNotesChip.setOnClickListener {
            calendarViewModel.getAllNotes()
            observeData()
        }
        todayNotesChip.setOnClickListener {
            calendarViewModel.getTodayNotes()
            observeData()
        }
        lastWeekNotesChip.setOnClickListener {
            calendarViewModel.getLastWeekNotes()
            observeData()
        }
        lastMonthNotesChip.setOnClickListener {
            calendarViewModel.getLastMonthNotes()
            observeData()
        }
        lastYearNotesChip.setOnClickListener {
            calendarViewModel.getLastYearNotes()
            observeData()
        }
        selectPeriodNotesChip.setOnClickListener {
            selectPeriodDialog()
        }
    }

    private fun selectPeriodDialog() {

        val dateRangePicker = MaterialDatePicker
            .Builder
            .dateRangePicker()
            .setTitleText("Выбрать временной период")
            .setSelection(
                Pair(
                    MaterialDatePicker.thisMonthInUtcMilliseconds(),
                    MaterialDatePicker.todayInUtcMilliseconds()
                )
            )
            .build()
        dateRangePicker.show(childFragmentManager, "DATE_PICKER")
        dateRangePicker.addOnPositiveButtonClickListener {
            val beginDate = Date(it.first)
            val endDate = Date(it.second)
            calendarViewModel.getNotesInDatePeriod(beginDate, endDate)
            observeData()
        }
    }

    private fun setSelectPeriodChipText(text: String) = with(binding) {
        selectPeriodNotesChip.text = text
    }

    private fun handleError(message: String) = with(binding) {
        pickedDateNotesRecyclerView.visibility = View.GONE
        progressBar.visibility= View.GONE
        Snackbar.make(this.calendarFragmentRootLayout, message, Snackbar.LENGTH_INDEFINITE)
            .setAction(getString(diarynote.core.R.string.reload_notes_list_text)){
                allTimeNotesChip.isChecked = true}
            .show()
    }


    private fun setProgressBarVisible(visible: Boolean) = with(binding) {
        if (visible) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}