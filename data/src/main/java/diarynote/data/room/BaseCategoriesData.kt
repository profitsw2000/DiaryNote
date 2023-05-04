package diarynote.data.room

import diarynote.core.*
import diarynote.data.room.entity.CategoryEntity

val baseCategoriesList = listOf(
    CategoryEntity(
        id = 0,
        color = R.color.yellow,
        categoryName = "Без категории",
        categoryImage = R.drawable.bottom_nav_categories_icon,
        0
    ),
    CategoryEntity(
        id = 0,
        color = diarynote.core.R.color.violet,
        categoryName = "Работа",
        categoryImage = R.drawable.work_icon_outline_24,
        0
    ),
    CategoryEntity(
        id = 0,
        color = diarynote.core.R.color.green,
        categoryName = "Техника",
        categoryImage = R.drawable.tech_icon_24,
        0
    ),
    CategoryEntity(
        id = 0,
        color = diarynote.core.R.color.teal,
        categoryName = "Авто",
        categoryImage = R.drawable.auto_icon_24,
        0
    ),
    CategoryEntity(
        id = 0,
        color = diarynote.core.R.color.red,
        categoryName = "Документы",
        categoryImage = R.drawable.docs_icon_24,
        0
    ),
    CategoryEntity(
        id = 0,
        color = diarynote.core.R.color.dark_blue,
        categoryName = "Android",
        categoryImage = R.drawable.android_icon_24,
        0
    )
)