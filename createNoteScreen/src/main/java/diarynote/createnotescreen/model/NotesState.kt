package diarynote.createnotescreen.model

import diarynote.data.model.NoteModel

sealed class NotesState {
    data class Success(val noteModelList: List<NoteModel>) : NotesState()
    data class Error(val message: String, val errorCode: Int) : NotesState()
    object Loading : NotesState()
}
