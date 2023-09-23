package diarynote.data.domain

import diarynote.data.room.entity.NoteEntity
import diarynote.data.room.related.UserWithCategoriesAndNotes
import diarynote.data.room.related.UserWithNotes
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import java.util.Date

interface NoteRepository {

    fun getAllNotes(): Single<List<NoteEntity>>

    fun getNoteById(id: Int): Single<NoteEntity>

    fun getAllUserNotes(id: Int): Single<UserWithNotes>

    fun getUserNotesByCategory(userId: Int, categoryId: Int): Single<List<NoteEntity>>

    fun getUserNotesFromDate(userId: Int, fromDate: Date): Single<List<NoteEntity>>

    fun getUserNotesInDatePeriod(userId: Int, fromDate: Date, toDate: Date): Single<List<NoteEntity>>

    fun addNote(noteEntity: NoteEntity): Completable

    fun addNoteList(noteEntityList: List<NoteEntity>): Completable

    fun searchUserNotesWithWordInTags(userId: Int, search: String): Single<List<NoteEntity>>

    fun updateNote(noteEntity: NoteEntity): Completable

    fun deleteNote(noteEntity: NoteEntity): Completable

}