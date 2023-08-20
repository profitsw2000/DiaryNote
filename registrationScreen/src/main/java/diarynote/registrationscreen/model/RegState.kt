package diarynote.registrationscreen.model

import diarynote.data.model.UserModel

sealed class RegState{
    data class Success(val userModel: UserModel): RegState()
    data class Error(val code: Int): RegState()
    object Loading: RegState()
}
