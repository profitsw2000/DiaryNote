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

    fun getAllNotes(): Single<List<NoteEntity>>

    fun getNoteById(id: Int): Single<NoteEntity>

    fun getAllUserNotes(id: Int): Single<UserWithNotes>

    fun getUserNotesPagedList(
        compositeDisposable: CompositeDisposable,
        noteMapper: NoteMapper,
        dataSourceType: DataSourceType,
        userId: Int
    ): LiveData<PagedList<NoteModel>>

    fun getUserNotesByCategory(userId: Int, categoryId: Int): Single<List<NoteEntity>>

    fun getCategoryNotesPagedList(
        compositeDisposable: CompositeDisposable,
        noteMapper: NoteMapper,
        dataSourceType: DataSourceType,
        userId: Int,
        categoryId: Int
    ): LiveData<PagedList<NoteModel>>

    fun getUserNotesFromDate(userId: Int, fromDate: Date): Single<List<NoteEntity>>

    fun getDateNotesPagedList(
        compositeDisposable: CompositeDisposable,
        noteMapper: NoteMapper,
        dataSourceType: DataSourceType,
        userId: Int,
        fromDate: Date
    ): LiveData<PagedList<NoteModel>>

    fun getUserNotesInDatePeriod(userId: Int, fromDate: Date, toDate: Date): Single<List<NoteEntity>>

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

    fun searchUserNotesByString(query: SupportSQLiteQuery): Single<List<NoteEntity>>

    fun getSearchNotesPagedList(
        compositeDisposable: CompositeDisposable,
        noteMapper: NoteMapper,
        dataSourceType: DataSourceType,
        userId: Int,
        searchString: String
    ): LiveData<PagedList<NoteModel>>

    fun updateNote(noteEntity: NoteEntity): Completable

    fun deleteNote(noteEntity: NoteEntity): Completable

}