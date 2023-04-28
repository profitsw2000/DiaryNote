package diarynote.core.common.view

import android.app.Activity
import androidx.appcompat.app.AlertDialog

class Dialoger(private val activity: Activity) {

    fun showAlertDialog(title: String, message: String) {
        AlertDialog.Builder(activity)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .create()
            .show()
    }

}