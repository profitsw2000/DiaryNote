package diarynote.data.room.related

import androidx.room.Embedded
import androidx.room.Relation
import diarynote.data.room.entity.CategoryEntity
import diarynote.data.room.entity.UserEntity

data class UserWithCategoriesAndNotes(
    @Embedded
    val user: UserEntity,
    @Relation(
        entity = CategoryEntity::class,
        parentColumn = "id",
        entityColumn = "user_id"
    )
    val category: CategoryWithNotes
)
