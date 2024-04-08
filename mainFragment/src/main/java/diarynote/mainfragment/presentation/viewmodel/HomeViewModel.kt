package diarynote.mainfragment.presentation.viewmodel

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import diarynote.core.viewmodel.CoreViewModel
import diarynote.data.appsettings.TAGS_SEARCH_PRIORITY_KEY
import diarynote.data.appsettings.TEXT_SEARCH_PRIORITY_KEY
import diarynote.data.appsettings.TITLE_SEARCH_PRIORITY_KEY
import diarynote.data.domain.CURRENT_USER_ID
import diarynote.data.interactor.NoteInteractor
import diarynote.data.mappers.NoteMapper
import diarynote.data.model.NoteModel
import diarynote.data.model.state.NotesCountChangeState
import diarynote.data.model.state.NotesState
import diarynote.data.model.type.DataSourceType
import diarynote.data.room.entity.NoteEntity
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class HomeViewModel(
    private val noteInteractor: NoteInteractor,
    private val sharedPreferences: SharedPreferences,
    private val noteMapper: NoteMapper
) : CoreViewModel() {

    val allNotesList: LiveData<List<NoteEntity>> = noteInteractor.getAll(false)

    private lateinit var _notesPagedList: LiveData<PagedList<NoteModel>>
    val notesPagedList: LiveData<PagedList<NoteModel>> by this::_notesPagedList

    private lateinit var _notesState: LiveData<NotesState>
    val notesState: LiveData<NotesState> by this::_notesState

    init {
        getUserNotesPagedList()
        allNotesList.observeForever {
            getUserNotesPagedList()
        }
    }

    fun getUserNotesPagedList() {
        _notesPagedList = noteInteractor.getUserNotesPagedList(
            viewLifeCycleCompositeDisposable,
            noteMapper,
            DataSourceType.UserNotesDataSource,
            getCurrentUserId(sharedPreferences),
            false
        )
        _notesState = noteInteractor.getNotesState(DataSourceType.UserNotesDataSource, false)
    }

    fun getSearchNotesPagedList(search: String) {
        _notesPagedList = noteInteractor.getSearchNotesPagedList(
            viewLifeCycleCompositeDisposable,
            noteMapper,
            DataSourceType.SearchNotesDataSource,
            getCurrentUserId(sharedPreferences),
            search,
            getSearchPriorityNumbersList(),
            false
        )
        _notesState = noteInteractor.getNotesState(DataSourceType.SearchNotesDataSource, false)
    }

    private fun getSearchPriorityNumbersList() : List<Int> {
        val titleSearchPriority = sharedPreferences.getInt(TITLE_SEARCH_PRIORITY_KEY, 1)
        val textSearchPriority = sharedPreferences.getInt(TEXT_SEARCH_PRIORITY_KEY, 2)
        val tagsSearchPriority = sharedPreferences.getInt(TAGS_SEARCH_PRIORITY_KEY, 0)

        return if ((titleSearchPriority != textSearchPriority) &&
            (titleSearchPriority != tagsSearchPriority) &&
            (textSearchPriority != titleSearchPriority)) {
            arrayListOf(titleSearchPriority, textSearchPriority, tagsSearchPriority)
        } else {
            arrayListOf(1, 2, 0)
        }
    }

    private fun getCurrentUserId(sharedPreferences: SharedPreferences): Int {
        return sharedPreferences.getInt(CURRENT_USER_ID, 0)
    }
}