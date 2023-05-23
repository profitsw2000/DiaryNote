package diarynote.readnotescreen.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import diarynote.core.viewmodel.CoreViewModel
import diarynote.data.interactor.NoteInteractor
import diarynote.data.mappers.NoteMapper
import diarynote.data.model.state.NotesState
import diarynote.navigator.Navigator
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class ReadNoteViewModel(
    private val noteInteractor: NoteInteractor,
    private val noteMapper: NoteMapper,
    private val navigator: Navigator
) : CoreViewModel() {

    private val _notesLiveData = MutableLiveData<NotesState>()
    val notesLiveData: LiveData<NotesState> by this::_notesLiveData

    fun getNoteDetails(noteId: Int) {
        _notesLiveData.value = NotesState.Loading

        noteInteractor.getNoteById(noteId, false)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    _notesLiveData.value = NotesState.Success(
                        arrayListOf(noteMapper.map(it))
                    )
                }, {
                    val errorMessage = it.message ?: ""
                    _notesLiveData.value = NotesState.Error(errorMessage, 0)
                }
            )
    }

}