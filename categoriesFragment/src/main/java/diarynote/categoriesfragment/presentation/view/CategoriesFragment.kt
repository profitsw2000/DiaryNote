package diarynote.categoriesfragment.presentation.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import diarynote.categoriesfragment.R
import diarynote.categoriesfragment.databinding.FragmentCategoriesBinding
import diarynote.categoriesfragment.model.CategoriesState
import diarynote.categoriesfragment.presentation.view.adapter.CategoriesListAdapter
import diarynote.categoriesfragment.presentation.viewmodel.CategoriesViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class CategoriesFragment : Fragment() {

    private var _binding: FragmentCategoriesBinding? = null
    private val binding get() = _binding!!
    private val categoriesViewModel: CategoriesViewModel by viewModel()
    private val adapter = CategoriesListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCategoriesBinding.bind(inflater.inflate(R.layout.fragment_categories, container, false))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observeData()
        categoriesViewModel.getCategoriesList()
    }

    private fun initViews() {
        with(binding) {
            categoriesListRecyclerView.adapter = adapter
        }
    }

    private fun observeData() {
        val observer = Observer<CategoriesState> { renderData(it)}
        categoriesViewModel.categoriesLiveData.observe(viewLifecycleOwner, observer)
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
        fun newInstance() = CategoriesFragment()
    }
}