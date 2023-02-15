package diarynote.data.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class NoteEntity(
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
