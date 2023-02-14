package diarynote.data.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import diarynote.data.room.model.UserEntity

@Dao
interface UserDao {
    @Query("SELECT * FROM UserEntity")
    fun all(): Single<List<UserEntity>>

    @Query("SELECT * FROM UserEntity WHERE id LIKE :id")
    fun getUserById(id: Int): Single<UserEntity>

    @Query("SELECT * FROM UserEntity WHERE login LIKE :login")
    fun getUserByLogin(login: Int): Single<UserEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(userEntity: UserEntity): Completable

    @Update
    fun update(userEntity: UserEntity): Completable

    @Delete
    fun delete(userEntity: UserEntity): Completable
}