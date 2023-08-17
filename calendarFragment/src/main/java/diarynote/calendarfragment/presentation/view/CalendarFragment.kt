package diarynote.calendarfragment.presentation.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.chip.Chip
import diarynote.calendarfragment.R
import diarynote.calendarfragment.databinding.FragmentCalendarBinding
import java.util.Calendar
import java.util.Date

class CalendarFragment : Fragment() {

    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding!!
    private val calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCalendarBinding.bind(inflater.inflate(R.layout.fragment_calendar, container, false))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews(){
        binding.pickDateChipGroup.setOnCheckedStateChangeListener { group, checkedIds ->
            val chipText: String = group.findViewById<Chip>(checkedIds[0]).text.toString()

            getNotesByDate(chipText)
        }
    }

    private fun getNotesByDate(period: String) {
        when (period) {
            resources.getString(diarynote.core.R.string.all_time_notes_chip_text) ->
                getAllNotes()
            resources.getString(diarynote.core.R.string.todays_notes_chip_text) ->
                getTodayNotes()
            resources.getString(diarynote.core.R.string.last_week_notes_chip_text) ->
                getLastWeekNotes()
            resources.getString(diarynote.core.R.string.last_month_notes_chip_text) ->
                getLastMonthNotes()
            resources.getString(diarynote.core.R.string.last_year_notes_chip_text) ->
                getLastYearNotes()
            resources.getString(diarynote.core.R.string.select_period_notes_chip_text) ->
                selectPeriodDialog()
            else -> {  }
        }
    }

    private fun getAllNotes() {

    }

    private fun getTodayNotes() {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val today = Date(
            year,
            month,
            day
        )
        Log.d("VVV", "getTodayNotes: $today")

        Toast.makeText(requireContext(), today.toString(), Toast.LENGTH_SHORT).show()
        //viewModel.getNotesByDate(date)
    }

    private fun getLastWeekNotes() {
        val fromDateMilliseconds = Date(calendar.timeInMillis - (6*24*60*60*1000))
        val fromDate = Date(fromDateMilliseconds.year,
            fromDateMilliseconds.month,
            fromDateMilliseconds.day
        )
        Log.d("VVV", "getLastWeekNotes: $fromDate")
        Toast.makeText(requireContext(), fromDate.toString(), Toast.LENGTH_SHORT).show()
    }

    private fun getLastMonthNotes() {
        val dateMonthAgo = getMonthAgoDate(calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        Log.d("VVV", "getLastMonthNotes: $dateMonthAgo")
        Toast.makeText(requireContext(), dateMonthAgo.toString(), Toast.LENGTH_SHORT).show()
    }

    private fun getLastYearNotes() {

    }

    private fun selectPeriodDialog() {

    }

    private fun getMonthAgoDate(year: Int, month: Int, day: Int): Date {
        val previousMonth = if (month == Calendar.JANUARY) Calendar.DECEMBER
                            else month - 1
        val yearPreviousMonth = if (month == Calendar.JANUARY) year
                            else year - 1
        val dayPreviousMonth = if (day > getMonthLastDayNumber(year, month)) day
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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}