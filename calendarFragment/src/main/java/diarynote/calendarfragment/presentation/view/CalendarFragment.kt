package diarynote.calendarfragment.presentation.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.util.Pair
import androidx.lifecycle.Observer
import com.google.android.material.chip.Chip
import com.google.android.material.datepicker.MaterialDatePicker
import diarynote.calendarfragment.R
import diarynote.calendarfragment.databinding.FragmentCalendarBinding
import diarynote.calendarfragment.presentation.viewmodel.CalendarViewModel
import diarynote.template.model.NotesState
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Date

class CalendarFragment : Fragment() {

    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding!!
    private val calendarViewModel: CalendarViewModel by viewModel()

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
    }

    private fun initViews() {
        binding.pickDateChipGroup.setOnCheckedStateChangeListener { group, checkedIds ->
            val chipText: String = group.findViewById<Chip>(checkedIds[0]).text.toString()
            getNotesByDate(chipText)
        }
        binding.selectPeriodNotesChip.setOnClickListener {
            selectPeriodDialog()
        }
    }

    private fun observeData() {
        val observer = Observer<NotesState> { renderData(it) }
        calendarViewModel.notesLiveData.observe(viewLifecycleOwner, observer)
    }

    private fun renderData(notesState: NotesState) {
        when(notesState) {
            is NotesState.Success -> {}
            is NotesState.Loading -> {}
            is NotesState.Error -> {}
        }
    }

    private fun getNotesByDate(period: String) {
        when (period) {
            resources.getString(diarynote.core.R.string.all_time_notes_chip_text) ->
                calendarViewModel.getAllNotes()
            resources.getString(diarynote.core.R.string.todays_notes_chip_text) ->
                calendarViewModel.getTodayNotes()
            resources.getString(diarynote.core.R.string.last_week_notes_chip_text) ->
                calendarViewModel.getLastWeekNotes()
            resources.getString(diarynote.core.R.string.last_month_notes_chip_text) ->
                calendarViewModel.getLastMonthNotes()
            resources.getString(diarynote.core.R.string.last_year_notes_chip_text) ->
                calendarViewModel.getLastYearNotes()
            resources.getString(diarynote.core.R.string.select_period_notes_chip_text) -> selectPeriodDialog()
            else -> {  }
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
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}