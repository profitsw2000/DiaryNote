package diarynote.settingsfragment.presentation.view.account

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import diarynote.core.common.dialog.data.DialogerImpl
import diarynote.core.utils.EMAIL_ALREADY_EXIST_BIT_NUMBER
import diarynote.core.utils.EMAIL_BIT_NUMBER
import diarynote.core.utils.LOGIN_ALREADY_EXIST_BIT_NUMBER
import diarynote.core.utils.LOGIN_BIT_NUMBER
import diarynote.core.utils.LOGIN_MIN_LENGTH
import diarynote.core.utils.NAME_BIT_NUMBER
import diarynote.core.utils.NAME_MIN_LENGTH
import diarynote.core.utils.ROOM_BIT_NUMBER
import diarynote.core.utils.SURNAME_BIT_NUMBER
import diarynote.core.utils.listener.OnDialogPositiveButtonClickListener
import diarynote.settingsfragment.R
import diarynote.settingsfragment.databinding.FragmentDeleteAccountBinding
import diarynote.settingsfragment.presentation.viewmodel.SettingsViewModel
import diarynote.template.model.UserState
import org.koin.androidx.viewmodel.ext.android.viewModel


class DeleteAccountFragment : Fragment() {

    private var _binding: FragmentDeleteAccountBinding? = null
    private val binding get() = _binding!!
    private val settingsViewModel: SettingsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDeleteAccountBinding.bind(inflater.inflate(R.layout.fragment_delete_account, container, false))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initViews() = with(binding) {
        deleteAccountTextView.setOnClickListener {
            val dialoger = DialogerImpl(requireActivity(),
                object : OnDialogPositiveButtonClickListener{
                    override fun onClick() {
                        settingsViewModel.deleteCurrentUser()
                    }
                }
            )

            dialoger.showTwoButtonDialog(
                getString(diarynote.core.R.string.delete_account_dialog_title),
                getString(diarynote.core.R.string.delete_account_dialog_content),
                getString(diarynote.core.R.string.dialog_button_yes_text),
                getString(diarynote.core.R.string.dialog_button_no_text)
            )

        }
    }

    private fun observeData() {
        val observer = Observer<UserState?> { renderData(it) }
        settingsViewModel.userLiveData.observe(viewLifecycleOwner, observer)
    }

    private fun renderData(userState: UserState) {
        when(userState) {
            is UserState.Error -> handleError(userState.errorCode, userState.message)
            is UserState.Loading -> setProgressBarVisible(true)
            is UserState.Success -> {}
        }
    }

    private fun handleError(code: Int, message: String) = with(binding){
        val dialoger = DialogerImpl(requireActivity())
        setProgressBarVisible(false)

        if((1 shl ROOM_BIT_NUMBER) and code != 0) dialoger.showAlertDialog(getString(diarynote.core.R.string.error_dialog_title_text), getString(diarynote.core.R.string.delete_account_error_message, message), getString(diarynote.core.R.string.dialog_button_ok_text))
    }

    private fun setProgressBarVisible(visible: Boolean) = with(binding) {
        if (visible) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}