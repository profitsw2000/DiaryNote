package diarynote.settingsfragment.presentation.viewmodel

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import android.webkit.MimeTypeMap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import diarynote.core.utils.BACKUP_BIT_NUMBER
import diarynote.core.utils.CONFIRM_PASSWORD_BIT_NUMBER
import diarynote.core.utils.CURRENT_PASSWORD_BIT_NUMBER
import diarynote.core.utils.DB_FILE_OPEN_ERROR
import diarynote.core.utils.EMAIL_ALREADY_EXIST_BIT_NUMBER
import diarynote.core.utils.EMAIL_BIT_NUMBER
import diarynote.core.utils.EMAIL_PATTERN
import diarynote.core.utils.INVALID_FILE_EXTENSION_BIT_NUMBER
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
import diarynote.data.appsettings.TAGS_SEARCH_PRIORITY_KEY
import diarynote.data.appsettings.TEXT_SEARCH_PRIORITY_KEY
import diarynote.data.appsettings.TITLE_SEARCH_PRIORITY_KEY
import diarynote.data.domain.CURRENT_USER_ID
import diarynote.data.domain.ROOM_ERROR_CODE
import diarynote.data.interactor.SettingsInteractor
import diarynote.data.interactor.UserInteractor
import diarynote.data.mappers.UserMapper
import diarynote.data.model.SettingsMenuItemModel
import diarynote.data.model.UserModel
import diarynote.data.room.entity.UserEntity
import diarynote.data.model.state.BackupState
import diarynote.data.model.state.CopyFileState
import diarynote.data.model.state.HelpState
import diarynote.data.model.state.UserState
import diarynote.data.room.utils.SQLCipherUtils
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import java.io.File
import java.lang.Exception

