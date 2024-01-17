package diarynote.data.data.local.source

import androidx.lifecycle.MutableLiveData
import androidx.paging.PositionalDataSource
import androidx.sqlite.db.SupportSQLiteQuery
import diarynote.data.mappers.NoteMapper
import diarynote.data.model.NoteModel
import diarynote.data.model.state.NotesState
import diarynote.data.room.database.AppDatabase
import io.reactivex.rxjava3.disposables.CompositeDisposable

class SearchNotesDataSource(
    private val noteMapper: NoteMapper,
    private val compositeDisposable: CompositeDisposable,
    private val database: AppDatabase,
    private val userId: Int,
    private val searchQuery: SupportSQLiteQuery
) : PositionalDataSource<NoteModel>()  {

    val notesState: MutableLiveData<NotesState> = MutableLiveData()

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<NoteModel>) {
        TODO("Not yet implemented")
    }

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<NoteModel>) {
        TODO("Not yet implemented")
    }
}