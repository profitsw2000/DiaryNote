package diarynote.settingsfragment.presentation.viewmodel

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import diarynote.core.utils.BACKUP_BIT_NUMBER
import diarynote.core.utils.CONFIRM_PASSWORD_BIT_NUMBER
import diarynote.core.utils.CURRENT_PASSWORD_BIT_NUMBER
import diarynote.core.utils.EMAIL_ALREADY_EXIST_BIT_NUMBER
import diarynote.core.utils.EMAIL_BIT_NUMBER
import diarynote.core.utils.EMAIL_PATTERN
import diarynote.core.utils.InputValidator
import diarynote.core.utils.LOGIN_ALREADY_EXIST_BIT_NUMBER
import diarynote.core.utils.LOGIN_BIT_NUMBER
import diarynote.core.utils.LOGIN_MIN_LENGTH
import diarynote.core.utils.LOGIN_PATTERN
import diarynote.core.utils.NAME_BIT_NUMBER
import diarynote.core.utils.NAME_MIN_LENGTH
import diarynote.core.utils.NAME_PATTERN
import diarynote.core.utils.PASSWORD_BIT_NUMBER
import diarynote.core.utils.PASSWORD_MIN_LENGTH
import diarynote.core.utils.PASSWORD_PATTERN
import diarynote.core.utils.RESTORE_BIT_NUMBER
import diarynote.core.utils.ROOM_BIT_NUMBER
import diarynote.core.utils.ROOM_UPDATE_BIT_NUMBER
import diarynote.core.utils.SURNAME_BIT_NUMBER
import diarynote.core.viewmodel.CoreViewModel
import diarynote.data.appsettings.APP_THEME_LIGHT
import diarynote.data.appsettings.CURRENT_THEME_KEY
import diarynote.data.appsettings.DEFAULT_THEME_KEY
import diarynote.data.appsettings.INACTIVE_TIME_PERIOD_INDEX_KEY
import diarynote.data.appsettings.LANGUAGE_ID_KEY
import diarynote.data.appsettings.LANGUAGE_KEY
import diarynote.data.appsettings.PASSWORD_REQUIRED_KEY
import diarynote.data.appsettings.RUSSIAN_LANGUAGE_ID
import diarynote.data.domain.CURRENT_USER_ID
import diarynote.data.domain.ROOM_ERROR_CODE
import diarynote.data.interactor.SettingsInteractor
import diarynote.data.interactor.UserInteractor
import diarynote.data.mappers.UserMapper
import diarynote.data.model.SettingsMenuItemModel
import diarynote.data.model.UserModel
import diarynote.data.room.entity.UserEntity
import diarynote.data.model.state.BackupState
import diarynote.data.model.state.HelpState
import diarynote.data.model.state.UserState
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class SettingsViewModel(
    private val settingsInteractor: SettingsInteractor,
    private val userInteractor: UserInteractor,
    private val sharedPreferences: SharedPreferences,
    private val userMapper: UserMapper
) : CoreViewModel() {

    private val emptyUserModel = UserModel(0, "", "", "", "", "", "")

    private val _settingsLiveData = MutableLiveData<List<SettingsMenuItemModel>>()
    val settingsLiveData: LiveData<List<SettingsMenuItemModel>> by this::_settingsLiveData

    private val _userLiveData = MutableLiveData<UserState?>()
    val userLiveData: LiveData<UserState?> by this::_userLiveData

    private val _backupLiveData = MutableLiveData<BackupState?>()
    val backupLiveData: LiveData<BackupState?> by this::_backupLiveData

    private val _helpLiveData = MutableLiveData<HelpState?>()
    val helpLiveData: LiveData<HelpState?> by this::_helpLiveData

    fun getSettingsMenuItemList(context: Context) {
        _settingsLiveData.value = settingsInteractor.getSettingsMenuItemsList(context,false)
    }

    fun getAccountSettingsMenuItemList(context: Context) {
        _settingsLiveData.value = settingsInteractor.getAccountSettingsMenuItemsList(context,false)
    }

    fun getCurrentUserInfo(){
        getUserInfoById(sharedPreferences.getInt(CURRENT_USER_ID, 0))
    }

    fun deleteCurrentUser(userModel: UserModel) {
        deleteUser(userMapper.map(userModel))
    }

    fun getCurrentTheme(): Int {
        return sharedPreferences.getInt(CURRENT_THEME_KEY, APP_THEME_LIGHT)
    }

    fun getFromDefaultDeviceMode(): Boolean {
        return sharedPreferences.getBoolean(DEFAULT_THEME_KEY, false)
    }

    fun setCurrentTheme(theme: Int) {
        sharedPreferences
            .edit()
            .putInt(CURRENT_THEME_KEY, theme)
            .apply()
    }

    fun setCurrentLanguage(language: String) {
        sharedPreferences
            .edit()
            .putString(LANGUAGE_KEY, language)
            .apply()
    }

    fun setCurrentLanguageId(languageId: Int) {
        sharedPreferences
            .edit()
            .putInt(LANGUAGE_ID_KEY, languageId)
            .apply()
    }

    fun getCurrentLanguageId() : Int {
        return sharedPreferences.getInt(LANGUAGE_ID_KEY, RUSSIAN_LANGUAGE_ID)
    }

    fun setFromDefaultDeviceMode(mode: Boolean) {
        sharedPreferences
            .edit()
            .putBoolean(DEFAULT_THEME_KEY, mode)
            .apply()
    }

    fun setDefaultUserId() {
        sharedPreferences
            .edit()
            .putInt(CURRENT_USER_ID, 0)
            .apply()
    }

    private fun getUserInfoById(userId: Int) {
        _userLiveData.value = UserState.Loading
        userInteractor.getUserById(userId, false)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    _userLiveData.value = UserState.Success(userMapper.map(it))
                },
                {
                    _userLiveData.value = UserState.Error(ROOM_ERROR_CODE, it.message?: "")
                }
            )
    }

    fun isPasswordRequired(): Boolean {
        return sharedPreferences.getBoolean(PASSWORD_REQUIRED_KEY, true)
    }

    fun setPasswordRequired(passwordRequired: Boolean) {
        sharedPreferences
            .edit()
            .putBoolean(PASSWORD_REQUIRED_KEY, passwordRequired)
            .apply()
    }

    fun getCurrentInactiveTimePeriodIndex(): Int {
        return sharedPreferences.getInt(INACTIVE_TIME_PERIOD_INDEX_KEY, 0)
    }

    fun setCurrentInactiveTimePeriodIndex(index: Int) {
        sharedPreferences
            .edit()
            .putInt(INACTIVE_TIME_PERIOD_INDEX_KEY, index)
            .apply()
    }

    fun changeUserPassword(currentPassword: String,
                           newPassword: String,
                           confirmPassword: String,
                           userModel: UserModel) {
        val inputValidator = InputValidator()

        val currentPasswordIsValid = (currentPassword == userModel.password)
        val passwordIsValid = inputValidator.checkInputIsValid(newPassword, PASSWORD_MIN_LENGTH, PASSWORD_PATTERN)
        val confirmed = (newPassword == confirmPassword)

        if (currentPasswordIsValid && passwordIsValid && confirmed) {
            updateUserPassword(userModel, newPassword, sharedPreferences.getInt(CURRENT_USER_ID, 0))
        } else {
            invalidInput(!currentPasswordIsValid, !passwordIsValid, !confirmed)
        }
    }

    private fun invalidInput(currentPasswordIsValid: Boolean, passwordIsValid: Boolean, confirmed: Boolean) {
        val errorCode = (currentPasswordIsValid.toInt() shl CURRENT_PASSWORD_BIT_NUMBER) or
                (passwordIsValid.toInt() shl PASSWORD_BIT_NUMBER) or
                (confirmed.toInt() shl CONFIRM_PASSWORD_BIT_NUMBER)

        _userLiveData.value = UserState.Error(errorCode, "")
    }

    private fun updateUserPassword(userModel: UserModel, newPassword: String, userId: Int) {
        _userLiveData.value = UserState.Loading
        userInteractor.updateUserPassword(newPassword, userId, false)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    _userLiveData.value = UserState.Success(userModel)
                },
                {
                    val message = it.message ?: ""
                    _userLiveData.value = UserState.Error((1 shl ROOM_BIT_NUMBER), message)
                }
            )
    }

    fun changeUserInfo(userName: String,
                           userSurname: String,
                           login: String,
                           email: String,
                           userModel: UserModel) {
        val inputValidator = InputValidator()

        val nameIsValid = inputValidator.checkInputIsValid(userName, NAME_MIN_LENGTH, NAME_PATTERN) || (userName == "")
        val surnameIsValid = inputValidator.checkInputIsValid(userSurname, NAME_MIN_LENGTH, NAME_PATTERN) || (userSurname == "")
        val loginIsValid = inputValidator.checkInputIsValid(login, LOGIN_MIN_LENGTH, LOGIN_PATTERN)
        val emailIsValid = inputValidator.checkInputIsValid(email, EMAIL_PATTERN)

        if (nameIsValid && surnameIsValid && loginIsValid && emailIsValid) {
            updateUserInfo(
                UserModel(id = userModel.id,
                name = userName,
                surname = userSurname,
                imagePath = userModel.imagePath,
                login = login,
                email = email,
                password = userModel.password)
            )
        } else {
            invalidUserInfoInput(!nameIsValid, !surnameIsValid, !loginIsValid, !emailIsValid)
        }
    }

    fun changeUserImagePath(imagePath: String, userModel: UserModel) {
        if (imagePath != userModel.imagePath) {
            updateUserInfo(userModel.copy(imagePath = imagePath))
        }
    }

    private fun invalidUserInfoInput(nameIsValid: Boolean,
                                     surnameIsValid: Boolean,
                                     loginIsValid: Boolean,
                                     emailIsValid: Boolean) {
        val errorCode = (nameIsValid.toInt() shl NAME_BIT_NUMBER) or
                (surnameIsValid.toInt() shl SURNAME_BIT_NUMBER) or
                (loginIsValid.toInt() shl LOGIN_BIT_NUMBER) or
                (emailIsValid.toInt() shl EMAIL_BIT_NUMBER)

        _userLiveData.value = UserState.Error(errorCode, "")
    }

    private fun updateUserInfo(userModel: UserModel) {
        _userLiveData.value = UserState.Loading
        userInteractor.updateUser(userMapper.map(userModel), false)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    _userLiveData.value = UserState.Success(userModel)
                },
                {
                    val message = it.message ?: ""
                    _userLiveData.value = UserState.Error(getErrorCode(message), message)
                }
            )
    }

    private fun getErrorCode(errorMessage: String) : Int {
        return when(errorMessage) {
            "UNIQUE constraint failed: UserEntity.login (code 2067 SQLITE_CONSTRAINT_UNIQUE)" -> (1 shl LOGIN_ALREADY_EXIST_BIT_NUMBER)
            "UNIQUE constraint failed: UserEntity.email (code 2067 SQLITE_CONSTRAINT_UNIQUE)" -> (1 shl EMAIL_ALREADY_EXIST_BIT_NUMBER)
            else -> (1 shl ROOM_BIT_NUMBER) or (1 shl ROOM_UPDATE_BIT_NUMBER)
        }
    }

    private fun deleteUser(userEntity: UserEntity) {
        _userLiveData.value = UserState.Loading
        userInteractor.deleteUser(userEntity, false)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    _userLiveData.value = UserState.Success(userMapper.map(userEntity))
                },
                {
                    val message = it.message ?: ""
                    _userLiveData.value = UserState.Error((1 shl ROOM_BIT_NUMBER), message)
                }
            )
    }

    fun importDB(uri: Uri) {
        _backupLiveData.value = BackupState.Loading
        settingsInteractor.importDB(uri)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    _backupLiveData.value = BackupState.SuccessRestore
                },
                {
                    val message = it.message ?: ""
                    _backupLiveData.value = BackupState.Error(message, (1 shl RESTORE_BIT_NUMBER))
                }
            )
    }

    fun exportDB(uri: Uri) {
        _backupLiveData.value = BackupState.Loading
        settingsInteractor.exportDB(uri)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    _backupLiveData.value = BackupState.SuccessBackup
                },
                {
                    val message = it.message ?: ""
                    _backupLiveData.value = BackupState.Error(message, (1 shl BACKUP_BIT_NUMBER))
                }
            )
    }

    fun getHelpItemsList(context: Context) {
        _helpLiveData.value = HelpState.Loading
        settingsInteractor.getHelpItemsList(context, false)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    _helpLiveData.value = HelpState.Success(it)
                },
                {
                    val message = it.message ?: ""
                    _helpLiveData.value = HelpState.Error(message)
                }
            )
    }

    fun setBackupIdle() {
        _backupLiveData.value = BackupState.Idle
    }

    fun clear() {
        _userLiveData.value = null
    }

    private fun Boolean.toInt() = if (this) 1 else 0

}