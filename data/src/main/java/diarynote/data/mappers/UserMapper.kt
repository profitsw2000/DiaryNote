package diarynote.data.mappers

import diarynote.data.model.UserModel
import diarynote.data.room.entity.UserEntity

class UserMapper {

    fun map(userModel: UserModel): UserEntity {
        return UserEntity(
            id = userModel.id,
            name = userModel.name,
            surname = userModel.surname,
            imagePath = userModel.imagePath,
            login = userModel.login,
            email = userModel.email,
            password = userModel.password
        )
    }

    fun map(userEntity: UserEntity): UserModel {
        return UserModel(
            id = userEntity.id,
            name = userEntity.name,
            surname = userEntity.surname,
            imagePath = userEntity.imagePath,
            login = userEntity.login,
            email = userEntity.email,
            password = userEntity.password
        )
    }
}