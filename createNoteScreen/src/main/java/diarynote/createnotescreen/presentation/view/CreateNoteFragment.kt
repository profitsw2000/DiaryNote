package diarynote.createnotescreen.presentation.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import diarynote.createnotescreen.R
import diarynote.createnotescreen.databinding.FragmentCreateNoteBinding
import diarynote.createnotescreen.model.CategoriesState
import diarynote.createnotescreen.presentation.view.adapter.HorizontalCategoryListAdapter
import diarynote.createnotescreen.presentation.view.utils.OnItemClickListener
import diarynote.createnotescreen.presentation.viewmodel.CreateNoteViewModel
import diarynote.data.model.CategoryModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class CreateNoteFragment : Fragment() {

    private var _binding: FragmentCreateNoteBinding? = null
    private val binding get() = _binding!!
    private lateinit var data: List<CategoryModel>
    private val createNoteViewModel: CreateNoteViewModel by viewModel()
    private val adapter = HorizontalCategoryListAdapter(object : OnItemClickListener {
        override fun onItemClick(position: Int) {
            updateListItem(position)
        }
    })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentCreateNoteBinding.bind(inflater.inflate(R.layout.fragment_create_note, container, false))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeData()
        binding.horizontalCategoryListRecyclerView.adapter = adapter
        createNoteViewModel.getCategoriesList()
    }

    private fun observeData() {
        val observer = Observer<CategoriesState> { renderData(it)}
        createNoteViewModel.categoriesLiveData.observe(viewLifecycleOwner, observer)
    }

    private fun renderData(categoriesState: CategoriesState) {
        when(categoriesState) {
            is CategoriesState.Success -> loadingCategoriesSuccess(categoriesState)
            is CategoriesState.Loading -> showProgressBar()
            is CategoriesState.Error -> handleError(categoriesState.message)
        }
    }

    private fun loadingCategoriesSuccess(categoriesState: CategoriesState.Success)  = with(binding) {
        progressBar.visibility = View.GONE
        data = categoriesState.categoryModelList
        adapter.setData(data)
    }

    private fun handleError(message: String) = with(binding) {
        progressBar.visibility = View.GONE
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun showProgressBar() = with(binding) {
        progressBar.visibility = View.VISIBLE
    }

    private fun updateListItem(position: Int) {
        if(position < data.size) {
            binding.horizontalCategoryListRecyclerView.removeAllViews()
            adapter.updateData(data, position)
        } else {

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = CreateNoteFragment()
    }
}