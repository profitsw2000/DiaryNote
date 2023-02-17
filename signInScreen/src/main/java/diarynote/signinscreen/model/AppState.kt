package diarynote.signinscreen.model

import diarynote.data.model.UserModel

sealed class LoginState {
    data class Error(val errorCode: Int) : LoginState()
    data class LoginSuccess(val userModel: UserModel) : LoginState()
    object Loading : LoginState()
}
