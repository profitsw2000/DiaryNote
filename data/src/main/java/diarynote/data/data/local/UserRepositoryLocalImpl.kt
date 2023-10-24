package diarynote.data.data.local

import diarynote.data.domain.local.UserRepositoryLocal
import diarynote.data.room.database.AppDatabase
import diarynote.data.room.entity.UserEntity
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

class UserRepositoryLocalImpl (
    private val database: AppDatabase
) : UserRepositoryLocal {

    //Локальная база данных
    override fun addUser(userEntity: UserEntity): Single<Long> {
        return database.userDao.insert(userEntity)
    }

    override fun getUserById(userId: Int): Single<UserEntity> {
        return database.userDao.getUserById(userId)
    }

    override fun getUserByLogin(login: String): Single<UserEntity> {
        return database.userDao.getUserByLogin(login)
    }

    override fun getUserByEmail(email: String): Single<UserEntity> {
        return database.userDao.getUserByEmail(email)
    }

    override fun updateUserPassword(password: String, userId: Int): Completable {
        return database.userDao.updatePassword(password, userId)
    }

    override fun updateUser(userEntity: UserEntity): Completable {
        return database.userDao.update(userEntity)
    }

    override fun deleteUser(userEntity: UserEntity): Completable {
        return database.userDao.delete(userEntity)
    }
}