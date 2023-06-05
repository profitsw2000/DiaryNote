package diarynote.addcategoryscreen.presentation.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import diarynote.core.viewmodel.CoreViewModel
import diarynote.data.domain.CURRENT_USER_ID
import diarynote.data.interactor.CategoryInteractor
import diarynote.data.mappers.CategoryMapper
import diarynote.data.model.CategoryModel
import diarynote.data.model.state.CategoriesState
import diarynote.navigator.Navigator
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class AddCategoryViewModel(
    private val categoryInteractor: CategoryInteractor,
    private val categoryMapper: CategoryMapper,
    private val sharedPreferences: SharedPreferences,
    private val navigator: Navigator
) : CoreViewModel() {

    private val _categoryLiveData = MutableLiveData<CategoriesState?>()
    val categoryLiveData: LiveData<CategoriesState?> by this::_categoryLiveData

    fun addCategory(categoryModel: CategoryModel) {
        if (categoryModel.categoryName.length < 2) {
            _categoryLiveData.value = CategoriesState.Error("Название категории не менее 2 символов")
        } else {
            insertCategory(categoryModel)
        }
    }

    private fun insertCategory(categoryModel: CategoryModel) {
        _categoryLiveData.value = CategoriesState.Loading
        categoryInteractor.addCategory(categoryMapper.map(categoryModel.copy(userId = sharedPreferences.getInt(
            CURRENT_USER_ID, 0))), false)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    _categoryLiveData.value = CategoriesState.Success(arrayListOf())
                },{
                    val message = it.message ?: ""
                    _categoryLiveData.value = CategoriesState.Error(message)
                }
            )
    }

    fun clear() {
        _categoryLiveData.value = null
    }

    fun navigateUp() {
        navigator.navigateUp()
    }

}