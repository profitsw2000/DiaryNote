package diarynote.createnotescreen.model

import diarynote.data.model.CategoryModel

sealed class CategoriesState{
    data class Success(val categoryModelList: List<CategoryModel>) : CategoriesState()
    data class Error(val message: String) : CategoriesState()
    object Loading : CategoriesState()
}
