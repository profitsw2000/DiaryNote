package diarynote.data.room

import diarynote.data.R
import diarynote.data.room.entity.CategoryEntity

val baseCategoriesList = listOf(
    CategoryEntity(
        id = 1,
        color = diarynote.core.R.color.yellow,
        categoryName = "Без категории",
        categoryImage = diarynote.core.R.drawable.bottom_nav_categories_icon,
        0
    ),
    CategoryEntity(
        id = 1,
        color = diarynote.core.R.color.violet,
        categoryName = "Работа",
        categoryImage = diarynote.core.R.drawable.work_icon_outline_24,
        0
    ),
    CategoryEntity(
        id = 1,
        color = diarynote.core.R.color.green,
        categoryName = "Техника",
        categoryImage = diarynote.core.R.drawable.tech_icon_24,
        0
    ),
    CategoryEntity(
        id = 1,
        color = diarynote.core.R.color.teal,
        categoryName = "Авто",
        categoryImage = diarynote.core.R.drawable.auto_icon_24,
        0
    ),
    CategoryEntity(
        id = 1,
        color = diarynote.core.R.color.red,
        categoryName = "Документы",
        categoryImage = diarynote.core.R.drawable.docs_icon_24,
        0
    ),
    CategoryEntity(
        id = 1,
        color = diarynote.core.R.color.dark_blue,
        categoryName = "Android",
        categoryImage = diarynote.core.R.drawable.android_icon_24,
        0
    ),
)