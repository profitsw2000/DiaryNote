package diarynote.calendarfragment.presentation.viewmodel

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import diarynote.core.utils.ALL_NOTES_CHIP_SELECTED
import diarynote.core.utils.FOR_DATE_PERIOD_NOTES_CHIP_SELECTED
import diarynote.core.utils.LAST_MONTH_NOTES_CHIP_SELECTED
import diarynote.core.utils.LAST_WEEK_NOTES_CHIP_SELECTED
import diarynote.core.utils.LAST_YEAR_NOTES_CHIP_SELECTED
import diarynote.core.utils.TODAY_NOTES_CHIP_SELECTED
import diarynote.core.viewmodel.CoreViewModel
import diarynote.data.domain.CURRENT_USER_ID
import diarynote.data.interactor.NoteInteractor
import diarynote.data.mappers.NoteMapper
import diarynote.data.model.NoteModel
import diarynote.data.model.type.DataSourceType
import diarynote.data.room.entity.NoteEntity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

class CalendarViewModel(
    private val noteInteractor: NoteInteractor,
    private val sharedPreferences: SharedPreferences,
    private val noteMapper: NoteMapper
) : CoreViewModel() {

    var selectedChip = 0
    var fromDate: Date = Date(0L)
    var toDate: Date = Date(0L)

    private val calendar = Calendar.getInstance()
    private var selectPeriodDefaultText: String = ""

    val allNotesList: LiveData<List<NoteEntity>> = noteInteractor.getAll(false)

    private val _selectPeriodChipTextLiveData = MutableLiveData<String>()
    val selectPeriodChipTextLiveData by this::_selectPeriodChipTextLiveData

    private lateinit var _notesPagedList: LiveData<PagedList<NoteModel>>
    val notesPagedList: LiveData<PagedList<NoteModel>> by this::_notesPagedList

    private lateinit var _notesState: LiveData<diarynote.data.model.state.NotesState>
    val notesState: LiveData<diarynote.data.model.state.NotesState> by this::_notesState

    init {
        getAllNotes()
        allNotesList.observeForever {
            getNotesList(selectedChip, fromDate, toDate)
            Log.d("VVV", "Db updated! ")
        }
    }

    fun getNotesList(selectedChip: Int) {
        when(selectedChip) {
            ALL_NOTES_CHIP_SELECTED -> getAllNotes()
            TODAY_NOTES_CHIP_SELECTED -> getTodayNotes()
            LAST_WEEK_NOTES_CHIP_SELECTED -> getLastWeekNotes()
            LAST_MONTH_NOTES_CHIP_SELECTED -> getLastMonthNotes()
            LAST_YEAR_NOTES_CHIP_SELECTED -> getLastYearNotes()
            else -> {}
        }
    }

    fun getNotesList(selectedChip: Int, fromDate: Date, toDate: Date) {
        when(selectedChip) {
            ALL_NOTES_CHIP_SELECTED -> getAllNotes()
            TODAY_NOTES_CHIP_SELECTED -> getTodayNotes()
            LAST_WEEK_NOTES_CHIP_SELECTED -> getLastWeekNotes()
            LAST_MONTH_NOTES_CHIP_SELECTED -> getLastMonthNotes()
            LAST_YEAR_NOTES_CHIP_SELECTED -> getLastYearNotes()
            FOR_DATE_PERIOD_NOTES_CHIP_SELECTED -> getUserNotesInDatePeriodPagedList(
                fromDate,
                toDate,
                false
            )
            else -> {}
        }
    }

    private fun getAllNotes() {
        setSelectPeriodChipText(selectPeriodDefaultText)
        getUserNotesPagedList(false)
    }

    private fun getTodayNotes() {
        setSelectPeriodChipText(selectPeriodDefaultText)
        val today = Date(
            calendar.get(Calendar.YEAR) - 1900,
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        getUserNotesFromDatePagedList(today, false)
    }

    private fun getLastWeekNotes() {
        setSelectPeriodChipText(selectPeriodDefaultText)
        val dateWeekAgoMilliseconds = Date(calendar.timeInMillis - (6*24*60*60*1000))                   //учитываем текущий день как полный
        val dateWeekAgo = Date(dateWeekAgoMilliseconds.year,
            dateWeekAgoMilliseconds.month,
            dateWeekAgoMilliseconds.date
        )
        getUserNotesFromDatePagedList(dateWeekAgo, false)
    }

    private fun getLastMonthNotes() {
        setSelectPeriodChipText(selectPeriodDefaultText)
        val dateMonthAgo = getMonthAgoDate(calendar.get(Calendar.YEAR) - 1900,
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        getUserNotesFromDatePagedList(dateMonthAgo, false)
    }

    private fun getLastYearNotes() {
        setSelectPeriodChipText(selectPeriodDefaultText)
        val daysInYear: Long = if ((calendar.get(Calendar.YEAR)) % 4 == 0) 366
        else 365
        val dateYearAgoMilliseconds = Date(calendar.timeInMillis - ((daysInYear - 1)*24*60*60*1000))        //учитываем текущий день как полный
        val dateYearAgo = Date(dateYearAgoMilliseconds.year,
            dateYearAgoMilliseconds.month,
            dateYearAgoMilliseconds.date
        )
        getUserNotesFromDatePagedList(dateYearAgo, false)
    }

    fun getNotesInDatePeriod(fromDate: Date, toDate: Date) {
        val sdf = SimpleDateFormat("dd.MM.yyyy")
        setSelectPeriodChipText(sdf.format(fromDate) + "-" + sdf.format(toDate))
        getUserNotesInDatePeriodPagedList(fromDate, toDate, false)
    }

    fun setSelectPeriodChipDefaultText(text: String, isSelectPeriodChecked: Boolean) {
        selectPeriodDefaultText = text
        if (!isSelectPeriodChecked) setSelectPeriodChipText(selectPeriodDefaultText)
    }

    private fun setSelectPeriodChipText(text: String) {
        _selectPeriodChipTextLiveData.value = text
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

    private fun getUserNotesPagedList(remote: Boolean) {
        _notesPagedList = noteInteractor.getUserNotesPagedList(
            viewLifeCycleCompositeDisposable,
            noteMapper,
            DataSourceType.UserNotesDataSource,
            getCurrentUserId(sharedPreferences),
            remote
        )
        _notesState = noteInteractor.getNotesState(DataSourceType.UserNotesDataSource, false)
    }

    private fun getUserNotesFromDatePagedList(fromDate: Date, remote: Boolean) {
        _notesPagedList = noteInteractor.getDateNotesPagedList(
            viewLifeCycleCompositeDisposable,
            noteMapper,
            DataSourceType.DateNotesDataSource,
            getCurrentUserId(sharedPreferences),
            fromDate,
            remote
        )
        _notesState = noteInteractor.getNotesState(DataSourceType.DateNotesDataSource, false)
    }

    private fun getUserNotesInDatePeriodPagedList(fromDate: Date, toDate: Date, remote: Boolean) {
        _notesPagedList = noteInteractor.getDateNotesPagedList(
            viewLifeCycleCompositeDisposable,
            noteMapper,
            DataSourceType.DateNotesDataSource,
            getCurrentUserId(sharedPreferences),
            fromDate,
            toDate,
            remote
        )
        _notesState = noteInteractor.getNotesState(DataSourceType.DateNotesDataSource, false)
    }

    private fun getCurrentUserId(sharedPreferences: SharedPreferences): Int {
        return sharedPreferences.getInt(CURRENT_USER_ID, 0)
    }
}