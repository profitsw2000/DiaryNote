package diarynote.template.model

import diarynote.data.model.UserModel

sealed class UserState {
    data class Error(val errorCode: Int, val message: String) : UserState()
    data class Success(val userModel: UserModel) : UserState()
    object Loading : UserState()
}
