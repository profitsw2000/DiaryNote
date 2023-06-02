package diarynote.addcategoryscreen.presentation.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import diarynote.addcategoryscreen.R
import diarynote.addcategoryscreen.data.colorList
import diarynote.addcategoryscreen.data.iconList
import diarynote.addcategoryscreen.databinding.FragmentAddCategoryBinding
import diarynote.addcategoryscreen.presentation.view.adapter.ColorListAdapter
import diarynote.addcategoryscreen.presentation.view.adapter.IconListAdapter
import diarynote.addcategoryscreen.presentation.viewmodel.AddCategoryViewModel
import diarynote.core.common.dialog.data.DialogerImpl
import diarynote.core.utils.listener.OnItemClickListener
import diarynote.data.model.CategoryModel
import diarynote.data.model.state.CategoriesState
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddCategoryFragment : Fragment() {

    private var _binding: FragmentAddCategoryBinding? = null
    private val binding get() = _binding!!
    private val addCategoryViewModel: AddCategoryViewModel by viewModel()
    private val colorData = colorList
    private val iconData = iconList
    private var lastSelectedColorItem = 0
    private var lastSelectedIconItem = 0
    private val colorListAdapter = ColorListAdapter(object : OnItemClickListener {
        override fun onItemClick(position: Int) {
            updateColorList(position)
        }
    })
    private val iconListAdapter = IconListAdapter(object : OnItemClickListener {
        override fun onItemClick(position: Int) {
            Log.d("VVV", "lastSelectedIconItem = $lastSelectedIconItem")
            updateIconList(position)
            Log.d("VVV", "lastSelectedIconItem = $lastSelectedIconItem")
            Log.d("VVV", "iconData = $iconData")
        }
    })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAddCategoryBinding.bind(inflater.inflate(R.layout.fragment_add_category, container, false))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observeData()
    }

    private fun initViews() = with(binding) {
        colorPickerRecyclerView.adapter = colorListAdapter
        colorListAdapter.setData(colorData)
        iconPickerRecyclerView.adapter = iconListAdapter
        iconListAdapter.setData(iconData)
        addCategoryButton.setOnClickListener {
            val categoryModel = CategoryModel(
                0,
                colorData[lastSelectedColorItem].color,
                categoryTitleInputLayout.editText?.text.toString(),
                iconData[lastSelectedIconItem].icon,
                0
            )
            addCategoryViewModel.addCategory(categoryModel)
        }
    }

    private fun observeData() {
        val observer = Observer<CategoriesState> { renderData(it) }
        addCategoryViewModel.categoryLiveData.observe(viewLifecycleOwner, observer)
    }

    private fun renderData(categoriesState: CategoriesState) {
        when(categoriesState) {
            is CategoriesState.Success -> addCategorySuccess()
            is CategoriesState.Loading -> showProgressBar()
            is CategoriesState.Error -> handleError(categoriesState.message)
        }
    }

    private fun addCategorySuccess() = with(binding) {
        val dialoger = DialogerImpl(requireActivity())
        progressBar.visibility = View.GONE
        //Toast.makeText(requireContext(), "Создано успешно!!!", Toast.LENGTH_SHORT).show()

        dialoger.showAlertDialog(getString(diarynote.core.R.string.add_category_success_dialog_title_text),
            getString(diarynote.core.R.string.add_category_success_dialog_content_text),
            getString(diarynote.core.R.string.dialog_button_ok_text)
        )
    }

    private fun handleError(message: String) = with(binding) {
        progressBar.visibility = View.GONE
        if (message == "Название категории не менее 2 символов") categoryTitleInputLayout.error = message
        else Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun showProgressBar() = with(binding)  {
        progressBar.visibility = View.VISIBLE
    }

    private fun updateColorList(position: Int) {
        colorData[position].isSelected = true
        if (position != lastSelectedColorItem) colorData[lastSelectedColorItem].isSelected = false
        colorListAdapter.setData(colorData, position, lastSelectedColorItem)
        lastSelectedColorItem = position
    }

    private fun updateIconList(position: Int) {
        iconData[position].isSelected = true
        if (position != lastSelectedIconItem) iconData[lastSelectedIconItem].isSelected = false
        iconListAdapter.setData(iconData, position, lastSelectedIconItem)
        lastSelectedIconItem = position
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