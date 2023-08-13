package diarynote.categoriesfragment.presentation.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import diarynote.categoriesfragment.databinding.FragmentCategoryNotesBinding
import diarynote.categoriesfragment.presentation.viewmodel.CategoriesViewModel
import diarynote.data.domain.CATEGORY_MODEL_BUNDLE
import diarynote.template.model.NotesState
import org.koin.androidx.viewmodel.ext.android.viewModel

class CategoryNotesFragment : Fragment() {

    private val TAG = "VVV"
    private var _binding: FragmentCategoryNotesBinding? = null
    private val binding get() = _binding!!
    private val categoriesViewModel: CategoriesViewModel by viewModel()
    private val categoryId: Int? by lazy { arguments?.getInt(CATEGORY_MODEL_BUNDLE) }
    private val categoryName: String? by lazy { arguments?.getString(CATEGORY_MODEL_BUNDLE) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = categoryName
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentCategoryNotesBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeData()
        categoryId?.let { categoriesViewModel.getNotesList(it) }
    }

    private fun initViews() {

    }

    private fun observeData() {
        val observer = Observer<NotesState> { renderData(it) }
        categoriesViewModel.notesLiveData.observe(viewLifecycleOwner, observer)
    }

    private fun renderData(notesState: NotesState) {
        when(notesState) {
            is NotesState.Success -> {
                Log.d(TAG, "renderData: ${notesState.noteModelList}") }
            is NotesState.Loading -> {}
            is NotesState.Error -> {}
        }
    }
}