package diarynote.signinscreen.model

sealed class LoginState {
    data class Error(val message: String) : LoginState()
    object LoginSuccess : LoginState()
    object Loading : LoginState()
}
