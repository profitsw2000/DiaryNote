package diarynote.settingsfragment.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import diarynote.data.interactor.SettingsInteractor
import diarynote.data.model.SettingsMenuItemModel

class SettingsViewModel(
    private val interactor: SettingsInteractor
) {

    private val _settingsLiveData = MutableLiveData<List<SettingsMenuItemModel>>()
    val settingsLiveData: LiveData<List<SettingsMenuItemModel>> by this::_settingsLiveData

    fun getSettingsMenuItemList() {
        _settingsLiveData.value = interactor.getSettingsMenuItemsList(false)
    }

}