package diarynote.data.model.state

import diarynote.data.model.NoteModel

sealed class NotesCountChangeState {
    data class Success(val notesCountChanged: Boolean) : NotesCountChangeState()
    data class Error(val message: String) : NotesCountChangeState()
    object Loading : NotesCountChangeState()
}
