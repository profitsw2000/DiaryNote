package diarynote.settingsfragment.presentation.view.account

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import diarynote.data.model.UserModel
import diarynote.settingsfragment.R
import diarynote.settingsfragment.databinding.FragmentUserImageBinding
import diarynote.settingsfragment.presentation.viewmodel.SettingsViewModel
import diarynote.template.model.UserState
import org.koin.androidx.viewmodel.ext.android.viewModel

class UserImageFragment : Fragment() {

    private var _binding: FragmentUserImageBinding? = null
    private val binding get() = _binding!!
    private val settingsViewModel: SettingsViewModel by viewModel()
    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) {
        if (it != null) {
            binding.profilePhotoImageView.setImageURI(it)
            Log.d("VVV", "onActivityResult: ${it.encodedPath}")
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

    private fun handleError(errorCode: Int, message: String) {
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

    private fun handleSuccess(userModel: UserModel) {
        setProgressBarVisible(false)

    }

    private fun chooseImage() {
/*

        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT

        startActivityForResult(Intent.createChooser(intent, "Выберите фото"), SELECT_PICTURE)
*/*/

        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK && requestCode == 200) {
            val selectedImageUri = data?.data

            selectedImageUri?.let {
                binding.profilePhotoImageView.setImageURI(selectedImageUri)
                Log.d("VVV", "onActivityResult: ${it.encodedPath}")
            }
        }
    }


}

// val intent = Intent()
// intent.type = "image/*"
// intent.action = Intent.ACTION_GET_CONTENT
//
// startActivityForResult(Intent.createChooser(intent, "Выберите фото"), SELECT_PICTURE)