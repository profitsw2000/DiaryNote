package diarynote.data.interactor

import diarynote.data.domain.local.UserRepositoryLocal
import diarynote.data.domain.web.UserRepositoryRemote
import diarynote.data.room.entity.UserEntity
import io.reactivex.rxjava3.core.Single

class UserInteractor(
    private val userRepositoryLocal: UserRepositoryLocal,
    private val userRepositoryRemote: UserRepositoryRemote
) {

    fun getUserByLogin(login: String, remote: Boolean): Single<UserEntity> {
        return if (remote) {
            userRepositoryRemote.getUserByLogin(login)
        } else {
            userRepositoryLocal.getUserByLogin(login)
        }
    }

}