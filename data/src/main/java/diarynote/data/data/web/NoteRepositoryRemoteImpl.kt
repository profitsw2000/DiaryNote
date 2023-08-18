package diarynote.data.data.web

import diarynote.data.domain.web.NoteRepositoryRemote
import diarynote.data.room.entity.NoteEntity
import diarynote.data.room.related.UserWithNotes
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
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

    override fun getUserNotesByCategory(userId: Int, categoryId: Int): Single<List<NoteEntity>> {
        TODO("Not yet implemented")
    }

    override fun getUserNotesFromDate(userId: Int, fromDate: Date): Single<List<NoteEntity>> {
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

    override fun updateNote(noteEntity: NoteEntity): Completable {
        TODO("Not yet implemented")
    }

    override fun deleteNote(noteEntity: NoteEntity): Completable {
        TODO("Not yet implemented")
    }
}