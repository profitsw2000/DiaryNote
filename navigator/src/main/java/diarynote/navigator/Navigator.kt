package diarynote.navigator

import android.os.Bundle

interface Navigator {

    fun navigateToNoteCreation()

    fun actionMainToCreateNote()

    fun actionMainToReadNote(bundle: Bundle)

    fun navigateToNoteRead(bundle: Bundle)

    fun navigateToNoteEdit(bundle: Bundle)

    fun actionReadNoteToEditNote(bundle: Bundle)

    fun navigateToCategoryCreation()

    fun navigateToCategoryNotesList(bundle: Bundle)

    fun actionCategoriesToCreteCategory()

    fun actionCategoriesToCategoryNotes(bundle: Bundle)

    fun actionCategoryNotesToReadNote(bundle: Bundle)

    fun actionCalendarToReadNote(bundle: Bundle)

    fun navigateToAccountSettings()

    fun actionSettingsToAccountSettings()

    fun navigateToThemeSettings()

    fun actionSettingsToThemeSettings()

    fun navigateToLanguageSettings()

    fun actionSettingsToLanguageSettings()

    fun navigateToGeneralSettings()

    fun actionSettingsToGeneralSettings()

    fun navigateToHelpScreen()

    fun actionSettingsToHelp()

    fun navigateToAboutScreen()

    fun actionSettingsToAbout()

    fun navigateToChangePassword()

    fun actionSettingsToChangePassword()

    fun navigateToChangeUserInfo()

    fun actionSettingsToChangeUserInfo()

    fun navigateToAccountDelete()

    fun actionSettingsToDeleteAccountInfo()

    fun navigateToViewModelCleaner()

    fun navigateToUserImage()

    fun actionSettingsToUserImage()

    fun navigateToBackupRestore()

    fun actionSettingsToBackupRestore()

    fun navigateToHelpDescription(bundle: Bundle)

    fun actionHelpToHelpItemDescription(bundle: Bundle)

    fun navigateUp()
}