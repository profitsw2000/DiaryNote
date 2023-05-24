package diarynote.notesactivity.navigation

import android.os.Bundle
import androidx.navigation.NavController
import diarynote.navigator.Navigator
import diarynote.notesactivity.R

class NavigatorImpl(private val navController: NavController) : Navigator {

    override fun navigateToNoteCreation() {
        navController.navigate(R.id.create_note)
    }

    override fun navigateToNoteRead(bundle: Bundle) {
        navController.navigate(R.id.read_note, bundle)
    }

    override fun navigateUp() {
        navController.navigateUp()
    }
}