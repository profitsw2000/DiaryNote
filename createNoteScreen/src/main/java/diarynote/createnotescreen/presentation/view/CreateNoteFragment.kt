package diarynote.createnotescreen.presentation.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import diarynote.createnotescreen.R
import diarynote.createnotescreen.databinding.FragmentCreateNoteBinding
import diarynote.createnotescreen.model.CategoriesState
import diarynote.createnotescreen.presentation.view.adapter.HorizontalCategoryListAdapter
import diarynote.createnotescreen.presentation.viewmodel.CreateNoteViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class CreateNoteFragment : Fragment() {

    private var _binding: FragmentCreateNoteBinding? = null
    private val binding get() = _binding!!
    private val createNoteViewModel: CreateNoteViewModel by viewModel()
    private val adapter = HorizontalCategoryListAdapter()

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
            is CategoriesState.Success -> adapter.setData(categoriesState.categoryModelList)
            is CategoriesState.Loading -> showProgressBar()
            is CategoriesState.Error -> handleError(categoriesState.message)
        }
    }

    private fun handleError(message: String) {
    }

    private fun showProgressBar() {
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