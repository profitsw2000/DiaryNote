package ru.profitsw2000.diarynote.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import diarynote.core.common.Controller
import diarynote.passwordrecovery.presentation.view.PasswordRecoveryFragment
import diarynote.registrationscreen.presentation.view.RegistrationFragment
import diarynote.signinscreen.presentation.view.SignInFragment
import ru.profitsw2000.app.R

class MainActivity : AppCompatActivity(), Controller {

    private val fragmentManager by lazy { supportFragmentManager }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val actionBar = supportActionBar
        actionBar?.let {
            it.hide()
        }

        fragmentManager.apply {
            beginTransaction()
                .replace(R.id.fragment_container, SignInFragment.newInstance())
                .commitAllowingStateLoss()
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
}