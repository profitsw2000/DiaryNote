package diarynote.data.room.entity

import androidx.room.*
import androidx.room.ForeignKey.CASCADE
import java.util.*

@Entity(foreignKeys = [ForeignKey(
    entity = UserEntity::class,
    parentColumns = arrayOf("id"),
    childColumns = arrayOf("user_id"),
    onDelete = CASCADE)],
    indices = [Index("user_id")])
data class NoteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val category: String,
    val title: String,
    val text: String,
    val tags: List<String>,
    val image: String,
    val date: Date,
    val edited: Boolean,
    val editDate: Date,
    @ColumnInfo(name = "user_id")
    val userId: Int
)
