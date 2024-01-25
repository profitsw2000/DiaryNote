package diarynote.categoriesfragment.presentation.viewmodel

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import diarynote.categoriesfragment.model.CategoriesState
import diarynote.core.viewmodel.CoreViewModel
import diarynote.data.domain.CURRENT_USER_ID
import diarynote.data.interactor.CategoryInteractor
import diarynote.data.interactor.NoteInteractor
import diarynote.data.mappers.CategoryMapper
import diarynote.data.mappers.NoteMapper
import diarynote.data.model.NoteModel
import diarynote.data.model.type.DataSourceType
import diarynote.template.model.NotesState
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class CategoriesViewModel (
    private val categoryInteractor: CategoryInteractor,
    private val noteInteractor: NoteInteractor,
    private val sharedPreferences: SharedPreferences,
    private val categoryMapper: CategoryMapper,
    private val noteMapper: NoteMapper
) : CoreViewModel() {

    private val _categoriesLiveData = MutableLiveData<CategoriesState>()
    val categoriesLiveData: LiveData<CategoriesState> by this::_categoriesLiveData

    private lateinit var _notesPagedList: LiveData<PagedList<NoteModel>>
    val notesPagedList: LiveData<PagedList<NoteModel>> by this::_notesPagedList

    private lateinit var _notesState: LiveData<diarynote.data.model.state.NotesState>
    val notesState: LiveData<diarynote.data.model.state.NotesState> by this::_notesState

    fun getCategoriesList() {
        getAllUserCategories(sharedPreferences.getInt(CURRENT_USER_ID,0))
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
            .addViewLifeCycle()
    }

    fun getCategoryNotesPagedList(categoryId: Int) {
        _notesPagedList = noteInteractor.getCategoryNotesPagedList(
            viewLifeCycleCompositeDisposable,
            noteMapper,
            DataSourceType.CategoryNotesDataSource,
            getCurrentUserId(sharedPreferences),
            categoryId,
            false
        )
        _notesState = noteInteractor.getNotesState(DataSourceType.CategoryNotesDataSource, false)
    }

    private fun getCurrentUserId(sharedPreferences: SharedPreferences): Int {
        return sharedPreferences.getInt(CURRENT_USER_ID, 0)
    }
}