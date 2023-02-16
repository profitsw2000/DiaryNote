package diarynote.data.data

import diarynote.data.domain.local.UserRepositoryLocal
import diarynote.data.room.database.AppDatabase
import diarynote.data.room.entity.UserEntity
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

class UserRepositoryLocalImpl (
    private val database: AppDatabase
) : UserRepositoryLocal {

    override fun saveUser(userEntity: UserEntity): Completable {
        return database.userDao.insert(userEntity)
    }

    override fun getUserByLogin(login: String): Single<UserEntity> {
        return database.userDao.getUserByLogin(login)
    }
}