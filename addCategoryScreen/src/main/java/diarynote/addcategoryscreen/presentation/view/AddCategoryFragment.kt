package diarynote.addcategoryscreen.presentation.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import diarynote.data.model.CategoryModel
import diarynote.data.model.state.CategoriesState
import diarynote.navigator.Navigator
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

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
            if (position == (iconData.size -1)) chooseImage()
        }

    })
    private val pickSvgFile = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) {
        if (it != null) {
            val svgFilePath = FileHelper().getRealPathFromURI(requireActivity(), it)
            svgFilePath?.let {
                Toast.makeText(requireActivity(), it, Toast.LENGTH_SHORT).show()
            }
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

    private fun renderData(categoriesState: CategoriesState?) {
        when(categoriesState) {
            is CategoriesState.Success -> addCategorySuccess()
            is CategoriesState.Loading -> showProgressBar()
            is CategoriesState.Error -> handleError(categoriesState.message)
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
        val mimeType = "image/svg+xml"
        pickSvgFile.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.SingleMimeType(mimeType)))
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

    companion object {
        @JvmStatic
        fun newInstance() = AddCategoryFragment()
    }
}