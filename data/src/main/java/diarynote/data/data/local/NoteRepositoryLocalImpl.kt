package diarynote.data.data.local

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import androidx.sqlite.db.SupportSQLiteQuery
import diarynote.data.data.local.source.CategoryNotesDataSource
import diarynote.data.data.local.source.DateNotesDataSource
import diarynote.data.data.local.source.NotesDataSourceFactory
import diarynote.data.data.local.source.SearchNotesDataSource
import diarynote.data.data.local.source.UserNotesDataSource
import diarynote.data.domain.local.NoteRepositoryLocal
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

class NoteRepositoryLocalImpl(
    private val database: AppDatabase
) : NoteRepositoryLocal {

    private lateinit var notesPagedList: LiveData<PagedList<NoteModel>>
    private lateinit var notesDataSourceFactory: NotesDataSourceFactory

    override fun getAllNotes(): Single<List<NoteEntity>> {
        return database.noteDao.all()
    }

    override fun getNoteById(id: Int): Single<NoteEntity> {
        return database.noteDao.getNoteById(id)
    }

    override fun getAllUserNotes(id: Int): Single<UserWithNotes> {
        return database.noteDao.getUserWithNotes(id)
    }

    override fun getUserNotesCount(id: Int): Single<Int> {
        return database.noteDao.getUserNotesCount()
    }

    override fun getUserNotesPagedList(
        compositeDisposable: CompositeDisposable,
        noteMapper: NoteMapper,
        dataSourceType: DataSourceType,
        userId: Int
    ): LiveData<PagedList<NoteModel>> {
        notesDataSourceFactory = NotesDataSourceFactory(
            compositeDisposable, noteMapper, dataSourceType, database, userId
        )

        return getPagedList(notesDataSourceFactory)
    }

    override fun getUserNotesByCategory(userId: Int, categoryId: Int): Single<List<NoteEntity>> {
        return database.noteDao.getUserNotesByCategory(userId, categoryId)
    }

    override fun getCategoryNotesPagedList(
        compositeDisposable: CompositeDisposable,
        noteMapper: NoteMapper,
        dataSourceType: DataSourceType,
        userId: Int,
        categoryId: Int
    ): LiveData<PagedList<NoteModel>> {

        notesDataSourceFactory = NotesDataSourceFactory(
            compositeDisposable, noteMapper, dataSourceType, database, userId, categoryId
        )

        return getPagedList(notesDataSourceFactory)
    }

    override fun getUserNotesFromDate(userId: Int, fromDate: Date): Single<List<NoteEntity>> {
        return database.noteDao.getUserNotesFromDate(userId, fromDate)
    }

    override fun getDateNotesPagedList(
        compositeDisposable: CompositeDisposable,
        noteMapper: NoteMapper,
        dataSourceType: DataSourceType,
        userId: Int,
        fromDate: Date
    ): LiveData<PagedList<NoteModel>> {

        notesDataSourceFactory = NotesDataSourceFactory(
            compositeDisposable, noteMapper, dataSourceType, database, userId, fromDate
        )

        return getPagedList(notesDataSourceFactory)
    }

    override fun getDateNotesPagedList(
        compositeDisposable: CompositeDisposable,
        noteMapper: NoteMapper,
        dataSourceType: DataSourceType,
        userId: Int,
        fromDate: Date,
        toDate: Date
    ): LiveData<PagedList<NoteModel>> {

        notesDataSourceFactory = NotesDataSourceFactory(
            compositeDisposable, noteMapper, dataSourceType, database, userId, fromDate, toDate
        )

        return getPagedList(notesDataSourceFactory)
    }

    override fun getUserNotesInDatePeriod(
        userId: Int,
        fromDate: Date,
        toDate: Date
    ): Single<List<NoteEntity>> {
        return database.noteDao.getUserNotesInDatePeriod(userId, fromDate, toDate)
    }

    override fun addNote(noteEntity: NoteEntity): Completable {
        return database.noteDao.insert(noteEntity)
    }

    override fun addNoteList(noteEntityList: List<NoteEntity>): Completable {
        return database.noteDao.insert(noteEntityList)
    }

    override fun searchUserNotesByString(query: SupportSQLiteQuery): Single<List<NoteEntity>> {
        return database.noteDao.searchUserNotesByStringWithPriority(query)
    }

    override fun getSearchNotesPagedList(
        compositeDisposable: CompositeDisposable,
        noteMapper: NoteMapper,
        dataSourceType: DataSourceType,
        userId: Int,
        searchString: String
    ): LiveData<PagedList<NoteModel>> {

        notesDataSourceFactory = NotesDataSourceFactory(
            compositeDisposable, noteMapper, dataSourceType, database, userId, searchString
        )

        return getPagedList(notesDataSourceFactory)
    }

    override fun updateNote(noteEntity: NoteEntity): Completable {
        return database.noteDao.update(noteEntity)
    }

    override fun deleteNote(noteEntity: NoteEntity): Completable {
        return database.noteDao.delete(noteEntity)
    }

    private fun getPagedList(dataSourceFactory: NotesDataSourceFactory): LiveData<PagedList<NoteModel>> {
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(40)
            .build()
        notesPagedList = LivePagedListBuilder(dataSourceFactory, config).build()

        return notesPagedList
    }

    override fun getNotesState(dataSourceType: DataSourceType): LiveData<NotesState> {
        return when (dataSourceType) {
            DataSourceType.CategoryNotesDataSource -> {
                Transformations.switchMap(
                    notesDataSourceFactory.categoryNotesLiveDataSource, CategoryNotesDataSource::notesState
                )
            }
            DataSourceType.DateNotesDataSource -> {
                Transformations.switchMap(
                    notesDataSourceFactory.dateNotesLiveDataSource, DateNotesDataSource::notesState
                )
            }
            DataSourceType.SearchNotesDataSource -> {
                Transformations.switchMap(
                    notesDataSourceFactory.searchNotesLiveDataSource, SearchNotesDataSource::notesState
                )
            }
            DataSourceType.UserNotesDataSource -> {
                Transformations.switchMap(
                    notesDataSourceFactory.userNotesLiveDataSource, UserNotesDataSource::notesState
                )
            }
        }
    }
}