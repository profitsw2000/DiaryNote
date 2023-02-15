package diarynote.data.model

import java.util.Date

data class NoteModel(
    val id: Int = 0,
    val category: String = "",
    val title: String = "",
    val text: String = "",
    val tags: List<String> = arrayListOf(),
    val image: String = "",
    val date: Date,
    val edited: Boolean = false,
    val editDate: Date,
    val userLogin: String = ""
)
