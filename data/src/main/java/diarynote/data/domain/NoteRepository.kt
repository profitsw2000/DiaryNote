package diarynote.data.domain

import diarynote.data.room.entity.NoteEntity
import diarynote.data.room.related.UserWithNotes
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

interface NoteRepository {

    fun getAllNotes(): Single<List<NoteEntity>>

    fun getNoteById(id: Int): Single<NoteEntity>

    fun getAllUserNotes(id: Int): Single<UserWithNotes>

    fun addNote(noteEntity: NoteEntity): Completable

    fun updateNote(noteEntity: NoteEntity): Completable

    fun deleteNote(noteEntity: NoteEntity): Completable

}