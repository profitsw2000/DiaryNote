package diarynote.createnotescreen.presentation.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import diarynote.core.utils.*
import diarynote.core.viewmodel.CoreViewModel
import diarynote.createnotescreen.model.CategoriesState
import diarynote.createnotescreen.model.NotesState
import diarynote.data.domain.CURRENT_USER_ID
import diarynote.data.interactor.CategoryInteractor
import diarynote.data.interactor.NoteInteractor
import diarynote.data.mappers.CategoryMapper
import diarynote.data.mappers.NoteMapper
import diarynote.data.model.NoteModel
import diarynote.navigator.Navigator
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.Calendar

class CreateNoteViewModel(
    private val categoryInteractor: CategoryInteractor,
    private val noteInteractor: NoteInteractor,
    private val sharedPreferences: SharedPreferences,
    private val categoryMapper: CategoryMapper,
    private val noteMapper: NoteMapper,
    private val navigator: Navigator
) : CoreViewModel() {

    private val inputValidator = InputValidator()

    private val _categoriesLiveData = MutableLiveData<CategoriesState>()
    val categoriesLiveData: LiveData<CategoriesState> by this::_categoriesLiveData

    private val _notesLiveData = MutableLiveData<NotesState?>()
    val notesLiveData: LiveData<NotesState?> by this::_notesLiveData

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
    }

    fun getNotesData(noteTitle: String,
                    noteContent: String,
                     noteTags: String,
                     category: String) {
        val noteTitleIsValid = inputValidator.checkInputIsValid(noteTitle, NOTE_TITLE_MIN_LENGTH)
        val noteContentIsValid = inputValidator.checkInputIsValid(noteContent, NOTE_CONTENT_MIN_LENGTH)
        val noteTagsLengthIsValid = inputValidator.checkInputIsValid(noteTags, NOTE_TAGS_MIN_LENGTH)
        val noteTagsIsValid = checkNoteTags(noteTags)

        if (noteTitleIsValid &&
                noteContentIsValid &&
                noteTagsLengthIsValid &&
                noteTagsIsValid) {
            addNote(NoteModel(
                id = 0,
                category = category,
                title = noteTitle,
                text = noteContent,
                tags = getNoteTagsList(noteTags),
                image = "",
                date = Calendar.getInstance().time,
                edited = false,
                editDate = Calendar.getInstance().time,
                userId = sharedPreferences.getInt(CURRENT_USER_ID,0)
            ))
        } else {
            invalidInput(!noteTitleIsValid,
                !noteContentIsValid,
                !noteTagsLengthIsValid,
                !noteTagsIsValid
            )
        }
    }

    private fun getNoteTagsList(tags: String): List<String> {
        var tagsList = tags.split(",").toList()
        val newTagsList = mutableListOf<String>()

        tagsList.forEach {
            newTagsList.add(it.trimStart())
        }
        return newTagsList
    }

    private fun invalidInput(noteTitleIsValid: Boolean, noteContentIsValid: Boolean, noteTagsLengthIsValid: Boolean, noteTagsIsValid: Boolean) {
        val errorCode = (noteTitleIsValid.toInt() shl NOTE_TITLE_BIT_NUMBER) or
                (noteContentIsValid.toInt() shl NOTE_CONTENT_BIT_NUMBER) or
                (noteTagsLengthIsValid.toInt() shl NOTE_TAGS_BIT_NUMBER) or
                (noteTagsIsValid.toInt() shl NOTE_TAG_WORDS_BIT_NUMBER)
        _notesLiveData.value = NotesState.Error("", errorCode)
    }

    private fun checkNoteTags(noteTags: String) : Boolean {
        val tagsList: List<String> = noteTags.split(",").toList()
        if (tagsList.size >= 10) return false
        tagsList.forEach {
            val wordList = it.split(" ").toList()
            if (wordList.size > NOTE_TAG_WORDS_LIMIT) return false
        }
        return true
    }

    private fun addNote(noteModel: NoteModel) {
        _notesLiveData.value = NotesState.Loading
        noteInteractor.addNote(noteMapper.map(noteModel), false)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    _notesLiveData.value = NotesState.Success(arrayListOf())
                },{
                    _notesLiveData.value = NotesState.Error(it.message!!, (1 shl ROOM_BIT_NUMBER))
                }
            )
    }

    fun navigateUp() {
        navigator.navigateUp()
    }

    fun clear() {
        _notesLiveData.value = null
    }

    private fun Boolean.toInt() = if (this) 1 else 0
}