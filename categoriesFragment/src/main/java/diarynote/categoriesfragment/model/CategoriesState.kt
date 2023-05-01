package diarynote.categoriesfragment.model

import diarynote.data.model.CategoryModel

sealed class CategoriesState{
    data class Success(val categoryModel: CategoryModel) : CategoriesState()
    data class Error(val message: String) : CategoriesState()
    object Loading : CategoriesState()
}
