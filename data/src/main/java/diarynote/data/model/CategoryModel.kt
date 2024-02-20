package diarynote.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CategoryModel(
    val id: Int = 0,
    val color: Int = 0,
    val categoryName: String = "",
    val categoryImage: Int = 0,
    val imagePath: String = "",
    val userId: Int = 0
) : Parcelable
