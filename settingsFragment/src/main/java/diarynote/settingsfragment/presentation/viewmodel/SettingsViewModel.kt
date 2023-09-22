package diarynote.settingsfragment.presentation.viewmodel

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
import diarynote.template.model.UserState
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class SettingsViewModel(
    private val settingsInteractor: SettingsInteractor,
    private val userInteractor: UserInteractor,
    private val sharedPreferences: SharedPreferences,
    private val userMapper: UserMapper
) : CoreViewModel() {

    private val _settingsLiveData = MutableLiveData<List<SettingsMenuItemModel>>()
    val settingsLiveData: LiveData<List<SettingsMenuItemModel>> by this::_settingsLiveData

    private val _userLiveData = MutableLiveData<UserState>()
    val userLiveData: LiveData<UserState> by this::_userLiveData

    fun getSettingsMenuItemList(context: Context) {
        _settingsLiveData.value = settingsInteractor.getSettingsMenuItemsList(context,false)
    }

    fun getAccountSettingsMenuItemList(context: Context) {
        _settingsLiveData.value = settingsInteractor.getAccountSettingsMenuItemsList(context,false)
    }

    fun getCurrentUserInfo(){
        getUserInfoById(sharedPreferences.getInt(CURRENT_USER_ID, 0))
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

}