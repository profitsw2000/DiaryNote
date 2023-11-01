package diarynote.core.common.dialog.data

import android.app.Activity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import diarynote.core.common.dialog.domain.Dialoger
import diarynote.core.utils.listener.OnDialogItemClickListener
import diarynote.core.utils.listener.OnDialogNeutralButtonClickListener
import diarynote.core.utils.listener.OnDialogPositiveButtonClickListener

class DialogerImpl(
    private val activity: Activity,
    private val onDialogPositiveButtonClickListener: OnDialogPositiveButtonClickListener?,
    private val onDialogNeutralButtonClickListener: OnDialogNeutralButtonClickListener?,
    private val onDialogItemClickListener: OnDialogItemClickListener?
) : Dialoger {

    constructor(
        activity: Activity,
        onDialogPositiveButtonClickListener: OnDialogPositiveButtonClickListener
    ) : this(activity, onDialogPositiveButtonClickListener, null, null)

    constructor(
        activity: Activity
    ) : this(activity, null, null, null)

    constructor(
        activity: Activity,
        onDialogItemClickListener: OnDialogItemClickListener?
    ) : this(activity, null, null, onDialogItemClickListener)

    override fun showAlertDialog(title: String, message: String, positiveButtonText: String) {
        MaterialAlertDialogBuilder(activity)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(positiveButtonText) { dialog, _ ->
                onDialogPositiveButtonClickListener?.onClick()
                dialog.dismiss() }
            .create()
            .show()
    }

    override fun showTwoButtonDialog(
        title: String,
        message: String,
        positiveButtonText: String,
        negativeButtonText: String
    ) {
        MaterialAlertDialogBuilder(activity)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(positiveButtonText) { _, _ -> onDialogPositiveButtonClickListener?.onClick() }
            .setNegativeButton(negativeButtonText) { dialog, _ -> dialog.dismiss() }
            .create()
            .show()
    }

    override fun showThreeButtonDialog(
        title: String,
        message: String,
        positiveButtonText: String,
        neutralButtonText: String,
        negativeButtonText: String
    ) {
        MaterialAlertDialogBuilder(activity)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(positiveButtonText) { _, _ -> onDialogPositiveButtonClickListener?.onClick() }
            .setNeutralButton(neutralButtonText) { _, _ -> onDialogNeutralButtonClickListener?.onClick() }
            .setNegativeButton(negativeButtonText) { dialog, _ -> dialog.dismiss() }
            .create()
            .show()
    }

    override fun showDialogWithSingleChoice(
        title: String,
        negativeButtonText: String,
        itemList: Array<String>?,
        selectedItem: Int?
    ) {
        if (selectedItem != null) {
            MaterialAlertDialogBuilder(activity)
                .setTitle(title)
                .setSingleChoiceItems(itemList, selectedItem) { dialog, which ->
                    onDialogItemClickListener?.onItemClick(itemList?.get(which))
                    dialog.dismiss()
                }
                .setNegativeButton(negativeButtonText) { dialog, _ -> dialog.dismiss() }
                .create()
                .show()
        }
    }

}