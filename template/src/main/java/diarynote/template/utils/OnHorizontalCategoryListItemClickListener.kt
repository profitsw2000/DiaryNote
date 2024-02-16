package diarynote.template.utils

import diarynote.data.model.CategoryModel

interface OnHorizontalCategoryListItemClickListener: OnItemClickListener {
    fun onItemClick(position: Int)
}