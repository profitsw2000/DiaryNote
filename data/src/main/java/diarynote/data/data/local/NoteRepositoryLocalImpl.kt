package diarynote.data.data.local

import diarynote.data.domain.local.NoteRepositoryLocal
import diarynote.data.room.database.AppDatabase
import diarynote.data.room.entity.NoteEntity
import diarynote.data.room.related.UserWithNotes
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import java.util.Date

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

    override fun getUserNotesByCategory(userId: Int, categoryId: Int): Single<List<NoteEntity>> {
        return database.noteDao.getUserNotesByCategory(userId, categoryId)
    }

    override fun getUserNotesFromDate(userId: Int, fromDate: Date): Single<List<NoteEntity>> {
        return database.noteDao.getUserNotesFromDate(userId, fromDate)
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

    override fun searchUserNotesWithWordInTags(
        userId: Int,
        search: String
    ): Single<List<NoteEntity>> {
        return database.noteDao.searchUserNotesWithWordInTags(userId, search)
    }

    override fun searchUserNotesWithWordInText(
        userId: Int,
        search: String
    ): Single<List<NoteEntity>> {
        return database.noteDao.searchUserNotesWithWordInText(userId, search)
    }

    override fun searchUserNotesByWord(userId: Int, search: String): Single<List<NoteEntity>> {
        return database.noteDao.searchUserNotesByWordWithPriority(userId, search)
    }

    override fun updateNote(noteEntity: NoteEntity): Completable {
        return database.noteDao.update(noteEntity)
    }

    override fun deleteNote(noteEntity: NoteEntity): Completable {
        return database.noteDao.delete(noteEntity)
    }
}