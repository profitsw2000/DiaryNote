package diarynote.data.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single
import diarynote.data.room.model.UserModel

@Dao
interface UserDao {
    @Query("SELECT * FROM UserModel")
    fun all(): Single<List<UserModel>>

    @Query("SELECT * FROM UserModel WHERE id LIKE :id")
    fun getUserById(id: Int): Single<UserModel>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(model: UserModel): Completable

    @Update
    fun update(model: UserModel): Completable

    @Delete
    fun delete(model: UserModel): Completable
}