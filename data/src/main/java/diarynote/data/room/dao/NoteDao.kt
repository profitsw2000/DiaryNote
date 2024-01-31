package diarynote.data.room.dao

import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import diarynote.data.room.entity.NoteEntity
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

    @Query("SELECT COUNT(*) FROM NoteEntity")
    fun getUserNotesCount(): Single<Int>

    @Query("SELECT * " +
            "FROM NoteEntity " +
            "WHERE NoteEntity.user_id = :userId AND " +
            "NoteEntity.category_id = :categoryId " +
            "ORDER BY NoteEntity.editDate DESC")
    fun getUserNotesByCategory(userId: Int, categoryId: Int): Single<List<NoteEntity>>

    @Query("SELECT * " +
            "FROM NoteEntity " +
            "WHERE NoteEntity.user_id = :userId AND " +
            "NoteEntity.category_id = :categoryId " +
            "ORDER BY NoteEntity.editDate DESC " +
            "LIMIT :loadSize OFFSET :offset")
    fun getUserNotesByCategory(userId: Int, categoryId: Int, loadSize: Int, offset: Int): Single<List<NoteEntity>>

    @Query("SELECT * " +
            "FROM NoteEntity " +
            "WHERE NoteEntity.user_id = :userId " +
            "ORDER BY NoteEntity.editDate DESC")
    fun getAllUserNotes(userId: Int): Single<List<NoteEntity>>

    @Query("SELECT * " +
            "FROM NoteEntity " +
            "WHERE NoteEntity.user_id = :userId " +
            "ORDER BY NoteEntity.editDate DESC " +
            "LIMIT :loadSize OFFSET :offset")
    fun getAllUserNotes(userId: Int, loadSize: Int, offset: Int): Single<List<NoteEntity>>

    @Query("SELECT * " +
            "FROM NoteEntity " +
            "WHERE NoteEntity.user_id = :userId AND NoteEntity.editDate >= :fromDate " +
            "ORDER BY NoteEntity.editDate DESC")
    fun getUserNotesFromDate(userId: Int, fromDate: Date): Single<List<NoteEntity>>

    @Query("SELECT * " +
            "FROM NoteEntity " +
            "WHERE NoteEntity.user_id = :userId AND NoteEntity.editDate >= :fromDate " +
            "ORDER BY NoteEntity.editDate DESC " +
            "LIMIT :loadSize OFFSET :offset")
    fun getUserNotesFromDate(userId: Int, fromDate: Date, loadSize: Int, offset: Int): Single<List<NoteEntity>>

    @Query("SELECT * " +
            "FROM NoteEntity " +
            "WHERE NoteEntity.user_id = :userId AND NoteEntity.editDate BETWEEN :fromDate AND :toDate " +
            "ORDER BY NoteEntity.editDate DESC")
    fun getUserNotesInDatePeriod(userId: Int, fromDate: Date, toDate: Date): Single<List<NoteEntity>>

    @Query("SELECT * " +
            "FROM NoteEntity " +
            "WHERE NoteEntity.user_id = :userId AND NoteEntity.editDate BETWEEN :fromDate AND :toDate " +
            "ORDER BY NoteEntity.editDate DESC " +
            "LIMIT :loadSize OFFSET :offset")
    fun getUserNotesInDatePeriod(userId: Int, fromDate: Date, toDate: Date, loadSize: Int, offset: Int): Single<List<NoteEntity>>

    @Transaction
    @Query("SELECT * " +
            "FROM UserEntity " +
            "WHERE id LIKE :id")
    fun getUserWithNotes(id: Int): Single<UserWithNotes>

    @Transaction
    @Query("SELECT * FROM UserEntity WHERE id LIKE :id")
    fun getUserWithCategoriesAndNotes(id: Int): Single<UserWithCategoriesAndNotes>

    @RawQuery
    fun searchUserNotesByStringWithPriority(query: SupportSQLiteQuery): Single<List<NoteEntity>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insert(noteEntity: NoteEntity): Completable

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insert(noteEntityList: List<NoteEntity>): Completable

    @Update
    fun update(noteEntity: NoteEntity): Completable

    @Delete
    fun delete(noteEntity: NoteEntity): Completable
}

