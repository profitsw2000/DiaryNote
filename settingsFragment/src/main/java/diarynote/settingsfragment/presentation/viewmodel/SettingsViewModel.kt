package diarynote.settingsfragment.presentation.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import diarynote.core.viewmodel.CoreViewModel
import diarynote.data.appsettings.APP_THEME_LIGHT
import diarynote.data.appsettings.CURRENT_THEME_KEY
import diarynote.data.domain.CURRENT_USER_ID
import diarynote.data.domain.ROOM_ERROR_CODE
import diarynote.data.interactor.NoteInteractor
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

    fun getSettingsMenuItemList() {
        _settingsLiveData.value = settingsInteractor.getSettingsMenuItemsList(false)
    }

    fun getAccountSettingsMenuItemList() {
        _settingsLiveData.value = settingsInteractor.getAccountSettingsMenuItemsList(false)
    }

    fun getCurrentUserInfo(){
        getUserInfoById(sharedPreferences.getInt(CURRENT_USER_ID, 0))
    }

    fun getCurrentTheme(): Int {
        return sharedPreferences.getInt(CURRENT_THEME_KEY, APP_THEME_LIGHT)
    }

    fun setCurrentTheme(theme: Int) {
        sharedPreferences
            .edit()
            .putInt(CURRENT_THEME_KEY, theme)
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

}