package diarynote.navigator

import android.os.Bundle

interface Navigator {

    fun navigateToNoteCreation()

    fun navigateToNoteRead(bundle: Bundle)

    fun navigateToNoteEdit(bundle: Bundle)

    fun navigateToCategoryCreation()

    fun navigateToCategoryNotesList(bundle: Bundle)

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