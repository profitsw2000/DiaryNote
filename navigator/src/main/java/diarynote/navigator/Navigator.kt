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

    fun navigateToThemeSettings()

    fun navigateToLanguageSettings()

    fun navigateToGeneralSettings()

    fun navigateToHelpScreen()

    fun navigateToAboutScreen()

    fun navigateToChangePassword()

    fun navigateToChangeUserInfo()

    fun navigateToAccountDelete()

    fun navigateToViewModelCleaner()

    fun navigateToUserImage()

    fun navigateToBackupRestore()

    fun navigateToHelpDescription(bundle: Bundle)

    fun navigateUp()
}