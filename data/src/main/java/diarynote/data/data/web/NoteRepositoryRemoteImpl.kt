package diarynote.data.data.web

import diarynote.data.domain.web.NoteRepositoryRemote
import diarynote.data.room.entity.NoteEntity
import diarynote.data.room.related.UserWithNotes
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

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