package ru.profitsw2000.diarynote.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import diarynote.signinscreen.presentation.view.SignInFragment
import ru.profitsw2000.app.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val actionBar = supportActionBar
        actionBar?.let {
            it.hide()
        }

        supportFragmentManager.apply {
            beginTransaction()
                .replace(R.id.fragment_container, SignInFragment.newInstance())
                .commitAllowingStateLoss()
        }
    }
}