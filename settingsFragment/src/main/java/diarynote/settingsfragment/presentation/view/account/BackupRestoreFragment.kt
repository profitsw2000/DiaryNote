package diarynote.settingsfragment.presentation.view.account

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import diarynote.settingsfragment.R
import diarynote.settingsfragment.databinding.FragmentBackupRestoreBinding

class BackupRestoreFragment : Fragment() {

    private var _binding: FragmentBackupRestoreBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentBackupRestoreBinding.bind(inflater.inflate(R.layout.fragment_backup_restore, container, false))
        return binding.root
    }


}