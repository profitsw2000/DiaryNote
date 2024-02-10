package diarynote.addcategoryscreen.presentation.view

import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import diarynote.addcategoryscreen.R
import diarynote.addcategoryscreen.data.colorCodeList
import diarynote.addcategoryscreen.data.iconCodeList
import diarynote.addcategoryscreen.databinding.FragmentAddCategoryBinding
import diarynote.addcategoryscreen.presentation.view.adapter.ColorListAdapter
import diarynote.addcategoryscreen.presentation.view.adapter.IconListAdapter
import diarynote.addcategoryscreen.presentation.viewmodel.AddCategoryViewModel
import diarynote.core.common.dialog.data.DialogerImpl
import diarynote.core.utils.FileHelper
import diarynote.core.utils.listener.OnDialogPositiveButtonClickListener
import diarynote.core.utils.listener.OnItemClickListener
import diarynote.data.domain.NOTE_MODEL_BUNDLE
import diarynote.data.model.CategoryModel
import diarynote.data.model.state.CategoriesState
import diarynote.data.model.state.CopyFileState
import diarynote.navigator.Navigator
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.IOException
import java.util.jar.Manifest

class AddCategoryFragment : Fragment() {

    private var _binding: FragmentAddCategoryBinding? = null
    private val binding get() = _binding!!
    private val addCategoryViewModel: AddCategoryViewModel by viewModel()
    private val navigator: Navigator by inject()
    private val colorData = colorCodeList
    private val iconData = iconCodeList
    private val colorListAdapter = ColorListAdapter()
    private val iconListAdapter = IconListAdapter(object : OnItemClickListener {
        override fun onItemClick(position: Int) {
            if (position == (iconData.size -1)) getExternalStorageReadPermission()
        }
    })
    private val pickSvgFile = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            val svgFilePath = FileHelper().getRealPathFromURI(requireActivity(), uri)

