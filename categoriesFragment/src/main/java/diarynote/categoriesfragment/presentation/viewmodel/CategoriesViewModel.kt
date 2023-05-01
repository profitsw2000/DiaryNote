package diarynote.categoriesfragment.presentation.viewmodel

import diarynote.core.viewmodel.CoreViewModel
import diarynote.data.interactor.CategoryInteractor

class CategoriesViewModel (
    private val categoryInteractor: CategoryInteractor
) : CoreViewModel() {

}