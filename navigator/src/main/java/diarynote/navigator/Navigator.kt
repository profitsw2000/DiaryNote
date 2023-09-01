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

    fun navigateUp()
}