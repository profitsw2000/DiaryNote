package ru.profitsw2000.editcategoryscreen.presentation.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import diarynote.core.utils.CATEGORY_NAME_LENGTH_ERROR
import diarynote.core.viewmodel.CoreViewModel
import diarynote.data.domain.CURRENT_USER_ID
import diarynote.data.interactor.CategoryInteractor
import diarynote.data.mappers.CategoryMapper
import diarynote.data.model.CategoryModel
import diarynote.data.model.state.CategoriesState
import diarynote.data.model.state.CopyFileState
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import java.io.File

class EditCategoryViewModel(
    private val categoryInteractor: CategoryInteractor,
    private val categoryMapper: CategoryMapper,
    private val sharedPreferences: SharedPreferences
) : CoreViewModel() {
    var selectedColorPosition = 0
    var selectedIconPosition = 0

    private val _categoryLiveData = MutableLiveData<CategoriesState?>()
    val categoryLiveData: LiveData<CategoriesState?> by this::_categoryLiveData

    private val _copyFileLiveData = MutableLiveData<CopyFileState?>()
    val copyFileLiveData: LiveData<CopyFileState?> by this::_copyFileLiveData

    fun editCategory(categoryModel: CategoryModel) {
        if (categoryModel.categoryName.length < 2) {
            _categoryLiveData.value = CategoriesState.Error(CATEGORY_NAME_LENGTH_ERROR)
        } else {
            updateCategory(categoryModel)
        }
    }

    private fun updateCategory(categoryModel: CategoryModel) {
        _categoryLiveData.value = CategoriesState.Loading
        categoryInteractor.updateCategory(categoryMapper.map(categoryModel.copy(userId = sharedPreferences.getInt(
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
            .addViewLifeCycle()
    }

    fun copyFile(sourcePath: String, targetPath: String) {
        val sourceFile = File(sourcePath)
        val targetFile = File(targetPath)

        _copyFileLiveData.value = CopyFileState.Copying
        try {
            sourceFile.copyTo(targetFile, true)
            _copyFileLiveData.value = CopyFileState.Success(targetPath)
        } catch (exception: Exception) {
            _copyFileLiveData.value = CopyFileState.Error(exception.message.toString())
        }
    }

    fun clear() {
        _categoryLiveData.value = null
        _copyFileLiveData.value = null
    }
}