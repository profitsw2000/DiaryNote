package diarynote.notesactivity.presentation.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
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
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
    }
}