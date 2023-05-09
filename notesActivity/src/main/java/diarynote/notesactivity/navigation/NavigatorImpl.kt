package diarynote.notesactivity.navigation

import androidx.navigation.NavController
import diarynote.navigator.Navigator
import diarynote.notesactivity.R

class NavigatorImpl(private val navController: NavController) : Navigator {
    override fun navigateToNoteCreation() {
        navController.navigate(R.id.create_note)
    }
}