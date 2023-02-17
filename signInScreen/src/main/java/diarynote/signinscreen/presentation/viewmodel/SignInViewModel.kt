package diarynote.signinscreen.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import diarynote.core.utils.*
import diarynote.core.viewmodel.CoreViewModel
import diarynote.data.interactor.UserInteractor
import diarynote.data.mappers.UserMapper
import diarynote.signinscreen.model.LoginState
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class SignInViewModel(
    private val userInteractor: UserInteractor
) : CoreViewModel() {

    private val inputValidator = InputValidator()

    private val _loginResultLiveData = MutableLiveData<LoginState>()
    val loginResultLiveData: LiveData<LoginState> by this::_loginResultLiveData

    fun signIn(login: String, password: String) {

        val loginIsValid = inputValidator.checkInputIsValid(login, LOGIN_MIN_LENGTH, LOGIN_PATTERN)
        val passwordIsValid = inputValidator.checkInputIsValid(password, PASSWORD_MIN_LENGTH, PASSWORD_PATTERN)

        if (loginIsValid && passwordIsValid) {
            authenticateUser(login, password)
        } else {
            invalidInput(!loginIsValid, !passwordIsValid)
        }
    }

    private fun authenticateUser(login: String, password: String) {
        _loginResultLiveData.value = LoginState.Loading
        userInteractor.getUserByLogin(login, false)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    if (it.password == password) {
                        _loginResultLiveData.value = LoginState.LoginSuccess
                    } else {
                        _loginResultLiveData.value = LoginState.Error(1 shl INVALID_PASSWORD_BIT_NUMBER)
                    }
                },
                {
                    _loginResultLiveData.value = LoginState.Error(1 shl ROOM_BIT_NUMBER)
                }
            )
    }

    private fun invalidInput(loginIsValid: Boolean, passwordIsValid: Boolean) {

        val errorCode = (loginIsValid.toInt() shl LOGIN_BIT_NUMBER) or
                (passwordIsValid.toInt() shl PASSWORD_BIT_NUMBER)

        _loginResultLiveData.value = LoginState.Error(errorCode)
    }

    private fun Boolean.toInt() = if (this) 1 else 0
}