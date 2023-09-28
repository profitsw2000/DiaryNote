package diarynote.mainfragment.model

import diarynote.data.model.NoteModel

sealed class NotesState {
    data class Success(val noteModelList: List<NoteModel>) : NotesState()
    data class Error(val message: String) : NotesState()
    object Loading : NotesState()
}