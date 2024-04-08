package ru.profitsw2000.editcategoryscreen.presentation.view

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import coil.ImageLoader
import diarynote.core.common.dialog.data.DialogerImpl
import diarynote.core.utils.CATEGORY_NAME_LENGTH_ERROR
import diarynote.core.utils.FileHelper
import diarynote.core.utils.listener.OnDialogPositiveButtonClickListener
import diarynote.core.utils.listener.OnItemClickListener
import diarynote.data.domain.CATEGORY_MODEL_BUNDLE
import diarynote.data.hardcodeddata.colorCodeList
import diarynote.data.hardcodeddata.iconCodeList
import diarynote.data.model.CategoryModel
import diarynote.data.model.state.CategoriesState
import diarynote.data.model.state.CopyFileState
import diarynote.navigator.Navigator
import diarynote.template.databinding.FragmentCategoryCreationBinding
import diarynote.template.presentation.adapter.ColorListAdapter
import diarynote.template.presentation.adapter.IconListAdapter
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.profitsw2000.editcategoryscreen.R
import ru.profitsw2000.editcategoryscreen.presentation.viewmodel.EditCategoryViewModel


class EditCategoryFragment : Fragment() {

    private var _binding: FragmentCategoryCreationBinding? = null
    private val binding get() = _binding!!
    private val editCategoryViewModel: EditCategoryViewModel by viewModel()
    private val navigator: Navigator by inject()
    private val imageLoader: ImageLoader by inject()
    private val colorData = colorCodeList
    private val iconData = iconCodeList
    private val categoryModel: CategoryModel? by lazy { 
        arguments?.getParcelable(CATEGORY_MODEL_BUNDLE)
    }
    private val colorListAdapter = ColorListAdapter()
    private lateinit var imagePath: String
    private var isRecreated = false
    private val iconListAdapter = IconListAdapter(
        onItemClickListener = object : OnItemClickListener {
            override fun onItemClick(position: Int) {
                if (position == (iconData.size - 1)) getExternalStorageReadPermission()
            }
        },
        imageLoader = imageLoader
    )
    private val pickSvgFile = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            val svgFilePath = FileHelper().getRealPathFromURI(requireActivity(), uri)

            svgFilePath?.let {
                editCategoryViewModel.copyFile(
                    it,
                    getAppFileFullPath(getFileNameFromFullPath(it))
                )
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
        setHasOptionsMenu(true)
        _binding = FragmentCategoryCreationBinding.bind(inflater.inflate(diarynote.template.R.layout.fragment_category_creation, container, false))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isRecreated = (savedInstanceState != null)
        initViews()
        observeData()
        observeCopyFileData()
    }

    override fun onResume() {
        super.onResume()
        clearInputErrors()
    }

    private fun initViews() {
        if (categoryModel != null) {
            initInputLayout()
            initColorListAdapter()
            initIconListAdapter()
            initEditCategoryButton()
        }
    }

    private fun initInputLayout() = with(binding) {
        categoryTitleInputLayout.editText?.setText(categoryModel?.categoryName)
    }

    private fun initColorListAdapter() = with(binding) {
        colorPickerRecyclerView.adapter = colorListAdapter
        colorListAdapter.setData(colorData, getEditedCategoryColorIndex())
        colorPickerRecyclerView.scrollToPosition(getEditedCategoryColorIndex())
    }

    private fun initIconListAdapter() = with(binding) {
        imagePath = categoryModel?.imagePath ?: ""
        iconPickerRecyclerView.adapter = iconListAdapter
        iconListAdapter.setData(iconData, getEditedCategoryIconIndex())
        iconPickerRecyclerView.scrollToPosition(getEditedCategoryIconIndex())
    }

    private fun initEditCategoryButton() = with(binding) {
        addCategoryButton.text = getString(diarynote.core.R.string.change_item_button_text)
        addCategoryButton.setOnClickListener {
            categoryModel?.let { it1 ->
                editCategoryViewModel.editCategory(
                    it1.copy(
                        color = colorData[colorListAdapter.clickedPosition],
                        categoryName = categoryTitleInputLayout.editText?.text.toString(),
                        categoryImage = iconData[iconListAdapter.clickedPosition],
                        imagePath = imagePath)
                )
            }
        }

    }

    private fun observeData() {
        val observer = Observer<CategoriesState?> { renderData(it) }
        editCategoryViewModel.categoryLiveData.observe(viewLifecycleOwner, observer)
    }

    private fun observeCopyFileData() {
        val observer = Observer<CopyFileState?> { renderCopyFileData(it) }
        editCategoryViewModel.copyFileLiveData.observe(viewLifecycleOwner, observer)
    }

    private fun renderData(categoriesState: CategoriesState?) {
        when(categoriesState) {
            is CategoriesState.Success -> editCategorySuccess()
            is CategoriesState.Loading -> showProgressBar()
            is CategoriesState.Error -> handleError(categoriesState.message)
            else -> {}
        }
    }

    private fun renderCopyFileData(copyFileState: CopyFileState?) {
        when(copyFileState) {
            is CopyFileState.Error -> Toast.makeText(requireActivity(),
                getString(diarynote.core.R.string.file_reading_error_toast_text), Toast.LENGTH_SHORT).show()
            is CopyFileState.Success -> {
                iconListAdapter.updateIconImage(copyFileState.filePath)
                imagePath = copyFileState.filePath
            }
            else -> {}
        }
    }

    private fun editCategorySuccess() = with(binding) {
        val dialoger = DialogerImpl(requireActivity(), object : OnDialogPositiveButtonClickListener {
            override fun onClick() {
                editCategoryViewModel.clear()
                clearData()
            }
        })
        progressBar.visibility = View.GONE

        dialoger.showAlertDialog(getString(diarynote.core.R.string.edit_category_success_dialog_title_text),
            getString(diarynote.core.R.string.edit_category_success_dialog_content_text),
            getString(diarynote.core.R.string.dialog_button_ok_text)
        )
    }

    private fun handleError(message: String) = with(binding) {
        progressBar.visibility = View.GONE
        if (message == CATEGORY_NAME_LENGTH_ERROR)
            categoryTitleInputLayout.error = getString(diarynote.core.R.string.category_input_layout_error_text)
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
                    editCategoryViewModel.clear()
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

    private fun getEditedCategoryColorIndex() : Int {
        if (isRecreated) {
            return editCategoryViewModel.selectedColorPosition
        } else {
            colorData.forEachIndexed { index, element ->
                if (element == categoryModel?.color) return index
            }
            return 0
        }
    }

    private fun getEditedCategoryIconIndex() : Int {
        if (isRecreated) {
            return editCategoryViewModel.selectedIconPosition
        } else {
            iconData.forEachIndexed { index, element ->
                if (element == categoryModel?.categoryImage) return index
            }
            return 0
        }
    }

    override fun onStop() {
        super.onStop()
        editCategoryViewModel.selectedColorPosition = colorListAdapter.clickedPosition
        editCategoryViewModel.selectedIconPosition = iconListAdapter.clickedPosition
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}