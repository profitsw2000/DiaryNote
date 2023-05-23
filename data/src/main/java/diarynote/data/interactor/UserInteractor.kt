package diarynote.data.interactor

import diarynote.data.domain.local.UserRepositoryLocal
import diarynote.data.domain.web.UserRepositoryRemote
import diarynote.data.room.entity.UserEntity
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

class UserInteractor(
    private val userRepositoryLocal: UserRepositoryLocal,
    private val userRepositoryRemote: UserRepositoryRemote
) {

    fun addUser(userEntity: UserEntity, remote: Boolean): Single<Long> {
        return if (remote) {
            userRepositoryRemote.addUser(userEntity)
        } else {
            userRepositoryLocal.addUser(userEntity)
        }
    }

    fun getUserByLogin(login: String, remote: Boolean): Single<UserEntity> {
        return if (remote) {
            userRepositoryRemote.getUserByLogin(login)
        } else {
            userRepositoryLocal.getUserByLogin(login)
        }
    }

    fun getUserByEmail(email: String, remote: Boolean): Single<UserEntity> {
        return if (remote) {
            userRepositoryRemote.getUserByEmail(email)
        } else {
            userRepositoryLocal.getUserByEmail(email)
        }
    }

}