package diarynote.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
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
    val categoryId: Int = 0,
    val userId: Int = 0
) : Parcelable
