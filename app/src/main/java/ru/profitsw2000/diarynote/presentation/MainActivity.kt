package ru.profitsw2000.diarynote.presentation

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import diarynote.core.common.Controller
import diarynote.data.appsettings.APP_THEME_DARK
import diarynote.data.appsettings.APP_THEME_LIGHT
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

        if (mainViewModel.isPasswordRequired() == false && !mainViewModel.isInactivePeriodExpired()) {
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
        val locale = Locale(languageToLoad)
        Locale.setDefault(locale)

        val config = baseContext.resources.configuration
        config.setLocale(locale)
        baseContext.createConfigurationContext(config)
        baseContext.resources.updateConfiguration(config, baseContext.resources.displayMetrics)
    }

    private fun getThemeStyle(): Int {
        return when(mainViewModel.getCurrentThemeId()){
            APP_THEME_LIGHT -> diarynote.core.R.style.Theme_DiaryNote
            APP_THEME_DARK -> diarynote.core.R.style.Theme_DiaryNoteDark
            else -> diarynote.core.R.style.Theme_DiaryNote
        }
    }

}