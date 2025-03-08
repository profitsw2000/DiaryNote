package diarynote.settingsfragment.presentation.view.account

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import coil.ImageLoader
import coil.request.ImageRequest
import com.google.android.material.snackbar.Snackbar
//import com.squareup.picasso.Picasso
import diarynote.core.common.dialog.data.DialogerImpl
import diarynote.core.utils.FileHelper
import diarynote.core.utils.ROOM_UPDATE_BIT_NUMBER
import diarynote.core.utils.listener.OnDialogPositiveButtonClickListener
import diarynote.data.model.UserModel
import diarynote.data.model.state.CopyFileState
import diarynote.settingsfragment.R
import diarynote.settingsfragment.databinding.FragmentUserImageBinding
import diarynote.settingsfragment.presentation.viewmodel.SettingsViewModel
import diarynote.data.model.state.UserState
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.util.jar.Manifest

class UserImageFragment : Fragment() {

    private var _binding: FragmentUserImageBinding? = null
    private val binding get() = _binding!!
    private val settingsViewModel: SettingsViewModel by viewModel()
    private lateinit var userModel: UserModel
    private val imageLoader: ImageLoader by inject()
    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) {
        if (it != null) {
            val imagePath = context?.let { it1 -> FileHelper().getRealPathFromURI(it1, it) }
            if (imagePath != null) {
                //copy file and change user path
                settingsViewModel.copyFile(
                    imagePath,
                    getAppFileFullPath(getFileNameFromFullPath(imagePath))
                )
            } else {
                showErrorDialog()
            }
        }
    }
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
                isGranted: Boolean ->
            if (isGranted) {
                chooseImage()
            } else {
                showExplanationDialog()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentUserImageBinding.bind(inflater.inflate(R.layout.fragment_user_image, container, false))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observeData()
        observeCopyFileData()
    }

    private fun initViews() = with(binding) {
        val permissionString = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU)
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        else
            android.Manifest.permission.READ_MEDIA_IMAGES
        profilePhotoImageViewBackground.setOnClickListener {
            getExternalStorageReadPermission(permissionString)
        }
    }

    private fun observeData() {
        val observer = Observer<UserState?> { renderData(it) }
        settingsViewModel.userLiveData.observe(viewLifecycleOwner, observer)
    }

    private fun observeCopyFileData() {
        val observer = Observer<CopyFileState?> { renderCopyFileData(it) }
        settingsViewModel.copyFileLiveData.observe(viewLifecycleOwner, observer)
    }

    private fun renderData(userState: UserState?) {
        when(userState) {
            is UserState.Error -> handleError(userState.errorCode, userState.message)
            UserState.Loading -> setProgressBarVisible(true)
            is UserState.Success -> handleSuccess(userState.userModel)
            else -> {}
        }
    }

    private fun renderCopyFileData(copyFileState: CopyFileState?) {
        when(copyFileState) {
            is CopyFileState.Error -> Toast.makeText(requireActivity(),
                getString(diarynote.core.R.string.file_reading_error_toast_text), Toast.LENGTH_SHORT).show()
            is CopyFileState.Success -> {
                settingsViewModel.changeUserImagePath(copyFileState.filePath, userModel)
            }
            else -> {}
        }
    }

    private fun handleError(errorCode: Int, message: String) = with(binding)  {
        setProgressBarVisible(false)
        if ((1 shl ROOM_UPDATE_BIT_NUMBER) and errorCode != 0) {
            showErrorDialog()
        } else {
            Snackbar.make(this.userImageRootLayout, message, Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(diarynote.core.R.string.reload_notes_list_text)) { settingsViewModel.getCurrentUserInfo() }
                .show()
        }
    }

    private fun showErrorDialog() {
        val dialoger = DialogerImpl(requireActivity())

        dialoger.showAlertDialog(getString(diarynote.core.R.string.error_dialog_title_text),
            getString(diarynote.core.R.string.profile_photo_error_dialog_message_text),
            getString(diarynote.core.R.string.dialog_button_ok_text))
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

    private fun handleSuccess(userModel: UserModel) = with(binding) {
        val file = File(userModel.imagePath)

        setProgressBarVisible(false)
        this@UserImageFragment.userModel = userModel
        if (userModel.imagePath != "" && file.exists()) {
            val request = ImageRequest.Builder(requireContext())
                .data(userModel.imagePath)
                .target(profilePhotoImageView)
                .error(diarynote.core.R.drawable.person_icon)
                .build()
            imageLoader.enqueue(request)
        }
    }

    private fun chooseImage() {
        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun getExternalStorageReadPermission(permissionString: String) {
        when {
            ContextCompat.checkSelfPermission(
                requireActivity(),
                permissionString
            ) == PackageManager.PERMISSION_GRANTED -> chooseImage()

            //////////////////////////////////////////////////////////////////

            shouldShowRequestPermissionRationale(permissionString) -> showRationaleDialog()

            //////////////////////////////////////////////////////////////////

            else -> requestPermissionLauncher.launch(permissionString)
        }
    }

    private fun showRationaleDialog() {
        val dialoger = DialogerImpl(
            requireActivity(),
            onDialogPositiveButtonClickListener = object : OnDialogPositiveButtonClickListener {
                override fun onClick() {
                    requestPermissionLauncher.launch(
                        android.Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                }
            }
        )

        dialoger.showTwoButtonDialog(getString(diarynote.core.R.string.read_external_storage_permission_to_add_category_icon_dialog_title_text),
            getString(diarynote.core.R.string.read_external_storage_permission_to_add_user_image_dialog_message_text),
            getString(diarynote.core.R.string.permission_dialog_allow_button_text),
            getString(diarynote.core.R.string.permission_dialog_deny_button_text)
        )
    }

    private fun showExplanationDialog() {
        val dialoger = DialogerImpl(requireActivity())

        dialoger.showAlertDialog(
            getString(diarynote.core.R.string.read_external_storage_permission_denied_explanation_dialog_title_text),
            getString(diarynote.core.R.string.user_photo_read_external_storage_permission_denied_explanation_dialog_message_text),
            getString(diarynote.core.R.string.dialog_button_ok_text)
        )
    }

    private fun getFileNameFromFullPath(fullFilePath: String): String {
        val splittedPathList = fullFilePath.split("/")
        return splittedPathList.last()
    }

    private fun getAppFileFullPath(fileName: String): String {
        return "${requireActivity().filesDir.absolutePath}/img/$fileName"
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}