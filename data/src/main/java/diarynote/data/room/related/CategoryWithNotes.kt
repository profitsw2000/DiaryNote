package diarynote.data.room.related

import androidx.room.Embedded
import androidx.room.Relation
import diarynote.data.room.entity.CategoryEntity
import diarynote.data.room.entity.NoteEntity

data class CategoryWithNotes(
    @Embedded
    val category: CategoryEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "category_id"
    )
    val notes: List<NoteEntity>
)
