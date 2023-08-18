package diarynote.calendarfragment.presentation.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import diarynote.core.viewmodel.CoreViewModel
import diarynote.data.domain.CURRENT_USER_ID
import diarynote.data.interactor.NoteInteractor
import diarynote.data.mappers.NoteMapper
import diarynote.template.model.NotesState
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
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
        getAllUserNotes(sharedPreferences.getInt(CURRENT_USER_ID,0), false)
    }

    fun getTodayNotes() {
        val today = Date(
            calendar.get(Calendar.YEAR) - 1900,
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        getUserNotesFromDate(sharedPreferences.getInt(CURRENT_USER_ID,0), today, false)
    }

    fun getLastWeekNotes() {
        val dateWeekAgoMilliseconds = Date(calendar.timeInMillis - (6*24*60*60*1000))                   //учитываем текущий день как полный
        val dateWeekAgo = Date(dateWeekAgoMilliseconds.year,
            dateWeekAgoMilliseconds.month,
            dateWeekAgoMilliseconds.date
        )

        getUserNotesFromDate(sharedPreferences.getInt(CURRENT_USER_ID,0), dateWeekAgo, false)
    }

    fun getLastMonthNotes() {
        val dateMonthAgo = getMonthAgoDate(calendar.get(Calendar.YEAR) - 1900,
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        getUserNotesFromDate(sharedPreferences.getInt(CURRENT_USER_ID,0), dateMonthAgo, false)
    }

    fun getLastYearNotes() {
        val daysInYear: Long = if ((calendar.get(Calendar.YEAR)) % 4 == 0) 366
        else 365
        val dateYearAgoMilliseconds = Date(calendar.timeInMillis - ((daysInYear - 1)*24*60*60*1000))        //учитываем текущий день как полный
        val dateYearAgo = Date(dateYearAgoMilliseconds.year,
            dateYearAgoMilliseconds.month,
            dateYearAgoMilliseconds.date
        )

        getUserNotesFromDate(sharedPreferences.getInt(CURRENT_USER_ID,0), dateYearAgo, false)
    }

    fun getNotesInDatePeriod(fromDate: Date, toDate: Date) {
        getUserNotesInDatePeriod(sharedPreferences.getInt(CURRENT_USER_ID,0), fromDate, toDate, false)
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

    private fun getAllUserNotes(userId: Int, remote: Boolean) {
        _notesLiveData.value = NotesState.Loading
        noteInteractor.getAllUserNotes(userId, remote)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    _notesLiveData.value = NotesState.Success(noteMapper.map(it.notesList))
                }, {
                    _notesLiveData.value = it.message?.let { it1 -> NotesState.Error(it1) }
                }
            )
    }

    private fun getUserNotesFromDate(userId: Int, fromDate: Date, remote: Boolean) {
        _notesLiveData.value = NotesState.Loading
        noteInteractor.getUserNotesFromDate(userId, fromDate, remote)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    _notesLiveData.value = NotesState.Success(noteMapper.map(it))
                },
                {
                    _notesLiveData.value = it.message?.let { it1 -> NotesState.Error(it1) }
                }
            )
    }

    private fun getUserNotesInDatePeriod(userId: Int, fromDate: Date, toDate: Date, remote: Boolean) {
        _notesLiveData.value = NotesState.Loading
        noteInteractor.getUserNotesInDatePeriod(userId, fromDate, toDate, remote)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    _notesLiveData.value = NotesState.Success(noteMapper.map(it))
                },
                {
                    _notesLiveData.value = it.message?.let { it1 -> NotesState.Error(it1) }
                }
            )
    }
}