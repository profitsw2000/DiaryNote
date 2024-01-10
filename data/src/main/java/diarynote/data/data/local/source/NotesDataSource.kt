package diarynote.data.data.local.source

import android.content.SharedPreferences
import androidx.paging.PositionalDataSource
import diarynote.data.interactor.NoteInteractor
import diarynote.data.mappers.NoteMapper
import diarynote.data.model.NoteModel

class NotesDataSource(
    private val noteInteractor: NoteInteractor,
    private val noteMapper: NoteMapper
) : PositionalDataSource<Int>() {
    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<Int>) {
        TODO("Not yet implemented")
    }

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<Int>) {
        TODO("Not yet implemented")
    }

}