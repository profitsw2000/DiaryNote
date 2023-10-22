package diarynote.settingsfragment.presentation.view.account

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import androidx.room.Database
import diarynote.data.room.database.AppDatabase
import diarynote.settingsfragment.R
import diarynote.settingsfragment.databinding.FragmentBackupRestoreBinding
import diarynote.settingsfragment.presentation.viewmodel.SettingsViewModel
import diarynote.template.model.UserState
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

private const val DEFAULT_EXPORT_TITLE = "BackupDatabase.db"

class BackupRestoreFragment : Fragment() {

    private var _binding: FragmentBackupRestoreBinding? = null
    private val binding get() = _binding!!
    private val settingsViewModel: SettingsViewModel by viewModel()
    private val createFile = registerForActivityResult(ActivityResultContracts.CreateDocument()) {
        if (it != null) {
            settingsViewModel.exportDB(it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentBackupRestoreBinding.bind(inflater.inflate(R.layout.fragment_backup_restore, container, false))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observeData()
    }

    private fun initViews() {
        binding.createBackupButton.setOnClickListener {
            createFile.launch(DEFAULT_EXPORT_TITLE)
        }
    }

    private fun observeData() {
        val observer = Observer<UserState?> { renderData(it) }
        settingsViewModel.userLiveData.observe(viewLifecycleOwner, observer)
    }

    private fun renderData(userState: UserState) {
        when(userState) {
            is UserState.Error -> TODO()
            UserState.Loading -> TODO()
            is UserState.Success -> TODO()
        }
    }

}