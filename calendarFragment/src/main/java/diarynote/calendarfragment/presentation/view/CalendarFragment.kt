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
import diarynote.calendarfragment.R
import diarynote.calendarfragment.databinding.FragmentCalendarBinding
import diarynote.calendarfragment.presentation.viewmodel.CalendarViewModel
import diarynote.data.model.state.NotesState
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Date

class CalendarFragment : Fragment() {

    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding!!
    //private val calendar = Calendar.getInstance()
    private val calendarViewModel: CalendarViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
            resources.getString(diarynote.core.R.string.select_period_notes_chip_text) -> {}
            else -> {  }
        }
    }/*

    private fun getAllNotes() {

    }

    private fun getTodayNotes() {
        val today = Date(
            calendar.get(Calendar.YEAR) - 1900,
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        Log.d("VVV", "getTodayNotes: $today")
        //viewModel.getNotesByDate(date)
    }

    private fun getLastWeekNotes() {
        val dateWeekAgoMilliseconds = Date(calendar.timeInMillis - (6*24*60*60*1000))                   //учитываем текущий день как полный
        val dateWeekAgo = Date(dateWeekAgoMilliseconds.year,
            dateWeekAgoMilliseconds.month,
            dateWeekAgoMilliseconds.date
        )
        Log.d("VVV", "getLastWeekNotes: $dateWeekAgo")
    }

    private fun getLastMonthNotes() {
        val dateMonthAgo = getMonthAgoDate(calendar.get(Calendar.YEAR) - 1900,
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        Log.d("VVV", "getLastMonthNotes: $dateMonthAgo")
    }

    private fun getLastYearNotes() {
        val daysInYear: Long = if ((calendar.get(Calendar.YEAR)) % 4 == 0) 366
                        else 365
        val dateYearAgoMilliseconds = Date(calendar.timeInMillis - ((daysInYear - 1)*24*60*60*1000))        //учитываем текущий день как полный
        val dateYearAgo = Date(dateYearAgoMilliseconds.year,
            dateYearAgoMilliseconds.month,
            dateYearAgoMilliseconds.date
        )
        Log.d("VVV", "getLastYearNotes: $dateYearAgo")
    }*/

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
            Log.d("VVV", "selectPeriodDialog: $beginDate" +
                    "\n$endDate")
        }
    }

/*    private fun getMonthAgoDate(year: Int, month: Int, day: Int): Date {
        val previousMonth = if (month == Calendar.JANUARY) Calendar.DECEMBER
                            else month - 1
        val yearPreviousMonth = if (month == Calendar.JANUARY) year - 1
                            else year
        val dayPreviousMonth = if (day < getMonthLastDayNumber(year, month)) day
                            else getMonthLastDayNumber(year, month)

        return Date(yearPreviousMonth, previousMonth, dayPreviousMonth)
    }

    private fun getMonthLastDayNumber(year: Int, month: Int): Int {
        return when(month) {
            Calendar.JANUARY -> 31
            Calendar.FEBRUARY -> { if (year%4 == 0) 29
                else 28
            }
            Calendar.MARCH -> 31
            Calendar.APRIL -> 30
            Calendar.MAY -> 31
            Calendar.JUNE -> 30
            Calendar.JULY -> 31
            Calendar.AUGUST -> 31
            Calendar.SEPTEMBER -> 30
            Calendar.OCTOBER -> 31
            Calendar.NOVEMBER -> 30
            Calendar.DECEMBER -> 31
            else -> 0
        }
    }*/

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}