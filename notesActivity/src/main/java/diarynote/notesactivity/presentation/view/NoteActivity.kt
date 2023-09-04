package diarynote.notesactivity.presentation.view

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import diarynote.core.utils.SHARED_PREFERENCE_NAME
import diarynote.data.appsettings.APP_THEME_DARK
import diarynote.data.appsettings.APP_THEME_LIGHT
import diarynote.data.appsettings.CURRENT_THEME_KEY
import diarynote.navigator.Navigator
import diarynote.notesactivity.R
import diarynote.notesactivity.databinding.ActivityNoteBinding
import diarynote.notesactivity.navigation.NavigatorImpl
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

class NoteActivity : AppCompatActivity() {

    private var _binding: ActivityNoteBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(getThemeStyle())
        super.onCreate(savedInstanceState)
        _binding = ActivityNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(
            R.id.fragment_container
        ) as NavHostFragment

        navController = navHostFragment.navController
        loadKoinModules(module {single<Navigator> { NavigatorImpl(navController) }})
        binding.bottomNav.setupWithNavController(navController)

        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.main, R.id.categories, R.id.calendar, R.id.settings)
        )
        setupActionBarWithNavController(navController, appBarConfiguration)

        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(false)

        navController.addOnDestinationChangedListener{ _, destination, _ ->
            if (destination.id == R.id.main ||
                destination.id == R.id.categories ||
                destination.id == R.id.calendar ||
                destination.id == R.id.settings) {
                binding.bottomNav.visibility = View.VISIBLE
                actionBar?.let {
                    it.hide()
                }
            } else {
                binding.bottomNav.visibility = View.GONE
                actionBar?.let {
                    it.show()
                    it.setDisplayHomeAsUpEnabled(true)
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
    }

    private fun getCurrentThemeId() : Int {
        val sharedPreferences = getSharedPreferences(SHARED_PREFERENCE_NAME, MODE_PRIVATE)
        return sharedPreferences.getInt(CURRENT_THEME_KEY, APP_THEME_LIGHT)
    }

    fun getThemeStyle(): Int {
        return when(getCurrentThemeId()){
            APP_THEME_LIGHT -> diarynote.core.R.style.Theme_DiaryNote
            APP_THEME_DARK -> diarynote.core.R.style.Theme_DiaryNoteDark
            else -> diarynote.core.R.style.Theme_DiaryNote
        }
    }
}