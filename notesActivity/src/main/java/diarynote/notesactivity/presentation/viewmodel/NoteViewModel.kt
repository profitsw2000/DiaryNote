package diarynote.notesactivity.presentation.viewmodel

import android.content.SharedPreferences
import diarynote.core.viewmodel.CoreViewModel
import diarynote.data.appsettings.APP_THEME_LIGHT
import diarynote.data.appsettings.CURRENT_THEME_KEY
import diarynote.data.appsettings.DEFAULT_THEME_KEY

class NoteViewModel(
    private val sharedPreferences: SharedPreferences
) : CoreViewModel() {

    fun getCurrentThemeId() : Int {
        return sharedPreferences.getInt(CURRENT_THEME_KEY, APP_THEME_LIGHT)
    }

    fun isDefaultDeviceTheme(): Boolean {
        return sharedPreferences.getBoolean(DEFAULT_THEME_KEY, false)
    }
}