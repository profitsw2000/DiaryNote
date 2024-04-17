package ru.profitsw2000.diarynote.presentation.viewmodel

import android.content.SharedPreferences
import diarynote.core.viewmodel.CoreViewModel
import diarynote.data.appsettings.APP_THEME_LIGHT
import diarynote.data.appsettings.CURRENT_THEME_KEY
import diarynote.data.appsettings.DAY_INACTIVE_INDEX
import diarynote.data.appsettings.DEFAULT_THEME_KEY
import diarynote.data.appsettings.ENGLISH_LANGUAGE_ABBR
import diarynote.data.appsettings.ENGLISH_LANGUAGE_ID
import diarynote.data.appsettings.HOUR_INACTIVE_INDEX
import diarynote.data.appsettings.INACTIVE_TIME_PERIOD_INDEX_KEY
import diarynote.data.appsettings.LANGUAGE_ID_KEY
import diarynote.data.appsettings.LANGUAGE_KEY
import diarynote.data.appsettings.LAST_ENTRANCE_TIME_KEY
import diarynote.data.appsettings.MONTH_INACTIVE_INDEX
import diarynote.data.appsettings.PASSWORD_REQUIRED_KEY
import diarynote.data.appsettings.RUSSIAN_LANGUAGE_ABBR
import diarynote.data.appsettings.RUSSIAN_LANGUAGE_ID
import diarynote.data.appsettings.UNKNOWN_LANGUAGE_ABBR
import diarynote.data.appsettings.WEEK_INACTIVE_INDEX
import diarynote.data.domain.CURRENT_USER_ID
import java.util.Calendar
import java.util.Locale

private const val HOUR_IN_MILLIS: Long = 1000*60*60
private const val DAY_IN_MILLIS: Long = HOUR_IN_MILLIS*24
private const val WEEK_IN_MILLIS: Long = DAY_IN_MILLIS*7
private const val MONTH_IN_MILLIS: Long = DAY_IN_MILLIS*30

class MainViewModel(
    private val sharedPreferences: SharedPreferences
) : CoreViewModel(){

    private val calendar = Calendar.getInstance()
    fun getCurrentThemeId() : Int {
        return sharedPreferences.getInt(CURRENT_THEME_KEY, APP_THEME_LIGHT)
    }

    fun isDefaultDeviceTheme(): Boolean {
        return sharedPreferences.getBoolean(DEFAULT_THEME_KEY, false)
    }

    fun getCurrentLanguage(): String? {
        var appLanguage = sharedPreferences.getString(LANGUAGE_KEY, UNKNOWN_LANGUAGE_ABBR)

        if (appLanguage == UNKNOWN_LANGUAGE_ABBR) {
            if (Locale.getDefault().language == RUSSIAN_LANGUAGE_ABBR) {
                appLanguage = RUSSIAN_LANGUAGE_ABBR
                setCurrentLanguageId(RUSSIAN_LANGUAGE_ID)
            } else {
                appLanguage = ENGLISH_LANGUAGE_ABBR
                setCurrentLanguageId(ENGLISH_LANGUAGE_ID)
            }
        }

        return appLanguage
    }

    private fun setCurrentLanguageId(languageId: Int) {
        sharedPreferences
            .edit()
            .putInt(LANGUAGE_ID_KEY, languageId)
            .apply()
    }

    fun isPasswordRequired(): Boolean {
        return sharedPreferences.getBoolean(PASSWORD_REQUIRED_KEY, true)
    }

    private fun getCurrentInactiveTimePeriodIndex(): Int {
        return sharedPreferences.getInt(INACTIVE_TIME_PERIOD_INDEX_KEY, 0)
    }

    fun setLastEntranceTimeInMillis() {
        sharedPreferences.edit()
            .putLong(LAST_ENTRANCE_TIME_KEY, calendar.timeInMillis)
            .apply()
    }

    private fun getLastEntranceDateInMillis(): Long {
        return sharedPreferences.getLong(LAST_ENTRANCE_TIME_KEY, 0)
    }

    fun getCurrentUserId() : Int {
        return sharedPreferences.getInt(CURRENT_USER_ID, 0)
    }

    private fun getFromNowPeriodStartTime() : Long {
        val currentTime = calendar.timeInMillis

        return when(getCurrentInactiveTimePeriodIndex()) {
            HOUR_INACTIVE_INDEX -> currentTime - HOUR_IN_MILLIS
            DAY_INACTIVE_INDEX -> currentTime - DAY_IN_MILLIS
            WEEK_INACTIVE_INDEX -> currentTime - WEEK_IN_MILLIS
            MONTH_INACTIVE_INDEX -> currentTime - MONTH_IN_MILLIS
            else -> currentTime - HOUR_IN_MILLIS
        }
    }

    fun isInactivePeriodExpired(): Boolean {
        return getLastEntranceDateInMillis() < getFromNowPeriodStartTime()
    }
}