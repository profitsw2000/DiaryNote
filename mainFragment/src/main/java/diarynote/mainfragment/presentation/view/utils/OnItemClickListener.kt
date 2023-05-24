package diarynote.mainfragment.presentation.view.utils

import diarynote.data.model.NoteModel

interface OnItemClickListener {
    fun onItemClick(noteModel: NoteModel)
}