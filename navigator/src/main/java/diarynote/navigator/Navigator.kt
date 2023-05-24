package diarynote.navigator

import android.os.Bundle

interface Navigator {

    fun navigateToNoteCreation()

    fun navigateToNoteRead(bundle: Bundle)

    fun navigateUp()
}