package diarynote.data.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val color: Int,
    val categoryName: String,
    val categoryImage: Int
)
