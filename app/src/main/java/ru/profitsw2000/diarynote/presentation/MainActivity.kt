package ru.profitsw2000.diarynote.presentation

import android.content.Intent
import android.content.res.Configuration
import android.content.res.Configuration.SCREENLAYOUT_SIZE_LARGE
import android.content.res.Configuration.SCREENLAYOUT_SIZE_MASK
import android.content.res.Configuration.SCREENLAYOUT_SIZE_NORMAL
import android.content.res.Configuration.SCREENLAYOUT_SIZE_SMALL
import android.content.res.Configuration.SCREENLAYOUT_SIZE_XLARGE
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import diarynote.core.common.Controller
import diarynote.data.appsettings.APP_THEME_DARK
import diarynote.data.appsettings.APP_THEME_LIGHT
import diarynote.data.appsettings.UNKNOWN_LANGUAGE_ABBR
import diarynote.notesactivity.presentation.view.NoteActivity
import diarynote.passwordrecovery.presentation.view.PasswordRecoveryFragment
import diarynote.registrationscreen.presentation.view.RegistrationFragment
import diarynote.signinscreen.presentation.view.SignInFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.profitsw2000.app.R
import ru.profitsw2000.diarynote.presentation.viewmodel.MainViewModel
import java.util.Locale

class MainActivity : AppCompatActivity(), Controller {

    private val fragmentManager by lazy { supportFragmentManager }
    private val mainViewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        if(!mainViewModel.isDefaultDeviceTheme()) setTheme(getThemeStyle())
        setLanguage()
        super.onCreate(savedInstanceState)

        if (!mainViewModel.isPasswordRequired() && !mainViewModel.isInactivePeriodExpired() && mainViewModel.getCurrentUserId() != 0) {
            startNotesActivity()
        } else {
            setContentView(R.layout.activity_main)

            val actionBar = supportActionBar
            actionBar?.hide()

            fragmentManager.apply {
                beginTransaction()
                    .replace(R.id.fragment_container, SignInFragment.newInstance())
                    .commitAllowingStateLoss()
            }
        }
    }

    override fun openRegistrationFragment() {
        fragmentManager.apply {
            beginTransaction()
                .setCustomAnimations(
                    diarynote.core.R.anim.slide_in,
                    diarynote.core.R.anim.fade_out,
                    diarynote.core.R.anim.fade_in,
                    diarynote.core.R.anim.slide_out
                )
                .replace(R.id.fragment_container, RegistrationFragment.newInstance())
                .addToBackStack("")
                .commitAllowingStateLoss()
        }
    }

    override fun openPasswordRecoveryFragment() {
        fragmentManager.apply {
            beginTransaction()
                .setCustomAnimations(
                    diarynote.core.R.anim.slide_in,
                    diarynote.core.R.anim.fade_out,
                    diarynote.core.R.anim.fade_in,
                    diarynote.core.R.anim.slide_out
                )
                .replace(R.id.fragment_container, PasswordRecoveryFragment.newInstance())
                .addToBackStack("")
                .commitAllowingStateLoss()
        }
    }

    override fun startNotesActivity() {
        mainViewModel.setLastEntranceTimeInMillis()
        val intent = Intent(this, NoteActivity::class.java)
        startActivity(intent)
    }

    private fun setLanguage() {
        val languageToLoad = mainViewModel.getCurrentLanguage()
        val locale = languageToLoad?.let { Locale(it) }
        if (locale != null) {
            Locale.setDefault(locale)
        }

        val config = baseContext.resources.configuration
        config.setLocale(locale)
        baseContext.createConfigurationContext(config)
        baseContext.resources.updateConfiguration(config, baseContext.resources.displayMetrics)
    }

    private fun getThemeStyle(): Int {
        return when(mainViewModel.getCurrentThemeId()){
            APP_THEME_LIGHT -> getLightTheme()
            APP_THEME_DARK -> getDarkTheme()
            else -> diarynote.core.R.style.Theme_DiaryNote
        }
    }

    private fun getLightTheme(): Int {
        return when(baseContext.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
            Configuration.UI_MODE_NIGHT_YES -> diarynote.core.R.style.Theme_DiaryNoteDark
            Configuration.UI_MODE_NIGHT_NO -> diarynote.core.R.style.Theme_DiaryNote
            else -> diarynote.core.R.style.Theme_DiaryNote
        }
    }

    private fun getDarkTheme(): Int {
        return when(baseContext.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
            Configuration.UI_MODE_NIGHT_YES -> diarynote.core.R.style.Theme_DiaryNote
            Configuration.UI_MODE_NIGHT_NO -> diarynote.core.R.style.Theme_DiaryNoteDark
            else -> diarynote.core.R.style.Theme_DiaryNote
        }
    }
}