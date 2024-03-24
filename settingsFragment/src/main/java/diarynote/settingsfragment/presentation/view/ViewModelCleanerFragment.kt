package diarynote.settingsfragment.presentation.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import diarynote.settingsfragment.R
import diarynote.settingsfragment.presentation.viewmodel.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class ViewModelCleanerFragment : Fragment() {

    private val settingsViewModel: SettingsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setDefaultSettings()
        requireActivity().finish()
        System.exit(0)
        //startMainActivity()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_model_cleaner, container, false)
    }

    private fun startMainActivity() {
        try {
            val intent = Intent()
            context?.let { intent.setClassName(it, "ru.profitsw2000.diarynote.presentation.MainActivity") }
            activity?.finish()
            startActivity(intent)
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }
    }

    private fun setDefaultSettings() {
        settingsViewModel.setDefaultUserId()
    }
}