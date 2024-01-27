package diarynote.settingsfragment.presentation.view.account

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import diarynote.core.common.dialog.data.DialogerImpl
import diarynote.core.utils.ROOM_BIT_NUMBER
import diarynote.core.utils.listener.OnDialogPositiveButtonClickListener
import diarynote.data.model.UserModel
import diarynote.navigator.Navigator
import diarynote.settingsfragment.R
import diarynote.settingsfragment.databinding.FragmentDeleteAccountBinding
import diarynote.settingsfragment.presentation.viewmodel.SettingsViewModel
import diarynote.data.model.state.UserState
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class DeleteAccountFragment : Fragment() {

    private var _binding: FragmentDeleteAccountBinding? = null
    private val binding get() = _binding!!
    private val settingsViewModel: SettingsViewModel by viewModel()
    private val navigator: Navigator by inject()
    private var isDeleteAccount = false
    private lateinit var userModel: UserModel

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
        observeData()
    }

    private fun initViews() = with(binding) {
        deleteAccountTextView.setOnClickListener {
            val dialoger = DialogerImpl(requireActivity(),
                object : OnDialogPositiveButtonClickListener{
                    override fun onClick() {
                        settingsViewModel.deleteCurrentUser(userModel)
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
            is UserState.Success -> handleSuccess(userState.userModel)
        }
    }

    private fun handleError(code: Int, message: String) {
        if (isDeleteAccount) {
            val dialoger = DialogerImpl(requireActivity())
            setProgressBarVisible(false)

            if((1 shl ROOM_BIT_NUMBER) and code != 0) dialoger.showAlertDialog(getString(diarynote.core.R.string.error_dialog_title_text), getString(diarynote.core.R.string.delete_account_error_message, message), getString(diarynote.core.R.string.dialog_button_ok_text))
        }
    }

    private fun setProgressBarVisible(visible: Boolean) = with(binding) {
        if (visible) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }

    private fun handleSuccess(userModel: UserModel) {
        setProgressBarVisible(false)
        if (isDeleteAccount) {
            successfullAccountDeletion()
        } else {
            this.userModel = userModel
            isDeleteAccount = true
            initViews()
        }
    }

    private fun successfullAccountDeletion() {
        val dialoger = DialogerImpl(requireActivity(),
            object : OnDialogPositiveButtonClickListener {
                override fun onClick() {
                    navigator.navigateToViewModelCleaner()
                }
            })

        dialoger.showAlertDialog(
            getString(diarynote.core.R.string.delete_account_dialog_title),
            getString(diarynote.core.R.string.delete_account_successfull_dialog_message),getString(diarynote.core.R.string.dialog_button_ok_text)
        )
    }

    private fun startMainActivity() {
        try {
            val intent = Intent()
            context?.let { intent.setClassName(it, "ru.profitsw2000.diarynote.presentation.MainActivity") }
            startActivity(intent)
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        requireActivity().viewModelStore.clear()
    }
}