package diarynote.data.interactor

import diarynote.data.domain.local.NoteRepositoryLocal
import diarynote.data.domain.web.NoteRepositoryRemote
import diarynote.data.room.entity.NoteEntity
import diarynote.data.room.related.UserWithNotes
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

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

    fun addNote(noteEntity: NoteEntity, remote: Boolean): Completable {
        return if (remote) {
            noteRepositoryRemote.addNote(noteEntity)
        } else {
            noteRepositoryLocal.addNote(noteEntity)
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