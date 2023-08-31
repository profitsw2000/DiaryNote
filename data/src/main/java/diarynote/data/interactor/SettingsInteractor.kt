package diarynote.data.interactor

import diarynote.data.model.SettingsMenuItemModel
import diarynote.data.settingsMenuItemList

class SettingsInteractor() {

    fun getSettingsMenuItemsList(remote: Boolean)  : List<SettingsMenuItemModel>{
        if (remote) return arrayListOf()
        else return  settingsMenuItemList
    }

}