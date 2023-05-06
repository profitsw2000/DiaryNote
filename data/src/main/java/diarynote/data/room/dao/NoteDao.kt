package diarynote.data.room.dao

import androidx.room.*
import diarynote.data.room.entity.NoteEntity
import diarynote.data.room.related.UserWithNotes
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

@Dao
interface NoteDao {

    @Transaction
    @Query("SELECT * FROM UserEntity WHERE id LIKE :id")
    fun getUserWithNotes(id: Int): Single<UserWithNotes>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insert(noteEntity: NoteEntity): Completable

    @Update
    fun update(noteEntity: NoteEntity): Completable

    @Delete
    fun delete(noteEntity: NoteEntity): Completable
}