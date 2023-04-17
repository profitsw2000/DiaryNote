package diarynote.data.room.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CategoryModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val color: Int,
    val categoryName: String,
    val categoryImage: Int
)
