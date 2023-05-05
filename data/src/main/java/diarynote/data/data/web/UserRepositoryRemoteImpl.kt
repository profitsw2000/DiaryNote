package diarynote.data.data.web

import diarynote.data.domain.web.UserRepositoryRemote
import diarynote.data.room.entity.UserEntity
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

class UserRepositoryRemoteImpl : UserRepositoryRemote {

    override fun addUser(userEntity: UserEntity): Single<Long> {
        TODO("Not yet implemented")
    }

    override fun getUserByLogin(login: String): Single<UserEntity> {
        TODO("Not yet implemented")
    }

    override fun getUserByEmail(email: String): Single<UserEntity> {
        TODO("Not yet implemented")
    }
}