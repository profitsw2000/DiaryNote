package diarynote.notesactivity.presentation.viewmodel

import android.content.SharedPreferences
import diarynote.core.viewmodel.CoreViewModel
import diarynote.data.appsettings.APP_THEME_LIGHT
import diarynote.data.appsettings.CURRENT_THEME_KEY
import diarynote.data.appsettings.DEFAULT_THEME_KEY
import diarynote.data.appsettings.ENGLISH_LANGUAGE_ABBR
import diarynote.data.appsettings.ENGLISH_LANGUAGE_ID
import diarynote.data.appsettings.LANGUAGE_ID_KEY
import diarynote.data.appsettings.LANGUAGE_KEY
import diarynote.data.appsettings.RUSSIAN_LANGUAGE_ABBR
import diarynote.data.appsettings.RUSSIAN_LANGUAGE_ID
import diarynote.data.appsettings.UNKNOWN_LANGUAGE_ABBR
import java.util.Locale

class NoteViewModel(
    private val sharedPreferences: SharedPreferences
) : CoreViewModel() {

    fun getCurrentThemeId() : Int {
        return sharedPreferences.getInt(CURRENT_THEME_KEY, APP_THEME_LIGHT)
    }

    fun isDefaultDeviceTheme(): Boolean {
        return sharedPreferences.getBoolean(DEFAULT_THEME_KEY, false)
    }

    fun getCurrentLanguage() : String? {
        return sharedPreferences.getString(LANGUAGE_KEY, RUSSIAN_LANGUAGE_ABBR)
    }
}