package diarynote.registrationscreen.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import diarynote.core.utils.*
import diarynote.core.viewmodel.CoreViewModel
import diarynote.data.interactor.CategoryInteractor
import diarynote.data.interactor.NoteInteractor
import diarynote.data.interactor.UserInteractor
import diarynote.data.mappers.UserMapper
import diarynote.data.model.UserModel
import diarynote.data.room.baseCategoriesList
import diarynote.data.room.entity.CategoryEntity
import diarynote.registrationscreen.model.RegState
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class RegistrationViewModel(
    private val userInteractor: UserInteractor,
    private val categoryInteractor: CategoryInteractor,
    private val userMapper: UserMapper
) : CoreViewModel() {

    private val inputValidator = InputValidator()

    private val _registrationLiveData = MutableLiveData<RegState?>()
    val registrationLiveData: LiveData<RegState?> by this::_registrationLiveData

    fun registerUser(login: String, email: String, password: String, confirmPassword: String) {
        val loginIsValid = inputValidator.checkInputIsValid(login, LOGIN_MIN_LENGTH, LOGIN_PATTERN)
        val emailIsValid = inputValidator.checkInputIsValid(email, EMAIL_PATTERN)
        val passwordIsValid = inputValidator.checkInputIsValid(password, PASSWORD_MIN_LENGTH, PASSWORD_PATTERN)
        val confirmed = (password == confirmPassword) and password.isNotEmpty()

        if (loginIsValid && emailIsValid && passwordIsValid && confirmed) {
            addUser(UserModel(0, "", "", "", login, email, password))
        } else {
            invalidInput(!loginIsValid, !emailIsValid, !passwordIsValid, !confirmed)
        }
    }

    private fun invalidInput(loginIsValid: Boolean, emailIsValid: Boolean, passwordIsValid: Boolean, confirmed: Boolean) {
        val errorCode = (loginIsValid.toInt() shl LOGIN_BIT_NUMBER) or
                (emailIsValid.toInt() shl EMAIL_BIT_NUMBER) or
                (passwordIsValid.toInt() shl PASSWORD_BIT_NUMBER) or
                (confirmed.toInt() shl CONFIRM_PASSWORD_BIT_NUMBER)

        _registrationLiveData.value = RegState.Error(errorCode)
    }

    private fun addUser(userModel: UserModel) {
        _registrationLiveData.value = RegState.Loading

        userInteractor.addUser(userMapper.map(userModel), false)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe (
                {
                    _registrationLiveData.value = RegState.Success(userModel.copy(id = it.toInt()))
                },
                {
                    Log.d("VVV", "addUser: ${it.message}")
                    _registrationLiveData.value = RegState.Error(getErrorCode(it.message.toString()))
                }
            )
            .addViewLifeCycle()
    }

    private fun getErrorCode(errorMessage: String) : Int {
        return when(errorMessage) {
            "UNIQUE constraint failed: UserEntity.login (code 2067 SQLITE_CONSTRAINT_UNIQUE)" -> (1 shl LOGIN_ALREADY_EXIST_BIT_NUMBER)
            "UNIQUE constraint failed: UserEntity.email (code 2067 SQLITE_CONSTRAINT_UNIQUE)" -> (1 shl EMAIL_ALREADY_EXIST_BIT_NUMBER)
            else -> (1 shl ROOM_BIT_NUMBER)
        }
    }

    private fun getDefaultCategoriesList(userId: Int) : List<CategoryEntity> {
        return baseCategoriesList.map {
            it.copy(userId = userId)
        }
    }

    fun insertDefaultCategories(userModel: UserModel) {
        categoryInteractor.addCategoryList(getDefaultCategoriesList(userModel.id), false)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe (
                {

                },
                {
                    _registrationLiveData.value = RegState.Error(getErrorCode(it.message.toString()))
                }
            )
            .addViewLifeCycle()
    }

    fun clear() {
        _registrationLiveData.value = null
    }

    private fun Boolean.toInt() = if (this) 1 else 0
}