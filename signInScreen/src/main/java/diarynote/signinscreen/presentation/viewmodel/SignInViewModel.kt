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

private const val BASE_INVALID_INPUT_MESSAGE = "Неверный ввод:"
private const val INVALID_PASSWORD_INPUT_MESSAGE = "пароль должен содержать от 7 до 20 цифровых и буквенных символов"
private const val INVALID_LOGIN_INPUT_MESSAGE = "логин должен содержать от 4 до 20 цифровых и буквенных символов"
private const val INVALID_LOGIN_OR_PASSWORD_MESSAGE = "Неверный логин и/или пароль"

class SignInViewModel(
    private val userInteractor: UserInteractor,
    private val userMapper: UserMapper
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
            invalidInput(loginIsValid, passwordIsValid)
        }
    }

    private fun authenticateUser(login: String, password: String) {
        _loginResultLiveData.value = LoginState.Loading
        userInteractor.getUserByLogin(login, false)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    _loginResultLiveData.value = LoginState.LoginSuccess(userMapper.map(it))
                },
                {
                    _loginResultLiveData.value = LoginState.Error(INVALID_LOGIN_OR_PASSWORD_MESSAGE)
                }
            )
    }

    private fun invalidInput(loginIsValid: Boolean, passwordIsValid: Boolean) {
        var errorMessage = ""

        if (loginIsValid) {
            errorMessage = "$BASE_INVALID_INPUT_MESSAGE ${INVALID_PASSWORD_INPUT_MESSAGE}."
        } else if (passwordIsValid) {
            errorMessage = "$BASE_INVALID_INPUT_MESSAGE ${INVALID_LOGIN_INPUT_MESSAGE}."
        } else {
            errorMessage = "$BASE_INVALID_INPUT_MESSAGE ${INVALID_LOGIN_INPUT_MESSAGE}, ${INVALID_PASSWORD_INPUT_MESSAGE}."
        }
        _loginResultLiveData.value = LoginState.Error(errorMessage)
    }
}