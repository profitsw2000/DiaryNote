package diarynote.data.room.related

import androidx.room.Embedded
import androidx.room.Relation
import diarynote.data.room.entity.NoteEntity
import diarynote.data.room.entity.UserEntity

data class UserWithNotes (
    @Embedded
    val user: UserEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "id"
    )
    val notesList: List<NoteEntity>
)