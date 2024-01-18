package diarynote.data.data.web

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import androidx.sqlite.db.SupportSQLiteQuery
import diarynote.data.domain.web.NoteRepositoryRemote
import diarynote.data.mappers.NoteMapper
import diarynote.data.model.NoteModel
import diarynote.data.model.state.NotesState
import diarynote.data.model.type.DataSourceType
import diarynote.data.room.database.AppDatabase
import diarynote.data.room.entity.NoteEntity
import diarynote.data.room.related.UserWithNotes
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import java.util.Date

class NoteRepositoryRemoteImpl() : NoteRepositoryRemote{

    override fun getAllNotes(): Single<List<NoteEntity>> {
        TODO("Not yet implemented")
    }

    override fun getNoteById(id: Int): Single<NoteEntity> {
        TODO("Not yet implemented")
    }

    override fun getAllUserNotes(id: Int): Single<UserWithNotes> {
        TODO("Not yet implemented")
    }

    override fun getUserNotesPagedList(
        compositeDisposable: CompositeDisposable,
        noteMapper: NoteMapper,
        dataSourceType: DataSourceType,
        userId: Int
    ): LiveData<PagedList<NoteModel>> {
        TODO("Not yet implemented")
    }

    override fun getUserNotesByCategory(userId: Int, categoryId: Int): Single<List<NoteEntity>> {
        TODO("Not yet implemented")
    }

    override fun getCategoryNotesPagedList(
        compositeDisposable: CompositeDisposable,
        noteMapper: NoteMapper,
        dataSourceType: DataSourceType,
        userId: Int,
        categoryId: Int
    ): LiveData<PagedList<NoteModel>> {
        TODO("Not yet implemented")
    }

    override fun getUserNotesFromDate(userId: Int, fromDate: Date): Single<List<NoteEntity>> {
        TODO("Not yet implemented")
    }

    override fun getDateNotesPagedList(
        compositeDisposable: CompositeDisposable,
        noteMapper: NoteMapper,
        dataSourceType: DataSourceType,
        userId: Int,
        fromDate: Date
    ): LiveData<PagedList<NoteModel>> {
        TODO("Not yet implemented")
    }

    override fun getDateNotesPagedList(
        compositeDisposable: CompositeDisposable,
        noteMapper: NoteMapper,
        dataSourceType: DataSourceType,
        userId: Int,
        fromDate: Date,
        toDate: Date
    ): LiveData<PagedList<NoteModel>> {
        TODO("Not yet implemented")
    }

    override fun getUserNotesInDatePeriod(
        userId: Int,
        fromDate: Date,
        toDate: Date
    ): Single<List<NoteEntity>> {
        TODO("Not yet implemented")
    }

    override fun addNote(noteEntity: NoteEntity): Completable {
        TODO("Not yet implemented")
    }

    override fun addNoteList(noteEntityList: List<NoteEntity>): Completable {
        TODO("Not yet implemented")
    }

    override fun searchUserNotesByString(query: SupportSQLiteQuery): Single<List<NoteEntity>> {
        TODO("Not yet implemented")
    }

    override fun getSearchNotesPagedList(
        compositeDisposable: CompositeDisposable,
        noteMapper: NoteMapper,
        dataSourceType: DataSourceType,
        userId: Int,
        searchString: String
    ): LiveData<PagedList<NoteModel>> {
        TODO("Not yet implemented")
    }

    override fun updateNote(noteEntity: NoteEntity): Completable {
        TODO("Not yet implemented")
    }

    override fun deleteNote(noteEntity: NoteEntity): Completable {
        TODO("Not yet implemented")
    }

    override fun getNotesState(): LiveData<NotesState> {
        TODO("Not yet implemented")
    }
}