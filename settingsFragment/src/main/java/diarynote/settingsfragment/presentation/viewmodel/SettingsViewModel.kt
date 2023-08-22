package diarynote.settingsfragment.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import diarynote.core.viewmodel.CoreViewModel
import diarynote.data.interactor.SettingsInteractor
import diarynote.data.model.SettingsMenuItemModel

class SettingsViewModel(
    private val settingsInteractor: SettingsInteractor
) : CoreViewModel() {

    private val _settingsLiveData = MutableLiveData<List<SettingsMenuItemModel>>()
    val settingsLiveData: LiveData<List<SettingsMenuItemModel>> by this::_settingsLiveData

    fun getSettingsMenuItemList() {
        _settingsLiveData.value = settingsInteractor.getSettingsMenuItemsList(false)
    }

}