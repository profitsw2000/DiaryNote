package diarynote.data.room.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class NoteModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val category: String,
    val title: String,
    val text: String,
    //val tags: List<String>,
    val image: String,
    //val date: Date,
    val edited: Boolean,
    //val editDate: Date,
    val userLogin: String
)
