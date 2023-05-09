package diarynote.data.room.dao

import androidx.room.*
import diarynote.data.room.entity.NoteEntity
import diarynote.data.room.entity.UserEntity
import diarynote.data.room.related.UserWithNotes
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

@Dao
interface NoteDao {

    @Query("SELECT * FROM NoteEntity")
    fun all(): Single<List<NoteEntity>>

    @Transaction
    @Query("SELECT * FROM UserEntity WHERE id LIKE :id")
    fun getUserWithNotes(id: Int): Single<UserWithNotes>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insert(noteEntity: NoteEntity): Completable

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insert(noteEntityList: List<NoteEntity>): Completable

    @Update
    fun update(noteEntity: NoteEntity): Completable

    @Delete
    fun delete(noteEntity: NoteEntity): Completable
}