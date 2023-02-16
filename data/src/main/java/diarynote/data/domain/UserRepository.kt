package diarynote.data.domain

import diarynote.data.room.entity.UserEntity
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

interface UserRepository {

    fun saveUser(userEntity: UserEntity): Completable

    fun getUserByLogin(login: String): Single<UserEntity>
}