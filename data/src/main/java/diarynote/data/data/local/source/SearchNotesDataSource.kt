package diarynote.data.data.local.source

import androidx.lifecycle.MutableLiveData
import androidx.paging.PositionalDataSource
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import diarynote.core.utils.SearchQueryBuilder
import diarynote.data.mappers.NoteMapper
import diarynote.data.model.NoteModel
import diarynote.data.model.state.NotesState
import diarynote.data.room.database.AppDatabase
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class SearchNotesDataSource(
    private val noteMapper: NoteMapper,
    private val compositeDisposable: CompositeDisposable,
    private val database: AppDatabase,
    private val userId: Int,
    private val searchString: String
) : PositionalDataSource<NoteModel>()  {

    val notesState: MutableLiveData<NotesState> = MutableLiveData()

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<NoteModel>) {
        notesState.postValue(NotesState.Loading)

        compositeDisposable.add(
            database.noteDao.searchUserNotesByStringWithPriority(
                getSearchQueryPair(searchString,
                    userId,
                    params.requestedLoadSize,
                    params.requestedStartPosition
                )
            )
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {
                        callback.onResult(noteMapper.map(it), 0)
                        notesState.postValue(NotesState.Loaded)
                    },
                    {
                        val message = it.message ?: ""
                        notesState.postValue(NotesState.Error(message, 0))
                    }
                )
        )
    }

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<NoteModel>) {
        notesState.postValue(NotesState.Loading)
        compositeDisposable.add(
            database.noteDao.searchUserNotesByStringWithPriority(
                getSearchQueryPair(
                    searchString,
                    userId,
                    params.loadSize,
                    params.startPosition
                )
            )
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {
                        callback.onResult(noteMapper.map(it))
                        notesState.postValue(NotesState.Loaded)
                    },
                    {
                        val message = it.message ?: ""
                        notesState.postValue(NotesState.Error(message, 0))
                    }
                )
        )
    }

    private fun getSearchQueryPair(searchString: String,
                                   userId: Int,
                                   loadSize: Int,
                                   offset: Int) : SimpleSQLiteQuery {

        val searchQueryBuilder = SearchQueryBuilder(
            searchString,
            userId,
            loadSize,
            offset,
            arrayListOf(0, 1, 2)
        )

        val queryString: String = searchQueryBuilder.getSearchQueryPair().first
        val queryArgs: Array<Any> = searchQueryBuilder.getSearchQueryPair().second.toTypedArray()

        return SimpleSQLiteQuery(queryString, queryArgs)
    }
}