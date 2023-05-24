package diarynote.core.common.dialog.domain

interface Dialoger {

    fun showAlertDialog(title: String, message: String, positiveButtonText: String)

    fun showTwoButtonDialog(
        title: String,
        message: String,
        positiveButtonText: String,
        negativeButtonText: String
    )

    fun showThreeButtonDialog(
        title: String,
        message: String,
        positiveButtonText: String,
        neutralButtonText: String,
        negativeButtonText: String
    )
}