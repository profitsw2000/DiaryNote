package diarynote.template.utils

import diarynote.data.model.CategoryModel
import diarynote.data.model.NoteModel

interface OnCategoryItemClickListener {
    fun onItemClick(categoryModel: CategoryModel)
}