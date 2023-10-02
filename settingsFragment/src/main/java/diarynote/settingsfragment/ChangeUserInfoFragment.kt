package diarynote.settingsfragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import diarynote.settingsfragment.databinding.FragmentChangeUserInfoBinding
import diarynote.settingsfragment.presentation.viewmodel.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChangeUserInfoFragment : Fragment() {

    private var _binding: FragmentChangeUserInfoBinding? = null
    private val binding get() = _binding!!
    private val settingsViewModel: SettingsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentChangeUserInfoBinding.bind(inflater.inflate(R.layout.fragment_change_user_info, container, false))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observeData()
        settingsViewModel.getCurrentUserInfo()
    }

    private fun initViews() {

    }

    private fun observeData() {

    }
}