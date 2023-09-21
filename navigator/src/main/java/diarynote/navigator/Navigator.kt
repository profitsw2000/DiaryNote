package diarynote.navigator

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

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

    fun navigateUp()
}