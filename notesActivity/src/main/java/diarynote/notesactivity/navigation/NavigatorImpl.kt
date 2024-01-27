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

    override fun actionMainToCreateNote() {
        navController.navigate(R.id.action_main_to_create_note)
    }

    override fun actionMainToReadNote(bundle: Bundle) {
        navController.navigate(R.id.action_main_to_read_note, bundle)
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

    override fun actionCategoriesToCreteCategory() {
        navController.navigate(R.id.action_categories_to_create_category)
    }

    override fun actionCategoriesToCategoryNotes(bundle: Bundle) {
        navController.navigate(R.id.action_categories_to_category_notes, bundle)
    }

    override fun actionCategoryNotesToReadNote(bundle: Bundle) {
        navController.navigate(R.id.action_category_notes_to_read_note, bundle)
    }

    override fun actionCalendarToReadNote(bundle: Bundle) {
        navController.navigate(R.id.action_calendar_to_read_note, bundle)
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

    override fun navigateToChangePassword() {
        navController.navigate(R.id.change_password_fragment)
    }

    override fun navigateToChangeUserInfo() {
        navController.navigate(R.id.change_user_info_fragment)
    }

    override fun navigateToAccountDelete() {
        navController.navigate(R.id.delete_user_account_fragment)
    }

    override fun navigateToViewModelCleaner() {
        navController.navigate(R.id.view_model_cleaner_fragment)
    }

    override fun navigateToUserImage() {
        navController.navigate(R.id.user_image_fragment)
    }

    override fun navigateToBackupRestore() {
        navController.navigate(R.id.backup_restore_fragment)
    }

    override fun navigateToHelpDescription(bundle: Bundle) {
        navController.navigate(R.id.help_item_description_fragment, bundle)
    }

    override fun navigateUp() {
        navController.navigateUp()
    }
}
