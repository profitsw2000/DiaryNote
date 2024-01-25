package diarynote.data.data.local.source

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import diarynote.data.mappers.NoteMapper
import diarynote.data.model.NoteModel
import diarynote.data.model.type.DataSourceType
import diarynote.data.room.database.AppDatabase
import io.reactivex.rxjava3.disposables.CompositeDisposable
import java.util.Date

class NotesDataSourceFactory(
    private val compositeDisposable: CompositeDisposable,
    private val noteMapper: NoteMapper,
    private val dataSourceType: DataSourceType,
    private val database: AppDatabase,
    private val userId: Int,
    private val categoryId: Int?,
    private val searchString: String?,
    private val fromDate: Date?,
    private val toDate: Date?
) : DataSource.Factory<Int, NoteModel>() {

    constructor(
        compositeDisposable: CompositeDisposable,
        noteMapper: NoteMapper,
        dataSourceType: DataSourceType,
        database: AppDatabase,
        userId: Int
    ) : this (compositeDisposable, noteMapper, dataSourceType, database, userId, null, null, null, null)

    constructor(
        compositeDisposable: CompositeDisposable,
        noteMapper: NoteMapper,
        dataSourceType: DataSourceType,
        database: AppDatabase,
        userId: Int,
        categoryId: Int?
    ) : this (compositeDisposable, noteMapper, dataSourceType, database, userId, categoryId, null, null, null)

    constructor(
        compositeDisposable: CompositeDisposable,
        noteMapper: NoteMapper,
        dataSourceType: DataSourceType,
        database: AppDatabase,
        userId: Int,
        searchString: String?
    ) : this (compositeDisposable, noteMapper, dataSourceType, database, userId, null, searchString, null, null)

    constructor(
        compositeDisposable: CompositeDisposable,
        noteMapper: NoteMapper,
        dataSourceType: DataSourceType,
        database: AppDatabase,
        userId: Int,
        fromDate: Date?
    ) : this (compositeDisposable, noteMapper, dataSourceType, database, userId, null, null, fromDate, null)

    constructor(
        compositeDisposable: CompositeDisposable,
        noteMapper: NoteMapper,
        dataSourceType: DataSourceType,
        database: AppDatabase,
        userId: Int,
        fromDate: Date?,
        toDate: Date?
    ) : this (compositeDisposable, noteMapper, dataSourceType, database, userId, null, null, fromDate, toDate)

    val userNotesLiveDataSource = MutableLiveData<UserNotesDataSource>()
    val categoryNotesLiveDataSource = MutableLiveData<CategoryNotesDataSource>()
    val dateNotesLiveDataSource = MutableLiveData<DateNotesDataSource>()
    val searchNotesLiveDataSource = MutableLiveData<SearchNotesDataSource>()

    override fun create(): DataSource<Int, NoteModel> {
        val notesDataSource = when(dataSourceType){
            DataSourceType.CategoryNotesDataSource -> getCategoryNotesDataSource(
                noteMapper = noteMapper,
                compositeDisposable = compositeDisposable,
                database = database,
                userId = userId,
                categoryId = categoryId
            )
            DataSourceType.DateNotesDataSource -> getDateNotesDataSource(
                noteMapper = noteMapper,
                compositeDisposable = compositeDisposable,
                database = database,
                userId = userId,
                fromDate = fromDate,
                toDate = toDate
            )
            DataSourceType.SearchNotesDataSource -> getSearchNotesDataSource(
                noteMapper = noteMapper,
                compositeDisposable = compositeDisposable,
                database = database,
                userId = userId,
                searchString = searchString
            )
            DataSourceType.UserNotesDataSource -> getUserNotesDataSource(
                noteMapper = noteMapper,
                compositeDisposable = compositeDisposable,
                database = database,
                userId = userId
            )
        }
        return notesDataSource
    }

    private fun getCategoryNotesDataSource(
        noteMapper: NoteMapper,
        compositeDisposable: CompositeDisposable,
        database: AppDatabase,
        userId: Int,
        categoryId: Int?
    ) : DataSource<Int, NoteModel> {
        if (categoryId != null) {
            val categoryNotesDataSource = CategoryNotesDataSource(
                noteMapper = noteMapper,
                compositeDisposable = compositeDisposable,
                database = database,
                userId = userId,
                categoryId = categoryId
            )
            categoryNotesLiveDataSource.postValue(categoryNotesDataSource)
            return categoryNotesDataSource
        } else {
            val userNotesDataSource = UserNotesDataSource(
                noteMapper = noteMapper,
                compositeDisposable = compositeDisposable,
                database = database,
                userId = userId
            )
            userNotesLiveDataSource.postValue(userNotesDataSource)
            return userNotesDataSource
        }
    }

    private fun getUserNotesDataSource(
        noteMapper: NoteMapper,
        compositeDisposable: CompositeDisposable,
        database: AppDatabase,
        userId: Int
    ) : DataSource<Int, NoteModel> {
        val userNotesDataSource = UserNotesDataSource(
            noteMapper = noteMapper,
            compositeDisposable = compositeDisposable,
            database = database,
            userId = userId
        )

        userNotesLiveDataSource.postValue(userNotesDataSource)
        return userNotesDataSource
    }

    private fun getSearchNotesDataSource(
        noteMapper: NoteMapper,
        compositeDisposable: CompositeDisposable,
        database: AppDatabase,
        userId: Int,
        searchString: String?
    ) : DataSource<Int, NoteModel> {
        if (searchString != null) {
            val searchNotesDataSource = SearchNotesDataSource(
                noteMapper = noteMapper,
                compositeDisposable = compositeDisposable,
                database = database,
                userId = userId,
                searchString = searchString
            )
            searchNotesLiveDataSource.postValue(searchNotesDataSource)
            return searchNotesDataSource
        } else {
            //throw NullPointerException()
            val userNotesDataSource = UserNotesDataSource(
                noteMapper = noteMapper,
                compositeDisposable = compositeDisposable,
                database = database,
                userId = userId
            )
            userNotesLiveDataSource.postValue(userNotesDataSource)
            return userNotesDataSource
        }
    }

    private fun getDateNotesDataSource(
        noteMapper: NoteMapper,
        compositeDisposable: CompositeDisposable,
        database: AppDatabase,
        userId: Int,
        fromDate: Date?,
        toDate: Date?
    ) : DataSource<Int, NoteModel> {
        return  if (toDate == null) getFromDateNotesDataSource(
            noteMapper,
            compositeDisposable,
            database,
            userId,
            fromDate
        ) else getDateRangeNotesDataSource(noteMapper,
            compositeDisposable,
            database,
            userId,
            fromDate,
            toDate
        )
    }

    private fun getFromDateNotesDataSource(
        noteMapper: NoteMapper,
        compositeDisposable: CompositeDisposable,
        database: AppDatabase,
        userId: Int,
        fromDate: Date?
    ) : DataSource<Int, NoteModel> {
        if (fromDate != null) {
            val dateNotesDataSource = DateNotesDataSource(
                noteMapper = noteMapper,
                compositeDisposable = compositeDisposable,
                database = database,
                userId = userId,
                fromDate = fromDate
            )
            dateNotesLiveDataSource.postValue(dateNotesDataSource)
            return dateNotesDataSource
        } else {
            val userNotesDataSource = UserNotesDataSource(
                noteMapper = noteMapper,
                compositeDisposable = compositeDisposable,
                database = database,
                userId = userId
            )
            userNotesLiveDataSource.postValue(userNotesDataSource)
            return userNotesDataSource
        }
    }

    private fun getDateRangeNotesDataSource(
        noteMapper: NoteMapper,
        compositeDisposable: CompositeDisposable,
        database: AppDatabase,
        userId: Int,
        fromDate: Date?,
        toDate: Date?
    ) : DataSource<Int, NoteModel> {
        if (fromDate != null && toDate != null) {
            val dateNotesDataSource = DateNotesDataSource(
                noteMapper = noteMapper,
                compositeDisposable = compositeDisposable,
                database = database,
                userId = userId,
                fromDate = fromDate,
                toDate = toDate
            )
            dateNotesLiveDataSource.postValue(dateNotesDataSource)
            return dateNotesDataSource
        } else {
            val userNotesDataSource = UserNotesDataSource(
                noteMapper = noteMapper,
                compositeDisposable = compositeDisposable,
                database = database,
                userId = userId
            )
            userNotesLiveDataSource.postValue(userNotesDataSource)
            return userNotesDataSource
        }
    }
}