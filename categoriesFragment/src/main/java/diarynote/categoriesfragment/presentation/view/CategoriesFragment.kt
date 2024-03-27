package diarynote.categoriesfragment.presentation.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import coil.ImageLoader
import com.google.android.material.snackbar.Snackbar
import diarynote.categoriesfragment.R
import diarynote.categoriesfragment.databinding.FragmentCategoriesBinding
import diarynote.categoriesfragment.model.CategoriesState
import diarynote.categoriesfragment.presentation.view.adapter.CategoriesListAdapter
import diarynote.categoriesfragment.presentation.viewmodel.CategoriesViewModel
import diarynote.data.domain.CATEGORY_ID_BUNDLE
import diarynote.data.domain.CATEGORY_MODEL_BUNDLE
import diarynote.data.domain.CATEGORY_NAME_BUNDLE
import diarynote.data.model.CategoryModel
import diarynote.navigator.Navigator
import diarynote.template.utils.OnCategoryItemClickListener
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class CategoriesFragment : Fragment() {

    private var _binding: FragmentCategoriesBinding? = null
    private val binding get() = _binding!!
    private val navigator: Navigator by inject()
    private val imageLoader: ImageLoader by inject()
    private val categoriesViewModel: CategoriesViewModel by viewModel()
    private val adapter = CategoriesListAdapter(
        onCategoryItemClickListener = object : OnCategoryItemClickListener{
        override fun onItemClick(categoryModel: CategoryModel) {
                val bundle = Bundle().apply {
                    putInt(CATEGORY_ID_BUNDLE, categoryModel.id)
                    putString(CATEGORY_NAME_BUNDLE, categoryModel.categoryName)
                    putParcelable(CATEGORY_MODEL_BUNDLE, categoryModel)
                }
                this@CategoriesFragment.arguments = bundle
                navigator.actionCategoriesToCategoryNotes(bundle)
            }
        },
        imageLoader = imageLoader
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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
            addCategoryFab.setOnClickListener {
                navigator.actionCategoriesToCreteCategory()
            }
        }
    }

    private fun observeData() {
        val observer = Observer<CategoriesState> { renderData(it)}
        categoriesViewModel.categoriesLiveData.observe(viewLifecycleOwner, observer)
    }
    
    private fun renderData(categoriesState: CategoriesState) {
        when(categoriesState) {
            is CategoriesState.Success -> setList(categoriesState.categoryModelList)
            is CategoriesState.Loading -> showProgressBar()
            is CategoriesState.Error -> handleError(categoriesState.message)
        }
    }

    private fun handleError(message: String) = with(binding) {
        Log.d("VVV", "handleError: $message")
        Snackbar.make(this.categoriesFragmentRootLayout, message, Snackbar.LENGTH_INDEFINITE)
            .setAction(getString(diarynote.core.R.string.reload_notes_list_text)) { categoriesViewModel.getCategoriesList() }
            .show()
    }

    private fun setList(categoryModelList: List<CategoryModel>) {
        with(binding) {
            categoriesListRecyclerView.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
        }
        adapter.setData(categoryModelList)
    }

    private fun showProgressBar() = with(binding) {
        categoriesListRecyclerView.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
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