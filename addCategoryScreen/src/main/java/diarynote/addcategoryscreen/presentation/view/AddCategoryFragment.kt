package diarynote.addcategoryscreen.presentation.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.Observer
import diarynote.addcategoryscreen.R
import diarynote.addcategoryscreen.data.colorList
import diarynote.addcategoryscreen.data.iconList
import diarynote.addcategoryscreen.databinding.FragmentAddCategoryBinding
import diarynote.addcategoryscreen.presentation.view.adapter.ColorListAdapter
import diarynote.addcategoryscreen.presentation.view.adapter.IconListAdapter
import diarynote.addcategoryscreen.presentation.viewmodel.AddCategoryViewModel
import diarynote.core.common.dialog.data.DialogerImpl
import diarynote.core.utils.listener.OnDialogPositiveButtonClickListener
import diarynote.core.utils.listener.OnItemClickListener
import diarynote.data.model.CategoryModel
import diarynote.data.model.state.CategoriesState
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddCategoryFragment : Fragment() {

    private var _binding: FragmentAddCategoryBinding? = null
    private val binding get() = _binding!!
    private val addCategoryViewModel: AddCategoryViewModel by viewModel()
    private val colorData by lazy { colorList.map { it.copy() } }
    private val iconData by lazy { iconList.map { it.copy() } }
    private val colorListAdapter = ColorListAdapter()
    private val iconListAdapter = IconListAdapter()

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
        colorListAdapter.setData(colorData)
        iconPickerRecyclerView.adapter = iconListAdapter
        iconListAdapter.setData(iconData)
        Log.d("VVV", iconData.hashCode().toString())
        Log.d("VVV", iconList.hashCode().toString())
        Log.d("VVV", iconList.toString())
        Log.d("VVV", iconData.toString())
        Log.d("VVV", iconListAdapter.toString())
        addCategoryButton.setOnClickListener {
            val categoryModel = CategoryModel(
                0,
                colorData[colorListAdapter.getClickedPosition()].color,
                categoryTitleInputLayout.editText?.text.toString(),
                iconData[iconListAdapter.getClickedPosition()].icon,
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
                addCategoryViewModel.navigateUp()
                addCategoryViewModel.clear()
            }
        })
        progressBar.visibility = View.GONE
        //Toast.makeText(requireContext(), "Создано успешно!!!", Toast.LENGTH_SHORT).show()

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
                    addCategoryViewModel.navigateUp()
                }
            })

        dialoger.showTwoButtonDialog(getString(diarynote.core.R.string.exit_note_creation_dialog_title_text), getString(
            diarynote.core.R.string.exit_category_creation_dialog_message_text), getString(
            diarynote.core.R.string.dialog_button_yes_text), getString(
            diarynote.core.R.string.dialog_button_no_text))
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