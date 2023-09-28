package diarynote.core.view

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.google.android.material.snackbar.Snackbar
import diarynote.core.viewmodel.CoreViewModel

open class CoreFragment(layoutRes: Int) : Fragment(layoutRes) {

    protected open val viewModel: CoreViewModel? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel?.let { viewLifecycleOwner.lifecycle.addObserver(it) }
    }

    fun hideKeyboard(context: FragmentActivity) {
        val view = context.currentFocus
        view?.let {
            val manager = ContextCompat.getSystemService(context, InputMethodManager::class.java)
            manager?.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    fun makeSnackbar(
        view: View,
        text: String = "",
        actionText: String = "",
        action: (View) -> Unit = {},
        length: Int = Snackbar.LENGTH_LONG,
        anchor: View? = null
    ) {
        Snackbar
            .make(view, text, length)
            .setAction(actionText, action)
            .setAnchorView(anchor)
/*            .setBackgroundTint(resources.getColor(R.color.grey_800))
            .setTextColor(resources.getColor(R.color.white))*/
            .show()
    }

    fun showDialog(
        context: Context,
        title: String,
        message: String,
        positiveButtonText: String = ""
    ) {
        AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(positiveButtonText)
            { dialog, _ -> dialog.dismiss() }
            .create()
            .show()
    }
}