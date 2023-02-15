package diarynote.data.data.web

import diarynote.data.domain.web.UserRepositoryRemote
import diarynote.data.room.entity.UserEntity
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

class UserRepositoryRemoteImpl : UserRepositoryRemote {
    override fun saveUser(userEntity: UserEntity): Completable {
        TODO("Not yet implemented")
    }

    override fun getUserByLogin(login: String): Single<UserEntity> {
        TODO("Not yet implemented")
    }
}