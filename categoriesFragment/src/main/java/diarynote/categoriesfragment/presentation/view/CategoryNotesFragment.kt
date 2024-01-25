package diarynote.categoriesfragment.presentation.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import diarynote.categoriesfragment.databinding.FragmentCategoryNotesBinding
import diarynote.categoriesfragment.presentation.viewmodel.CategoriesViewModel
import diarynote.data.domain.CATEGORY_ID_BUNDLE
import diarynote.data.domain.CATEGORY_NAME_BUNDLE
import diarynote.data.domain.NOTE_MODEL_BUNDLE
import diarynote.data.model.NoteModel
import diarynote.data.model.state.NotesState
import diarynote.navigator.Navigator
import diarynote.template.presentation.adapter.NotesPagedListAdapter
import diarynote.template.utils.OnNoteItemClickListener
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class CategoryNotesFragment : Fragment() {

    private var _binding: FragmentCategoryNotesBinding? = null
    private val binding get() = _binding!!
    private val navigator: Navigator by inject()
    private val categoriesViewModel: CategoriesViewModel by viewModel()
    private val categoryId: Int? by lazy { arguments?.getInt(CATEGORY_ID_BUNDLE) }
    private val categoryName: String? by lazy { arguments?.getString(CATEGORY_NAME_BUNDLE) }
    private val adapter = NotesPagedListAdapter(object : OnNoteItemClickListener{
        override fun onItemClick(noteModel: NoteModel) {
            val bundle = Bundle().apply {
                putParcelable(NOTE_MODEL_BUNDLE, noteModel)
            }
            this@CategoryNotesFragment.arguments = bundle
            navigator.navigateToNoteRead(bundle)
        }
    })

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
        categoryId?.let { categoriesViewModel.getCategoryNotesPagedList(it) }
        initViews()
        observeData()
    }

    private fun initViews() = with(binding) {
        categoryNotesRecyclerView.adapter = adapter
        categoryNotesRecyclerView.setHasFixedSize(false)
    }

    private fun observeData() {
        categoriesViewModel.notesState.observe(viewLifecycleOwner) {
            when (it) {
                is NotesState.Error -> handleError(it.message)
                NotesState.Loaded -> setProgressBarVisible(false)
                NotesState.Loading -> setProgressBarVisible(true)
                is NotesState.Success -> setProgressBarVisible(false)
            }
        }

        categoriesViewModel.notesPagedList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    private fun handleError(message: String) = with(binding) {
        setProgressBarVisible(false)
        Snackbar.make(this.categoryNotesFragmentRootLayout, message, Snackbar.LENGTH_INDEFINITE)
            .setAction(getString(diarynote.core.R.string.reload_notes_list_text)) { categoryId?.let { it1 ->
                categoriesViewModel.getCategoryNotesPagedList(
                    it1
                )
            } }
            .show()
    }

    private fun setProgressBarVisible(visible: Boolean) = with(binding) {
        if (visible) {
            progressBar.visibility = View.VISIBLE
            categoryNotesRecyclerView.visibility = View.GONE
        } else {
            progressBar.visibility = View.GONE
            categoryNotesRecyclerView.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}