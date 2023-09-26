package diarynote.mainfragment.presentation.viewmodel

import android.content.SharedPreferences
import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.sqlite.db.SimpleSQLiteQuery
import diarynote.core.viewmodel.CoreViewModel
import diarynote.data.domain.CURRENT_USER_ID
import diarynote.data.interactor.NoteInteractor
import diarynote.data.mappers.NoteMapper
import diarynote.navigator.Navigator
import diarynote.template.model.NotesState
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class HomeViewModel(
    private val noteInteractor: NoteInteractor,
    private val sharedPreferences: SharedPreferences,
    private val noteMapper: NoteMapper
) : CoreViewModel() {

    private val _notesLiveData = MutableLiveData<NotesState>()
    val notesLiveData: LiveData<NotesState> by this::_notesLiveData

    fun getNotesList() {
        getAllUserNotes(sharedPreferences.getInt(CURRENT_USER_ID, 0))
    }

    private fun getAllUserNotes(userId: Int) {
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
                    _notesLiveData.value = NotesState.Error(errorMessage)
                }
            )
    }

    fun getUserNotesWithWordInTags(search: String) {
        _notesLiveData.value = NotesState.Loading
        noteInteractor.getUserNotesWithWordInTags(
            sharedPreferences.getInt(CURRENT_USER_ID, 0),
            search,
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
                    _notesLiveData.value = NotesState.Error(errorMessage)
                }
            )
    }

    fun getUserNotesWithWordInText(search: String) {
        _notesLiveData.value = NotesState.Loading
        noteInteractor.getUserNotesWithWordInText(
            sharedPreferences.getInt(CURRENT_USER_ID, 0),
            search,
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
                    _notesLiveData.value = NotesState.Error(errorMessage)
                }
            )
    }

    fun getUserNotesByWord(search: String) {
        _notesLiveData.value = NotesState.Loading
        noteInteractor.getUserNotesByWord(
            sharedPreferences.getInt(CURRENT_USER_ID, 0),
            search,
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
                    _notesLiveData.value = NotesState.Error(errorMessage)
                }
            )
    }

    private fun getQueryWordsList(searchQuery: String): List<String> {
        val wordsList: MutableList<String> = mutableListOf()
        if (searchQuery.trim().split(" ").toList().size < 2) {
            wordsList.addAll(searchQuery.trim().split(" ").toList())
        }
        return wordsList
    }

    private fun getQueryString(searchQuery: String): String {
        val mainPartQuery = "(SELECT *, 0 " +
                "AS PRIORITY " +
                "FROM NoteEntity " +
                "WHERE NoteEntity.user_id LIKE :userId" +
                " AND NoteEntity.tags LIKE '%${searchQuery.trim()}%' " +
                "UNION " +
                "SELECT *, 1 " +
                "AS PRIORITY " +
                "FROM NoteEntity " +
                "WHERE NoteEntity.user_id LIKE :userId " +
                "AND NoteEntity.title LIKE '%${searchQuery.trim()}%' " +
                "UNION " +
                "SELECT *, 2 " +
                "AS PRIORITY " +
                "FROM NoteEntity " +
                "WHERE NoteEntity.user_id LIKE :userId " +
                "AND NoteEntity.text LIKE '%${searchQuery.trim()}%' " +
                "ORDER BY PRIORITY)"

        return if (getQueryWordsList(searchQuery).size > 1) {
            ""
        } else {
            ""
        }
    }

    private fun getParticularWordsQuery(searchQuery: String) {
        val query: SimpleSQLiteQuery = SimpleSQLiteQuery(searchQuery)
        if (getQueryWordsList(searchQuery).size > 1) {
            getQueryWordsList(searchQuery).forEach {
                "SELECT *, 0 " +
                "AS PRIORITY " +
                "FROM NoteEntity " +
                "WHERE NoteEntity.user_id LIKE :userId" +
                " AND NoteEntity.tags LIKE '%${searchQuery.trim()}%' " +
                "UNION " +
                "SELECT *, 1 " +
                "AS PRIORITY " +
                "FROM NoteEntity " +
                "WHERE NoteEntity.user_id LIKE :userId " +
                "AND NoteEntity.title LIKE '%${searchQuery.trim()}%' " +
                "UNION " +
                "SELECT *, 2 " +
                "AS PRIORITY " +
                "FROM NoteEntity " +
                "WHERE NoteEntity.user_id LIKE :userId " +
                "AND NoteEntity.text LIKE '%${searchQuery.trim()}%' "
            }
        } else {
            getFullStringQuery(searchQuery)
        }
    }

    private fun getFullStringQuery(searchQuery: String) : String {
        return "SELECT *, 0 " +
                "AS PRIORITY " +
                "FROM NoteEntity " +
                "WHERE NoteEntity.user_id LIKE ?" +
                " AND NoteEntity.tags LIKE '%?%' " +
                "UNION " +
                "SELECT *, 1 " +
                "AS PRIORITY " +
                "FROM NoteEntity " +
                "WHERE NoteEntity.user_id LIKE ? " +
                "AND NoteEntity.title LIKE '%?%' " +
                "UNION " +
                "SELECT *, 2 " +
                "AS PRIORITY " +
                "FROM NoteEntity " +
                "WHERE NoteEntity.user_id LIKE ? " +
                "AND NoteEntity.text LIKE '%?%'"
    }
}