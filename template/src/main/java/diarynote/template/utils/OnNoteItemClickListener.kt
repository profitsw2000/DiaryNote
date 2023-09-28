package diarynote.template.utils

import diarynote.data.model.NoteModel

interface OnNoteItemClickListener : OnItemClickListener {
    fun onItemClick(noteModel: NoteModel)

}