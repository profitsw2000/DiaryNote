package diarynote.data.data.local

import diarynote.data.domain.local.UserRepositoryLocal
import diarynote.data.room.database.AppDatabase
import diarynote.data.room.entity.UserEntity
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

class UserRepositoryLocalImpl (
    private val database: AppDatabase
) : UserRepositoryLocal {

    override fun addUser(userEntity: UserEntity): Completable {
        return database.userDao.insert(userEntity)
    }

    override fun getUserByLogin(login: String): Single<UserEntity> {
        return database.userDao.getUserByLogin(login)
    }

    override fun getUserByEmail(email: String): Single<UserEntity> {
        TODO("Not yet implemented")
    }
}