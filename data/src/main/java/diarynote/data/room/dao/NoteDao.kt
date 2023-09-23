package diarynote.data.room.dao

import androidx.room.*
import diarynote.data.room.entity.NoteEntity
import diarynote.data.room.entity.UserEntity
import diarynote.data.room.related.UserWithCategoriesAndNotes
import diarynote.data.room.related.UserWithNotes
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import java.util.Date

@Dao
interface NoteDao {

    @Query("SELECT * FROM NoteEntity")
    fun all(): Single<List<NoteEntity>>

    @Query("SELECT * FROM NoteEntity WHERE id LIKE :id")
    fun getNoteById(id: Int): Single<NoteEntity>

    @Query("SELECT * FROM NoteEntity WHERE NoteEntity.user_id = :userId AND NoteEntity.category_id = :categoryId")
    fun getUserNotesByCategory(userId: Int, categoryId: Int): Single<List<NoteEntity>>

    @Query("SELECT * " +
            "FROM NoteEntity " +
            "WHERE NoteEntity.user_id = :userId " +
            "ORDER BY NoteEntity.editDate DESC")
    fun getAllUserNotes(userId: Int): Single<List<NoteEntity>>

    @Query("SELECT * " +
            "FROM NoteEntity " +
            "WHERE NoteEntity.user_id = :userId AND NoteEntity.editDate >= :fromDate " +
            "ORDER BY NoteEntity.editDate DESC")
    fun getUserNotesFromDate(userId: Int, fromDate: Date): Single<List<NoteEntity>>

    @Query("SELECT * " +
            "FROM NoteEntity " +
            "WHERE NoteEntity.user_id = :userId AND NoteEntity.editDate BETWEEN :fromDate AND :toDate " +
            "ORDER BY NoteEntity.editDate DESC")
    fun getUserNotesInDatePeriod(userId: Int, fromDate: Date, toDate: Date): Single<List<NoteEntity>>

    @Transaction
    @Query("SELECT * " +
            "FROM UserEntity " +
            "WHERE id LIKE :id")
    fun getUserWithNotes(id: Int): Single<UserWithNotes>

    @Transaction
    @Query("SELECT * FROM UserEntity WHERE id LIKE :id")
    fun getUserWithCategoriesAndNotes(id: Int): Single<UserWithCategoriesAndNotes>

    @Query("SELECT * FROM NoteEntity WHERE NoteEntity.user_id LIKE :userId AND NoteEntity.tags LIKE :search")
    fun searchUserNotesWithWordInTags(userId: Int, search: String): Single<List<NoteEntity>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insert(noteEntity: NoteEntity): Completable

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insert(noteEntityList: List<NoteEntity>): Completable

    @Update
    fun update(noteEntity: NoteEntity): Completable

    @Delete
    fun delete(noteEntity: NoteEntity): Completable
}