package diarynote.data.room.dao

import androidx.room.*
import diarynote.data.room.entity.CategoryEntity
import diarynote.data.room.related.UserWithCategories
import diarynote.data.room.related.UserWithCategoriesAndNotes
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

@Dao
interface CategoryDao {
    @Transaction
    @Query("SELECT * FROM UserEntity WHERE id LIKE :id")
    fun getUserWithCategories(id: Int): Single<UserWithCategories>

    @Transaction
    @Query("SELECT * FROM UserEntity INNER JOIN CategoryEntity ON CategoryEntity.user_id = UserEntity.id WHERE id LIKE :userId")
    fun getUserWithCategoryAndNotes(userId: Int, categoryId: Int): Single<UserWithCategoriesAndNotes>

    @Query("SELECT * FROM CategoryEntity")
    fun all(): Single<List<CategoryEntity>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insert(categoryEntity: CategoryEntity): Completable

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insert(categoryEntityList: List<CategoryEntity>): Completable

    @Update
    fun update(categoryEntity: CategoryEntity): Completable

    @Delete
    fun delete(categoryEntity: CategoryEntity): Completable
}