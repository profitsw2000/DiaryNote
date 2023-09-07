package diarynote.notesactivity.presentation.view

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Config
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
import diarynote.data.appsettings.DEFAULT_THEME_KEY
import diarynote.navigator.Navigator
import diarynote.notesactivity.R
import diarynote.notesactivity.databinding.ActivityNoteBinding
import diarynote.notesactivity.navigation.NavigatorImpl
import diarynote.notesactivity.presentation.viewmodel.NoteViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module
import java.util.Locale

class NoteActivity : AppCompatActivity() {

    private var _binding: ActivityNoteBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private val noteViewModel: NoteViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        if(!noteViewModel.isDefaultDeviceTheme()) setTheme(getThemeStyle())
        super.onCreate(savedInstanceState)
        setLanguage()
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

    private fun getThemeStyle(): Int {
        return when(noteViewModel.getCurrentThemeId()){
            APP_THEME_LIGHT -> diarynote.core.R.style.Theme_DiaryNote
            APP_THEME_DARK -> diarynote.core.R.style.Theme_DiaryNoteDark
            else -> diarynote.core.R.style.Theme_DiaryNote
        }
    }

    private fun setLanguage() {
        val languageToLoad = noteViewModel.getCurrentLanguage()
        val locale = Locale(languageToLoad)
        Locale.setDefault(locale)

        val config = baseContext.resources.configuration
        config.setLocale(locale)
        baseContext.createConfigurationContext(config)
        baseContext.resources.updateConfiguration(config, baseContext.resources.displayMetrics)
    }
}