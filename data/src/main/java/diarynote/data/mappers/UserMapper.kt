package diarynote.data.mappers

import diarynote.data.model.UserModel
import diarynote.data.room.entity.UserEntity

class UserMapper {

    fun map(userModel: UserModel): UserEntity {
        return UserEntity(
            id = userModel.id,
            login = userModel.login,
            email = userModel.email,
            password = userModel.password
        )
    }

    fun map(userEntity: UserEntity): UserModel {
        return UserModel(
            id = userEntity.id,
            login = userEntity.login,
            email = userEntity.email,
            password = userEntity.password
        )
    }
}