package diarynote.data.room.related

import androidx.room.Embedded
import androidx.room.Relation
import diarynote.data.room.entity.CategoryEntity
import diarynote.data.room.entity.UserEntity

data class UserWithCategories(
    @Embedded
    val user: UserEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "user_id"
    )
    val categoryList: List<CategoryEntity>
)
