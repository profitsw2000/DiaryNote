package diarynote.settingsfragment.presentation.view.account

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import diarynote.core.common.dialog.data.DialogerImpl
import diarynote.core.utils.listener.OnDialogPositiveButtonClickListener
import diarynote.navigator.Navigator
import diarynote.settingsfragment.R
import diarynote.settingsfragment.databinding.FragmentChangePasswordBinding
import diarynote.settingsfragment.presentation.viewmodel.SettingsViewModel
import diarynote.template.model.UserState
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChangePasswordFragment : Fragment() {

    private var _binding: FragmentChangePasswordBinding? = null
    private val binding get() = _binding!!
    private val settingsViewModel: SettingsViewModel by viewModel()
    private val navigator: Navigator by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentChangePasswordBinding.bind(inflater.inflate(R.layout.fragment_change_password, container, false))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()

    }

    private fun initViews() = with(binding) {
        changePasswordButton.setOnClickListener {
            settingsViewModel.changeUserPassword(
                currentPasswordEditText.text.toString(),
                passwordInputEditText.text.toString(),
                confirmPasswordInputEditText.text.toString()
            )
        }
    }

    private fun observeData() {
        val observer = Observer<UserState?>{ renderData(it) }
        settingsViewModel.userLiveData.observe(viewLifecycleOwner, observer)
    }

    private fun renderData(userState: UserState) {
        when(userState) {
            is UserState.Error -> handleError(userState.errorCode)
            is UserState.Loading -> setProgressBarVisible(true)
            is UserState.Success -> successfullPasswordChanging()
        }
    }

    private fun handleError(code: Int) {
        val dialoger = DialogerImpl(requireActivity())

        setProgressBarVisible(false)


    }

    private fun setProgressBarVisible(visible: Boolean) = with(binding) {
        if (visible) {
            progressBar.visibility = View.VISIBLE
            mainGroup.visibility = View.GONE
        } else {
            progressBar.visibility = View.GONE
            mainGroup.visibility = View.VISIBLE
        }
    }

    private fun successfullPasswordChanging() {
        val dialoger = DialogerImpl(requireActivity(),
            object : OnDialogPositiveButtonClickListener{
                override fun onClick() {
                    settingsViewModel.clear()
                    requireActivity().onBackPressed()
                }

            })

        setProgressBarVisible(false)
        dialoger.showAlertDialog("Изменение пароля",
            "Изменение пароля завершено успешно",getString(diarynote.core.R.string.dialog_button_ok_text)
            )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}