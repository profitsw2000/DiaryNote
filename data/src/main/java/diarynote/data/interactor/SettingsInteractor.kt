package diarynote.data.interactor

import diarynote.data.appsettings.accountSettingsMenuItemList
import diarynote.data.model.SettingsMenuItemModel
import diarynote.data.appsettings.settingsMenuItemList

class SettingsInteractor() {

    fun getSettingsMenuItemsList(remote: Boolean)  : List<SettingsMenuItemModel>{
        if (remote) return arrayListOf()
        else return  settingsMenuItemList
    }

    fun getAccountSettingsMenuItemsList(remote: Boolean)  : List<SettingsMenuItemModel>{
        if (remote) return arrayListOf()
        else return  accountSettingsMenuItemList
    }

}