package diarynote.signinscreen.model

import diarynote.data.model.UserModel

sealed class LoginState {
    data class Error(val errorCode: Int) : LoginState()
    object LoginSuccess : LoginState()
    object Loading : LoginState()
}