            svgFilePath?.let {
                addCategoryViewModel.copyFile(
                    it,
                    getAppFileFullPath(getFileNameFromFullPath(it))
                )
                iconListAdapter.updateIconImage(it)
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                exitCategoryCreationDialog()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true)
        _binding = FragmentAddCategoryBinding.bind(inflater.inflate(R.layout.fragment_add_category, container, false))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observeData()
        observeCopyFileData()
    }

    override fun onResume() {
        super.onResume()
        clearInputErrors()
    }

    private fun initViews() = with(binding) {

        colorPickerRecyclerView.adapter = colorListAdapter
        colorListAdapter.setData(colorData, addCategoryViewModel.selectedColorPosition)
        colorPickerRecyclerView.scrollToPosition(addCategoryViewModel.selectedColorPosition)

        iconPickerRecyclerView.adapter = iconListAdapter
        iconListAdapter.setData(iconData, addCategoryViewModel.selectedIconPosition)
        iconPickerRecyclerView.scrollToPosition(addCategoryViewModel.selectedIconPosition)

        addCategoryButton.setOnClickListener {
            val categoryModel = CategoryModel(
                0,
                colorData[colorListAdapter.clickedPosition],
                categoryTitleInputLayout.editText?.text.toString(),
                iconData[iconListAdapter.clickedPosition],
                0
            )
            addCategoryViewModel.addCategory(categoryModel)
        }
    }

    private fun observeData() {
        val observer = Observer<CategoriesState?> { renderData(it) }
        addCategoryViewModel.categoryLiveData.observe(viewLifecycleOwner, observer)
    }

    private fun observeCopyFileData() {
        val observer = Observer<CopyFileState?> { renderCopyFileData(it) }
        addCategoryViewModel.copyFileLiveData.observe(viewLifecycleOwner, observer)
    }

    private fun renderData(categoriesState: CategoriesState?) {
        when(categoriesState) {
            is CategoriesState.Success -> addCategorySuccess()
            is CategoriesState.Loading -> showProgressBar()
            is CategoriesState.Error -> handleError(categoriesState.message)
            else -> {}
        }
    }

    private fun renderCopyFileData(copyFileState: CopyFileState?) {
        when(copyFileState) {
            is CopyFileState.Error -> Toast.makeText(requireActivity(),
                getString(diarynote.core.R.string.file_reading_error_toast_text), Toast.LENGTH_SHORT).show()
            is CopyFileState.Success -> {}//iconListAdapter.updateIconImage(copyFileState.filePath)
            else -> {}
        }
    }

    private fun addCategorySuccess() = with(binding) {
        val dialoger = DialogerImpl(requireActivity(), object : OnDialogPositiveButtonClickListener{
            override fun onClick() {
                addCategoryViewModel.clear()
                clearData()
            }
        })
        progressBar.visibility = View.GONE

        dialoger.showAlertDialog(getString(diarynote.core.R.string.add_category_success_dialog_title_text),
            getString(diarynote.core.R.string.add_category_success_dialog_content_text),
            getString(diarynote.core.R.string.dialog_button_ok_text)
        )
    }

    private fun handleError(message: String) = with(binding) {
        progressBar.visibility = View.GONE
        if (message == getString(diarynote.core.R.string.category_input_layout_error_text)) categoryTitleInputLayout.error = message
        else Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun showProgressBar() = with(binding)  {
        progressBar.visibility = View.VISIBLE
    }

    private fun clearInputErrors() = with(binding) {
        categoryTitleInputLayout.error = null
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            exitCategoryCreationDialog()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun exitCategoryCreationDialog() {
        val dialoger =
            DialogerImpl(requireActivity(), object : OnDialogPositiveButtonClickListener {
                override fun onClick() {
                    addCategoryViewModel.clear()
                    clearData()
                }
            })

        dialoger.showTwoButtonDialog(getString(diarynote.core.R.string.exit_note_creation_dialog_title_text), getString(
            diarynote.core.R.string.exit_category_creation_dialog_message_text), getString(
            diarynote.core.R.string.dialog_button_yes_text), getString(
            diarynote.core.R.string.dialog_button_no_text))
    }

    private fun clearData() {
        colorListAdapter.clickedPosition = 0
        iconListAdapter.clickedPosition = 0
        navigator.navigateUp()
    }

    private fun chooseImage() {
        val mimeType = "image/*"
        pickSvgFile.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.SingleMimeType(mimeType)))
    }

    private fun copyFile(sourcePath: String, targetPath: String) {
        val sourceFile = File(sourcePath)
        val targetFile = File(targetPath)

        try {
            sourceFile.copyTo(targetFile, true)
        } catch (exception: Exception) {
            Toast.makeText(requireActivity(),
                getString(diarynote.core.R.string.file_reading_error_toast_text), Toast.LENGTH_SHORT).show()
        }
    }

    private fun getFileNameFromFullPath(fullFilePath: String): String {
        val splittedPathList = fullFilePath.split("/")
        return splittedPathList.last()
    }

    private fun getAppFileFullPath(fileName: String): String {
        return "${requireActivity().filesDir.absolutePath}/icons/$fileName"
    }

    private fun getExternalStorageReadPermission() {
        when {
            ContextCompat.checkSelfPermission(
                requireActivity(),
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED -> chooseImage()

            //////////////////////////////////////////////////////////////////

            shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE) -> showRationaleDialog()

            //////////////////////////////////////////////////////////////////

            else -> requestPermissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
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
            getString(diarynote.core.R.string.read_external_storage_permission_to_add_category_icon_dialog_message_text),
            getString(diarynote.core.R.string.permission_dialog_allow_button_text),
            getString(diarynote.core.R.string.permission_dialog_deny_button_text)
        )
    }

    private fun showExplanationDialog() {
        val dialoger = DialogerImpl(requireActivity())

        dialoger.showAlertDialog(
            getString(diarynote.core.R.string.read_external_storage_permission_denied_explanation_dialog_title_text),
            getString(diarynote.core.R.string.read_external_storage_permission_denied_explanation_dialog_message_text),
            getString(diarynote.core.R.string.dialog_button_ok_text)
        )
    }

    override fun onStop() {
        super.onStop()
        addCategoryViewModel.selectedColorPosition = colorListAdapter.clickedPosition
        addCategoryViewModel.selectedIconPosition = iconListAdapter.clickedPosition
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}