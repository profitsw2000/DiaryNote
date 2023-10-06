package diarynote.template.presentation

import android.content.Context
import android.content.Intent
import ru.profitsw2000.diarynote.presentation.MainActivity

class ActivityNavigator {

    fun startMainActivity(context: Context) : Intent {
        return Intent(context, MainActivity::class.java)
    }
}