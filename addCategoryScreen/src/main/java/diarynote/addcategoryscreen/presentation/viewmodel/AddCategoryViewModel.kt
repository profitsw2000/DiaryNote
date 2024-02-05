package diarynote.addcategoryscreen.presentation.viewmodel

import android.content.SharedPreferences
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import diarynote.core.viewmodel.CoreViewModel
import diarynote.data.domain.CURRENT_USER_ID
import diarynote.data.interactor.CategoryInteractor
import diarynote.data.mappers.CategoryMapper
import diarynote.data.model.CategoryModel
import diarynote.data.model.state.CategoriesState
import diarynote.data.model.state.CopyFileState
import diarynote.navigator.Navigator
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import java.io.File

class AddCategoryViewModel(
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
    }

/*    fun navigateUp() {
        navigator.navigateUp()
    }*/

}