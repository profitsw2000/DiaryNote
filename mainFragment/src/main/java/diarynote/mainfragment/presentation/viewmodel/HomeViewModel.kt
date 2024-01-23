package diarynote.mainfragment.presentation.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import androidx.sqlite.db.SimpleSQLiteQuery
import diarynote.core.utils.SearchQueryBuilder
import diarynote.core.viewmodel.CoreViewModel
import diarynote.data.domain.CURRENT_USER_ID
import diarynote.data.interactor.NoteInteractor
import diarynote.data.mappers.NoteMapper
import diarynote.data.model.NoteModel
import diarynote.data.model.state.NotesCountChangeState
import diarynote.data.model.state.NotesState
import diarynote.data.model.type.DataSourceType
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class HomeViewModel(
    private val noteInteractor: NoteInteractor,
    private val sharedPreferences: SharedPreferences,
    private val noteMapper: NoteMapper
) : CoreViewModel() {

    private var notesCount: Int = 0

/*    private val _notesLiveData = MutableLiveData<NotesState>()
    val notesLiveData: LiveData<NotesState> by this::_notesLiveData*/

    private lateinit var _notesPagedList: LiveData<PagedList<NoteModel>>
    val notesPagedList: LiveData<PagedList<NoteModel>> by this::_notesPagedList

    private val _userNotesCountChanged = MutableLiveData<NotesCountChangeState>()
    val userNotesCountChanged: LiveData<NotesCountChangeState> by this::_userNotesCountChanged

    private lateinit var _notesState: LiveData<NotesState>
    val notesState: LiveData<NotesState> by this::_notesState

    init {
        _notesPagedList = MutableLiveData<PagedList<NoteModel>>()
        _notesState = MutableLiveData<NotesState>()
    }

/*    fun getNotesList() {
        getAllUserNotes(sharedPreferences.getInt(CURRENT_USER_ID, 0))
    }*/

    fun checkUserNotesCountChanged() {

        _userNotesCountChanged.value = NotesCountChangeState.Loading
        noteInteractor.getUserNotesCount(getCurrentUserId(sharedPreferences), false)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    if (it != this.notesCount) {
                        getUserNotesPagedList()
                    }
                    _userNotesCountChanged.value = NotesCountChangeState.Success(it != this.notesCount)
                    this.notesCount = it
                },{
                    _userNotesCountChanged.value = NotesCountChangeState.Error(it.message!!)
                }
            )
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

/*    private fun getAllUserNotes(userId: Int) {
        _notesLiveData.value = NotesState.Loading
        noteInteractor.getAllUserNotes(userId, false)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    _notesLiveData.value = NotesState.Success(
                        noteMapper.map(it.notesList)
                    )
                },{
                    val errorMessage = it.message ?: ""
                    //_notesLiveData.value = NotesState.Error(errorMessage)
                }
            )
    }

    fun getUserNotesByString(search: String) {
        _notesLiveData.value = NotesState.Loading
        noteInteractor.getUserNotesByString(
            getSearchQueryPair(search),
            false
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    _notesLiveData.value = NotesState.Success(
                        noteMapper.map(it)
                    )
                },
                {
                    val errorMessage = it.message ?: ""
                    //_notesLiveData.value = NotesState.Error(errorMessage)
                }
            )
    }*/

    fun getSearchNotesPagedList(search: String) {
        _notesPagedList = noteInteractor.getSearchNotesPagedList(
            viewLifeCycleCompositeDisposable,
            noteMapper,
            DataSourceType.SearchNotesDataSource,
            getCurrentUserId(sharedPreferences),
            search,
            false
        )
        _notesState = noteInteractor.getNotesState(DataSourceType.SearchNotesDataSource, false)
    }

/*    private fun getSearchQueryPair(search: String) : SimpleSQLiteQuery {

        val searchQueryBuilder = SearchQueryBuilder(
            search,
            sharedPreferences.getInt(CURRENT_USER_ID, 0),
            0,
            0)

        val queryString: String = searchQueryBuilder.getSearchQueryPair().first
        val queryArgs: Array<Any> = searchQueryBuilder.getSearchQueryPair().second.toTypedArray()

        return SimpleSQLiteQuery(queryString, queryArgs)
    }*/

    private fun getCurrentUserId(sharedPreferences: SharedPreferences): Int {
        return sharedPreferences.getInt(CURRENT_USER_ID, 0)
    }
}