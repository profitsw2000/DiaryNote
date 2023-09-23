package diarynote.data.interactor

import diarynote.data.domain.local.NoteRepositoryLocal
import diarynote.data.domain.web.NoteRepositoryRemote
import diarynote.data.room.entity.NoteEntity
import diarynote.data.room.related.UserWithCategoriesAndNotes
import diarynote.data.room.related.UserWithNotes
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import java.util.Date

class NoteInteractor(
    private val noteRepositoryLocal: NoteRepositoryLocal,
    private val noteRepositoryRemote: NoteRepositoryRemote
) {
    fun getAllNotes(remote: Boolean): Single<List<NoteEntity>> {
        return if (remote) {
            noteRepositoryRemote.getAllNotes()
        } else {
            noteRepositoryLocal.getAllNotes()
        }
    }

    fun getAllUserNotes(id: Int, remote: Boolean): Single<UserWithNotes> {
        return if (remote) {
            noteRepositoryRemote.getAllUserNotes(id)
        } else {
            noteRepositoryLocal.getAllUserNotes(id)
        }
    }

    fun getUserNotesByCategory(userId: Int, categoryId: Int, remote: Boolean): Single<List<NoteEntity>> {
        return if (remote) {
            noteRepositoryRemote.getUserNotesByCategory(userId, categoryId)
        } else {
            noteRepositoryLocal.getUserNotesByCategory(userId, categoryId)
        }
    }

    fun getUserNotesFromDate(userId: Int, fromDate: Date, remote: Boolean): Single<List<NoteEntity>> {
        return if (remote) {
            noteRepositoryRemote.getUserNotesFromDate(userId, fromDate)
        } else {
            noteRepositoryLocal.getUserNotesFromDate(userId, fromDate)
        }
    }

    fun getUserNotesInDatePeriod(userId: Int, fromDate: Date, toDate: Date, remote: Boolean): Single<List<NoteEntity>> {
        return if (remote) {
            noteRepositoryRemote.getUserNotesInDatePeriod(userId, fromDate, toDate)
        } else {
            noteRepositoryLocal.getUserNotesInDatePeriod(userId, fromDate, toDate)
        }
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

    fun getUserNotesWithWordInTag(userId: Int, search: String, remote: Boolean): Single<List<NoteEntity>> {
        return if (remote) {
            noteRepositoryRemote.searchUserNotesWithWordInTags(userId, search)
        } else {
            noteRepositoryLocal.searchUserNotesWithWordInTags(userId, search)
        }
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

}