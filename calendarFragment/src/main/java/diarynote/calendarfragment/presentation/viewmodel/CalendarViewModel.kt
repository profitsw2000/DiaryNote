package diarynote.calendarfragment.presentation.viewmodel

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.MutableLiveData
import diarynote.core.viewmodel.CoreViewModel
import diarynote.data.interactor.NoteInteractor
import diarynote.data.mappers.NoteMapper
import diarynote.data.model.state.NotesState
import java.util.Calendar
import java.util.Date

class CalendarViewModel(
    private val noteInteractor: NoteInteractor,
    private val sharedPreferences: SharedPreferences,
    private val noteMapper: NoteMapper
) : CoreViewModel() {

    private val calendar = Calendar.getInstance()

    private val _notesLiveData = MutableLiveData<NotesState>()
    val notesLiveData by this::_notesLiveData

    fun getAllNotes() {

    }

    fun getTodayNotes() {
        val today = Date(
            calendar.get(Calendar.YEAR) - 1900,
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
    }

    fun getLastWeekNotes() {
        val dateWeekAgoMilliseconds = Date(calendar.timeInMillis - (6*24*60*60*1000))                   //учитываем текущий день как полный
        val dateWeekAgo = Date(dateWeekAgoMilliseconds.year,
            dateWeekAgoMilliseconds.month,
            dateWeekAgoMilliseconds.date
        )
    }

    fun getLastMonthNotes() {
        val dateMonthAgo = getMonthAgoDate(calendar.get(Calendar.YEAR) - 1900,
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
    }

    fun getLastYearNotes() {
        val daysInYear: Long = if ((calendar.get(Calendar.YEAR)) % 4 == 0) 366
        else 365
        val dateYearAgoMilliseconds = Date(calendar.timeInMillis - ((daysInYear - 1)*24*60*60*1000))        //учитываем текущий день как полный
        val dateYearAgo = Date(dateYearAgoMilliseconds.year,
            dateYearAgoMilliseconds.month,
            dateYearAgoMilliseconds.date
        )
    }

    private fun getMonthAgoDate(year: Int, month: Int, day: Int): Date {
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
    }
}