class SettingsViewModel(
    private val settingsInteractor: SettingsInteractor,
    private val userInteractor: UserInteractor,
    private val sharedPreferences: SharedPreferences,
    private val userMapper: UserMapper
) : CoreViewModel() {

    private val _settingsLiveData = MutableLiveData<List<SettingsMenuItemModel>>()
    val settingsLiveData: LiveData<List<SettingsMenuItemModel>> by this::_settingsLiveData

    private val _userLiveData = MutableLiveData<UserState?>()
    val userLiveData: LiveData<UserState?> by this::_userLiveData

    private val _backupLiveData = MutableLiveData<BackupState?>()
    val backupLiveData: LiveData<BackupState?> by this::_backupLiveData

    private val _helpLiveData = MutableLiveData<HelpState?>()
    val helpLiveData: LiveData<HelpState?> by this::_helpLiveData

    private val _copyFileLiveData = MutableLiveData<CopyFileState?>()
    val copyFileLiveData: LiveData<CopyFileState?> by this::_copyFileLiveData

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
            .addViewLifeCycle()
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

    fun saveSearchPriorityList(
        fieldsList: List<String>,
        searchPriorityStringsList: List<String>
    ) {
        val searchPriorityNumbersList = mutableListOf<Int>()

        for (j in 0..(fieldsList.size - 1)){
            searchPriorityStringsList.forEachIndexed { index, i ->
                if (i == fieldsList[j]) {
                    searchPriorityNumbersList.add(index)
                    return@forEachIndexed
                }
            }
        }
        setSearchPriorityNumbersList(searchPriorityNumbersList)
    }

    private fun setSearchPriorityNumbersList(prioritySearchList: List<Int>) {
        //check all elements of list are unique
        if (prioritySearchList.size == prioritySearchList.toSet().size) {
            sharedPreferences
                .edit()
                .putInt(TITLE_SEARCH_PRIORITY_KEY, prioritySearchList[0])
                .putInt(TEXT_SEARCH_PRIORITY_KEY, prioritySearchList[1])
                .putInt(TAGS_SEARCH_PRIORITY_KEY, prioritySearchList[2])
                .apply()
        }
    }

    fun getSearchPriorityStringsList(fieldsList: List<String>): List<String> {
        val searchPriorityStringsList = mutableListOf<String>()

        for (j in 0..(fieldsList.size - 1)){
            getSearchPriorityNumbersList().forEachIndexed { index, i ->
                if (i == j) {
                    searchPriorityStringsList.add(j, fieldsList[index])
                    return@forEachIndexed
                }
            }
        }
        return searchPriorityStringsList
    }

    private fun getSearchPriorityNumbersList() : List<Int> {
        val titleSearchPriority = sharedPreferences.getInt(TITLE_SEARCH_PRIORITY_KEY, 1)
        val textSearchPriority = sharedPreferences.getInt(TEXT_SEARCH_PRIORITY_KEY, 2)
        val tagsSearchPriority = sharedPreferences.getInt(TAGS_SEARCH_PRIORITY_KEY, 0)

        return if ((titleSearchPriority != textSearchPriority) &&
            (titleSearchPriority != tagsSearchPriority) &&
            (textSearchPriority != titleSearchPriority)) {
            arrayListOf(titleSearchPriority, textSearchPriority, tagsSearchPriority)
        } else {
            arrayListOf(1, 2, 0)
        }
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

    /**
     * Проверка пароля на соответствие требованиям - не менее 8 цифровых или буквенных символов и
     * правильно подтвержденный пароль
     * @return число, содержащее код результата проверки
     */
    fun backupPasswordEncryptionValidationCode(enteredPassword: String, confirmedPassword: String): Int {
        return ((!InputValidator().checkInputIsValid(enteredPassword, PASSWORD_MIN_LENGTH, PASSWORD_PATTERN)).toInt() shl PASSWORD_BIT_NUMBER) or
                ((!(enteredPassword == confirmedPassword)).toInt() shl CONFIRM_PASSWORD_BIT_NUMBER)
    }

    /**
     * Проверка пароля на соответствие требованиям - не менее 8 цифровых или буквенных символов и
     * правильно подтвержденный пароль
     * @return число, содержащее код результата проверки
     */
    fun recoveryPasswordValidationCode(enteredPassword: String): Int {
        return (!InputValidator().checkInputIsValid(enteredPassword, PASSWORD_MIN_LENGTH, PASSWORD_PATTERN)).toInt() shl PASSWORD_BIT_NUMBER
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
            .addViewLifeCycle()
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

    fun copyFile(sourcePath: String, targetPath: String) {
        val sourceFile = File(sourcePath)
        val targetFile = File(targetPath)

        _copyFileLiveData.value = CopyFileState.Copying
        try {
            sourceFile.copyTo(targetFile, true)
            _copyFileLiveData.value = CopyFileState.Success(targetPath)
        } catch (exception: Exception) {
            _copyFileLiveData.value = CopyFileState.Error(exception.message.toString())
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
            .addViewLifeCycle()
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
            .addViewLifeCycle()
    }

    fun importDB(uri: Uri) {
        _backupLiveData.value = BackupState.Loading
        settingsInteractor.importDecryptedDB(uri)
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
            .addViewLifeCycle()
    }

    fun importEncryptedDB(uri: Uri, backupPassword: String) {
        _backupLiveData.value = BackupState.Loading
        settingsInteractor.importEncryptedDB(uri, backupPassword)
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
            .addViewLifeCycle()
    }

    fun exportDB(uri: Uri, backupPassword: String) {
        _backupLiveData.value = BackupState.Loading
        if (backupPassword.isNullOrEmpty()) {
            exportDecryptedDB(uri)
        } else {
            exportEncryptedDB(uri, backupPassword)
        }
    }

    private fun exportEncryptedDB(uri: Uri, backupPassword: String) {
        settingsInteractor.exportDB(uri, backupPassword)
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
            .addViewLifeCycle()
    }

    private fun exportDecryptedDB(uri: Uri) {
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
            .addViewLifeCycle()
    }

    //Check encryption of database
    fun checkPickedFile(uri: Uri) {
        settingsInteractor.checkPickedFile(uri)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    _backupLiveData.value = BackupState.DbState(it, uri)
                },
                {
                    val message = it.message ?: ""
                    _backupLiveData.value = BackupState.Error(message, (1 shl DB_FILE_OPEN_ERROR))
                }
            )
            .addViewLifeCycle()
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
            .addViewLifeCycle()
    }

    fun setBackupIdle() {
        _backupLiveData.value = BackupState.Idle
    }

    private fun Boolean.toInt() = if (this) 1 else 0

}