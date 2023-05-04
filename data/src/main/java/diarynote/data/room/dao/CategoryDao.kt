package diarynote.data.room.dao

import androidx.room.*
import diarynote.data.room.entity.CategoryEntity
import diarynote.data.room.related.UserWithCategories
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

@Dao
interface CategoryDao {
    @Transaction
    @Query("SELECT * FROM UserEntity WHERE id LIKE :id")
    fun getUserWithCategories(id: Int): Single<UserWithCategories>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insert(categoryEntity: CategoryEntity): Completable

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insert(categoryEntityList: List<CategoryEntity>): Completable

    @Update
    fun update(categoryEntity: CategoryEntity): Completable

    @Delete
    fun delete(categoryEntity: CategoryEntity): Completable
}