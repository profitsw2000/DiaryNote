package diarynote.data.interactor

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.PagedList
import androidx.sqlite.db.SupportSQLiteQuery
import diarynote.data.data.local.source.CategoryNotesDataSource
import diarynote.data.data.local.source.DateNotesDataSource
import diarynote.data.data.local.source.NotesDataSourceFactory
import diarynote.data.data.local.source.SearchNotesDataSource
import diarynote.data.data.local.source.UserNotesDataSource
import diarynote.data.domain.local.NoteRepositoryLocal
import diarynote.data.domain.web.NoteRepositoryRemote
import diarynote.data.mappers.NoteMapper
import diarynote.data.model.NoteModel
import diarynote.data.model.state.NotesState
import diarynote.data.model.type.DataSourceType
import diarynote.data.room.entity.NoteEntity
import diarynote.data.room.related.UserWithCategoriesAndNotes
import diarynote.data.room.related.UserWithNotes
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import java.util.Date

class NoteInteractor(
    private val noteRepositoryLocal: NoteRepositoryLocal,
    private val noteRepositoryRemote: NoteRepositoryRemote
) {

    fun getUserNotesCount(id: Int, remote: Boolean): Single<Int> {
        return if (remote) {
            noteRepositoryRemote.getUserNotesCount(id)
        } else {
            noteRepositoryLocal.getUserNotesCount(id)
        }
    }

    fun getUserNotesPagedList(
        compositeDisposable: CompositeDisposable,
        noteMapper: NoteMapper,
        dataSourceType: DataSourceType,
        userId: Int,
        remote: Boolean
    ): LiveData<PagedList<NoteModel>> {

        return if (remote) noteRepositoryRemote.getUserNotesPagedList(
            compositeDisposable = compositeDisposable,
            noteMapper = noteMapper,
            dataSourceType = dataSourceType,
            userId = userId
        ) else noteRepositoryLocal.getUserNotesPagedList(
            compositeDisposable = compositeDisposable,
            noteMapper = noteMapper,
            dataSourceType = dataSourceType,
            userId = userId
        )
    }

    fun getCategoryNotesPagedList(
        compositeDisposable: CompositeDisposable,
        noteMapper: NoteMapper,
        dataSourceType: DataSourceType,
        userId: Int,
        categoryId: Int,
        remote: Boolean
    ): LiveData<PagedList<NoteModel>> {

        return if (remote) noteRepositoryRemote.getCategoryNotesPagedList(
            compositeDisposable = compositeDisposable,
            noteMapper = noteMapper,
            dataSourceType = dataSourceType,
            userId = userId,
            categoryId = categoryId
        ) else noteRepositoryLocal.getCategoryNotesPagedList(
            compositeDisposable = compositeDisposable,
            noteMapper = noteMapper,
            dataSourceType = dataSourceType,
            userId = userId,
            categoryId = categoryId
        )
    }

    fun getDateNotesPagedList(
        compositeDisposable: CompositeDisposable,
        noteMapper: NoteMapper,
        dataSourceType: DataSourceType,
        userId: Int,
        fromDate: Date,
        remote: Boolean
    ): LiveData<PagedList<NoteModel>> {

        return if (remote) noteRepositoryRemote.getDateNotesPagedList(
            compositeDisposable = compositeDisposable,
            noteMapper = noteMapper,
            dataSourceType = dataSourceType,
            userId = userId,
            fromDate = fromDate
        ) else noteRepositoryLocal.getDateNotesPagedList(
            compositeDisposable = compositeDisposable,
            noteMapper = noteMapper,
            dataSourceType = dataSourceType,
            userId = userId,
            fromDate = fromDate
        )
    }

    fun getDateNotesPagedList(
        compositeDisposable: CompositeDisposable,
        noteMapper: NoteMapper,
        dataSourceType: DataSourceType,
        userId: Int,
        fromDate: Date,
        toDate: Date,
        remote: Boolean
    ): LiveData<PagedList<NoteModel>> {

        return if (remote) noteRepositoryRemote.getDateNotesPagedList(
            compositeDisposable = compositeDisposable,
            noteMapper = noteMapper,
            dataSourceType = dataSourceType,
            userId = userId,
            fromDate = fromDate,
            toDate = toDate
        ) else noteRepositoryLocal.getDateNotesPagedList(
            compositeDisposable = compositeDisposable,
            noteMapper = noteMapper,
            dataSourceType = dataSourceType,
            userId = userId,
            fromDate = fromDate,
            toDate = toDate
        )
    }

    fun getNoteById(id: Int, remote: Boolean) : Single<NoteEntity> {
        return if (remote) {
            noteRepositoryRemote.getNoteById(id)
        } else {
            noteRepositoryLocal.getNoteById(id)
        }
    }

    fun addNote(noteEntity: NoteEntity, remote: Boolean): Completable {
        return if (remote) {
            noteRepositoryRemote.addNote(noteEntity)
        } else {
            noteRepositoryLocal.addNote(noteEntity)
        }
    }

    fun addNoteList(noteEntityList: List<NoteEntity>, remote: Boolean): Completable {
        return if (remote) {
            noteRepositoryRemote.addNoteList(noteEntityList)
        } else {
            noteRepositoryLocal.addNoteList(noteEntityList)
        }
    }

    fun getSearchNotesPagedList(
        compositeDisposable: CompositeDisposable,
        noteMapper: NoteMapper,
        dataSourceType: DataSourceType,
        userId: Int,
        searchString: String,
        remote: Boolean
    ): LiveData<PagedList<NoteModel>> {

        return if (remote) noteRepositoryRemote.getSearchNotesPagedList(
            compositeDisposable = compositeDisposable,
            noteMapper = noteMapper,
            dataSourceType = dataSourceType,
            userId = userId,
            searchString = searchString
        ) else (
            noteRepositoryLocal.getSearchNotesPagedList(
                compositeDisposable = compositeDisposable,
                noteMapper = noteMapper,
                dataSourceType = dataSourceType,
                userId = userId,
                searchString = searchString
            )
        )
    }

    fun updateNote(noteEntity: NoteEntity, remote: Boolean): Completable {
        return if (remote) {
            noteRepositoryRemote.updateNote(noteEntity)
        } else {
            noteRepositoryLocal.updateNote(noteEntity)
        }
    }

    fun deleteNote(noteEntity: NoteEntity, remote: Boolean): Completable {
        return if (remote) {
            noteRepositoryRemote.deleteNote(noteEntity)
        } else {
            noteRepositoryLocal.deleteNote(noteEntity)
        }
    }

    fun getNotesState(dataSourceType: DataSourceType, remote: Boolean): LiveData<NotesState> {
        return if (remote) noteRepositoryRemote.getNotesState(dataSourceType)
        else noteRepositoryLocal.getNotesState(dataSourceType)
    }

}