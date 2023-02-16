package diarynote.registrationscreen.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import diarynote.core.utils.*
import diarynote.core.viewmodel.CoreViewModel
import diarynote.data.interactor.UserInteractor
import diarynote.data.mappers.UserMapper
import diarynote.data.model.UserModel
import diarynote.registrationscreen.model.RegState
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class RegistrationViewModel(
    private val userInteractor: UserInteractor,
    private val userMapper: UserMapper
) : CoreViewModel() {

    private val inputValidator = InputValidator()

    private val _registrationLiveData = MutableLiveData<RegState>()
    val registrationLiveData: LiveData<RegState> by this::_registrationLiveData

    fun registerUser(login: String, email: String, password: String, confirmPassword: String) {
        val loginIsValid = inputValidator.checkInputIsValid(login, LOGIN_MIN_LENGTH, LOGIN_PATTERN)
        val emailIsValid = inputValidator.checkInputIsValid(email, EMAIL_PATTERN)
        val passwordIsValid = inputValidator.checkInputIsValid(password, PASSWORD_MIN_LENGTH, PASSWORD_PATTERN)
        val confirmed = (password == confirmPassword)

        if (loginIsValid && passwordIsValid && passwordIsValid && confirmed) {
            addUser(UserModel(null, login, email, password))
        } else {
            invalidInput(loginIsValid, emailIsValid, passwordIsValid, confirmed)
        }
    }

    private fun invalidInput(loginIsValid: Boolean, emailIsValid: Boolean, passwordIsValid: Boolean, confirmed: Boolean) {
        val errorCode = (loginIsValid.toInt() shl 0) or
                (emailIsValid.toInt() shl 1) or
                (passwordIsValid.toInt() shl 2) or
                (confirmed.toInt() shl 3)

        _registrationLiveData.value = RegState.Error(errorCode)
    }

    private fun addUser(userModel: UserModel) {
        _registrationLiveData.value = RegState.Loading

        userInteractor.addUser(userMapper.map(userModel), false)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe (
                {
                    _registrationLiveData.value = RegState.Success(userModel)
                },
                {
                    _registrationLiveData.value = RegState.Error(ROOM_BIT_NUMBER shl 1)
                }
            )
    }

    private fun Boolean.toInt() = if (this) 1 else 0
}