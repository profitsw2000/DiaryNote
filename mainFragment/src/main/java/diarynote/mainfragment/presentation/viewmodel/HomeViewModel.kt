package diarynote.mainfragment.presentation.viewmodel

import android.content.SharedPreferences
import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
    private val navigator: Navigator,
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

    fun navigateToNoteCreation() {
        navigator.navigateToNoteCreation()
    }

    fun navigateToNoteDetails(bundle: Bundle) {
        navigator.navigateToNoteRead(bundle)
    }

}