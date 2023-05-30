package diarynote.data.data.local

import diarynote.data.domain.local.NoteRepositoryLocal
import diarynote.data.room.database.AppDatabase
import diarynote.data.room.entity.NoteEntity
import diarynote.data.room.related.UserWithNotes
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

class NoteRepositoryLocalImpl(
    private val database: AppDatabase
) : NoteRepositoryLocal {
    override fun getAllNotes(): Single<List<NoteEntity>> {
        return database.noteDao.all()
    }

    override fun getNoteById(id: Int): Single<NoteEntity> {
        return database.noteDao.getNoteById(id)
    }

    override fun getAllUserNotes(id: Int): Single<UserWithNotes> {
        return database.noteDao.getUserWithNotes(id)
    }

    override fun addNote(noteEntity: NoteEntity): Completable {
        return database.noteDao.insert(noteEntity)
    }

    override fun addNoteList(noteEntityList: List<NoteEntity>): Completable {
        return database.noteDao.insert(noteEntityList)
    }

    override fun updateNote(noteEntity: NoteEntity): Completable {
        return database.noteDao.update(noteEntity)
    }

    override fun deleteNote(noteEntity: NoteEntity): Completable {
        return database.noteDao.delete(noteEntity)
    }
}