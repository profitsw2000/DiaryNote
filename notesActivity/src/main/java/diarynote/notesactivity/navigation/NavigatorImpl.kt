package diarynote.notesactivity.navigation

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import diarynote.navigator.Navigator
import diarynote.notesactivity.R

class NavigatorImpl(private val navController: NavController) : Navigator {

    override fun navigateToNoteCreation() {
        navController.navigate(R.id.create_note)
    }

    override fun navigateToNoteRead(bundle: Bundle) {
        navController.navigate(R.id.read_note, bundle)
    }

    override fun navigateToNoteEdit(bundle: Bundle) {
        val navOptions = NavOptions.Builder().setPopUpTo(R.id.read_note, true).build()
        navController.navigate(R.id.edit_note, bundle, navOptions)
    }

    override fun navigateToCategoryCreation() {
        navController.navigate(R.id.add_category)
    }

    override fun navigateToCategoryNotesList(bundle: Bundle) {
        navController.navigate(R.id.category_notes, bundle)
    }

    override fun navigateToAccountSettings() {
        navController.navigate(R.id.account_settings)
    }

    override fun navigateToThemeSettings() {
        navController.navigate(R.id.theme_settings)
    }

    override fun navigateToLanguageSettings() {
        navController.navigate(R.id.language_settings)
    }

    override fun navigateToGeneralSettings() {
        navController.navigate(R.id.general_settings)
    }

    override fun navigateToHelpScreen() {
        navController.navigate(R.id.help)
    }

    override fun navigateToAboutScreen() {
        navController.navigate(R.id.about)
    }

    override fun navigateUp() {
        navController.navigateUp()
    }
}