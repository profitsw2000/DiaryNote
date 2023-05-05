package diarynote.categoriesfragment.presentation.viewmodel

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import diarynote.categoriesfragment.model.CategoriesState
import diarynote.core.viewmodel.CoreViewModel
import diarynote.data.domain.CURRENT_USER_ID
import diarynote.data.interactor.CategoryInteractor
import diarynote.data.mappers.CategoryMapper
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class CategoriesViewModel (
    private val categoryInteractor: CategoryInteractor,
    private val sharedPreferences: SharedPreferences,
    private val categoryMapper: CategoryMapper
) : CoreViewModel() {

    private val _categoriesLiveData = MutableLiveData<CategoriesState>()
    val categoriesLiveData: LiveData<CategoriesState> by this::_categoriesLiveData

    fun getCategoriesList() {
        getAllUserCategories(sharedPreferences.getInt(CURRENT_USER_ID,0))
        //getAllCategories()
    }

    private fun getAllUserCategories(userId: Int) {
        _categoriesLiveData.value = CategoriesState.Loading
        categoryInteractor.getAllUserCategories(userId, false)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    _categoriesLiveData.value = CategoriesState.Success(
                        categoryMapper.map(it.categoryList)
                    )
                }, {
                    val errorMessage = it.message ?: ""
                    _categoriesLiveData.value = CategoriesState.Error(errorMessage)
                }
            )
    }

    private fun getAllCategories() {
        _categoriesLiveData.value = CategoriesState.Loading
        categoryInteractor.getAllCategories(false)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    _categoriesLiveData.value = CategoriesState.Success(
                        categoryMapper.map(it)
                    )
                }, {
                    val errorMessage = it.message ?: ""
                    _categoriesLiveData.value = CategoriesState.Error(errorMessage)
                }
            )
    }
}