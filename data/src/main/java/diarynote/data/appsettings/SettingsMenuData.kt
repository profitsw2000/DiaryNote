package diarynote.data.appsettings

import diarynote.data.model.HelpItemModel
import diarynote.data.model.SettingsMenuItemModel

fun createSettingsMenuList(itemIdList: List<Int>, itemNameList: Array<String>): List<SettingsMenuItemModel> {
    val settingsMenuItemList = mutableListOf<SettingsMenuItemModel>()
    if (itemIdList.size < itemNameList.size) {
        itemIdList.forEachIndexed { index, i ->
            settingsMenuItemList.add(SettingsMenuItemModel(i, itemNameList[index]))
        }
    } else {
        itemNameList.forEachIndexed { index, i ->
            settingsMenuItemList.add(SettingsMenuItemModel(itemIdList[index], i))
        }
    }

    return settingsMenuItemList
}

fun createHelpItemsList(itemNameList: Array<String>, itemDescriptionList: Array<String>): List<HelpItemModel> {

    return mutableListOf<HelpItemModel>().apply {
        itemNameList.forEachIndexed { index, itemName ->
            this.add(HelpItemModel(index, itemName, itemDescriptionList[index]))
        }
    }
}

val settingsIdList = listOf(
    SETTINGS_ACCOUNT_ID,
    SETTINGS_THEME_ID,
    SETTINGS_LANGUAGE_ID,
    SETTINGS_GENERAL_ID,
    SETTINGS_HELP_ID,
    SETTINGS_ABOUT_ID
)

val accountSettingsIdList = listOf(
    ACCOUNT_CHANGE_PASSWORD_ID,
    ACCOUNT_PROFILE_PHOTO_ID,
    ACCOUNT_CHANGE_ACCOUNT_INFO_ID,
    DELETE_ACCOUNT_ID,
    ACCOUNT_BACKUP_ID
)

val appLanguageIdList = listOf(
    RUSSIAN_LANGUAGE_ID,
    ENGLISH_LANGUAGE_ID
)