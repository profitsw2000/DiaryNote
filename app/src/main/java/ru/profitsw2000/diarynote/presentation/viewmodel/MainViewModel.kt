package ru.profitsw2000.diarynote.presentation.viewmodel

import android.content.SharedPreferences
import diarynote.core.viewmodel.CoreViewModel
import diarynote.data.appsettings.APP_THEME_LIGHT
import diarynote.data.appsettings.CURRENT_THEME_KEY
import diarynote.data.appsettings.DAY_INACTIVE_INDEX
import diarynote.data.appsettings.DEFAULT_THEME_KEY
import diarynote.data.appsettings.HOUR_INACTIVE_INDEX
import diarynote.data.appsettings.INACTIVE_TIME_PERIOD_INDEX_KEY
import diarynote.data.appsettings.LANGUAGE_KEY
import diarynote.data.appsettings.LAST_ENTRANCE_TIME_KEY
import diarynote.data.appsettings.MONTH_INACTIVE_INDEX
import diarynote.data.appsettings.PASSWORD_REQUIRED_KEY
import diarynote.data.appsettings.RUSSIAN_LANGUAGE_ABBR
import diarynote.data.appsettings.WEEK_INACTIVE_INDEX
import java.util.Calendar

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
        return sharedPreferences.getString(LANGUAGE_KEY, RUSSIAN_LANGUAGE_ABBR)
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