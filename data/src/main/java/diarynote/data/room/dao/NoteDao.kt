package diarynote.data.room.dao

import androidx.room.*
import diarynote.data.room.entity.NoteEntity
import diarynote.data.room.entity.UserEntity
import io.reactivex.rxjava3.core.Completable

@Dao
interface NoteDao {


    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insert(noteEntity: NoteEntity): Completable

    @Update
    fun update(noteEntity: NoteEntity): Completable

    @Delete
    fun delete(noteEntity: NoteEntity): Completable
}