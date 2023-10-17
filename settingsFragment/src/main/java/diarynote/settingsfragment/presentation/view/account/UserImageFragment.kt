package diarynote.settingsfragment.presentation.view.account

import android.annotation.SuppressLint
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import diarynote.core.utils.FileHelper
import diarynote.data.model.UserModel
import diarynote.settingsfragment.R
import diarynote.settingsfragment.databinding.FragmentUserImageBinding
import diarynote.settingsfragment.presentation.viewmodel.SettingsViewModel
import diarynote.template.model.UserState
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

class UserImageFragment : Fragment() {

    private var _binding: FragmentUserImageBinding? = null
    private val binding get() = _binding!!
    private val settingsViewModel: SettingsViewModel by viewModel()
    private lateinit var userModel: UserModel
    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) {
        if (it != null) {
            val imagePath = context?.let { it1 -> FileHelper().getRealPathFromURI(it1, it) }
            if (imagePath != null) {
                settingsViewModel.changeUserImagePath(imagePath, userModel)
            }
        } else {
            Log.d("VVV", "No media selected")
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
    }

    private fun initViews() = with(binding) {
        profilePhotoImageViewBackground.setOnClickListener {
            chooseImage()
        }
    }

    private fun observeData() {
        val observer = Observer<UserState?> { renderData(it) }
        settingsViewModel.userLiveData.observe(viewLifecycleOwner, observer)
    }

    private fun renderData(userState: UserState) {
        when(userState) {
            is UserState.Error -> handleError(userState.errorCode, userState.message)
            UserState.Loading -> setProgressBarVisible(true)
            is UserState.Success -> handleSuccess(userState.userModel)
        }
    }

    private fun handleError(errorCode: Int, message: String) = with(binding)  {
        setProgressBarVisible(false)
        Snackbar.make(this.userImageRootLayout, message, Snackbar.LENGTH_INDEFINITE)
            .setAction(getString(diarynote.core.R.string.reload_notes_list_text)) { settingsViewModel.getCurrentUserInfo() }
            .show()
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
            Picasso.get().load(file).into(profilePhotoImageView)
        }
    }

    private fun chooseImage() {
        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }
}