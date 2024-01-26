package diarynote.data.domain

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import androidx.sqlite.db.SupportSQLiteQuery
import diarynote.data.mappers.NoteMapper
import diarynote.data.model.NoteModel
import diarynote.data.model.state.NotesState
import diarynote.data.model.type.DataSourceType
import diarynote.data.room.database.AppDatabase
import diarynote.data.room.entity.NoteEntity
import diarynote.data.room.related.UserWithCategoriesAndNotes
import diarynote.data.room.related.UserWithNotes
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import java.util.Date

interface NoteRepository {

    fun getNoteById(id: Int): Single<NoteEntity>

    fun getUserNotesCount(id: Int): Single<Int>

    fun getUserNotesPagedList(
        compositeDisposable: CompositeDisposable,
        noteMapper: NoteMapper,
        dataSourceType: DataSourceType,
        userId: Int
    ): LiveData<PagedList<NoteModel>>

    fun getCategoryNotesPagedList(
        compositeDisposable: CompositeDisposable,
        noteMapper: NoteMapper,
        dataSourceType: DataSourceType,
        userId: Int,
        categoryId: Int
    ): LiveData<PagedList<NoteModel>>

    fun getDateNotesPagedList(
        compositeDisposable: CompositeDisposable,
        noteMapper: NoteMapper,
        dataSourceType: DataSourceType,
        userId: Int,
        fromDate: Date
    ): LiveData<PagedList<NoteModel>>

    fun getDateNotesPagedList(
        compositeDisposable: CompositeDisposable,
        noteMapper: NoteMapper,
        dataSourceType: DataSourceType,
        userId: Int,
        fromDate: Date,
        toDate: Date
    ): LiveData<PagedList<NoteModel>>

    fun addNote(noteEntity: NoteEntity): Completable

    fun addNoteList(noteEntityList: List<NoteEntity>): Completable

    fun getSearchNotesPagedList(
        compositeDisposable: CompositeDisposable,
        noteMapper: NoteMapper,
        dataSourceType: DataSourceType,
        userId: Int,
        searchString: String
    ): LiveData<PagedList<NoteModel>>

    fun updateNote(noteEntity: NoteEntity): Completable

    fun deleteNote(noteEntity: NoteEntity): Completable

    fun getNotesState(dataSourceType: DataSourceType): LiveData<NotesState>

}