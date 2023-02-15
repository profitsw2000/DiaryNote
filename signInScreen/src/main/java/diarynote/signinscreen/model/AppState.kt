package diarynote.signinscreen.model

import diarynote.data.model.UserModel

sealed class LoginState {
    data class Error(val message: String) : LoginState()
    data class LoginSuccess(val userModel: UserModel) : LoginState()
    object Loading : LoginState()
}
