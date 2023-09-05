package diarynote.data.appsettings

import diarynote.data.model.SettingsMenuItemModel

val settingsMenuItemList = listOf(
    SettingsMenuItemModel(
        itemId = SETTINGS_ACCOUNT_ID,
        itemName = "Учетная запись"
    ),
    SettingsMenuItemModel(
        itemId = SETTINGS_THEME_ID,
        itemName = "Тема"
    ),
    SettingsMenuItemModel(
        itemId = SETTINGS_LANGUAGE_ID,
        itemName = "Язык"
    ),
    SettingsMenuItemModel(
        itemId = SETTINGS_GENERAL_ID,
        itemName = "Общие"
    ),
    SettingsMenuItemModel(
        itemId = SETTINGS_HELP_ID,
        itemName = "Помощь"
    ),
    SettingsMenuItemModel(
        itemId = SETTINGS_ABOUT_ID,
        itemName = "О приложении"
    )
)

val accountSettingsMenuItemList = listOf(
    SettingsMenuItemModel(
        itemId = ACCOUNT_CHANGE_PASSWORD_ID,
        itemName = "Изменить пароль"
    ),
    SettingsMenuItemModel(
        itemId = ACCOUNT_PROFILE_PHOTO_ID,
        itemName = "Фото профиля"
    ),
    SettingsMenuItemModel(
        itemId = ACCOUNT_CHANGE_ACCOUNT_INFO_ID,
        itemName = "Изменить данные аккаунта"
    ),
    SettingsMenuItemModel(
        itemId = DELETE_ACCOUNT_ID,
        itemName = "Удалить учетную запись"
    ),
    SettingsMenuItemModel(
        itemId = ACCOUNT_BACKUP_ID,
        itemName = "Резервная копия"
    )
)

val appLanguageList = listOf(
    SettingsMenuItemModel(
        itemId = RUSSIAN_LANGUAGE_ID,
        itemName = "Русский"
    ),
    SettingsMenuItemModel(
        itemId = ENGLISH_LANGUAGE_ID,
        itemName = "English"
    )
)