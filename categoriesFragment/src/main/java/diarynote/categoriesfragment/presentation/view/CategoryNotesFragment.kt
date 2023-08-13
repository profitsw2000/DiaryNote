package diarynote.categoriesfragment.presentation.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import diarynote.categoriesfragment.databinding.FragmentCategoryNotesBinding
import diarynote.categoriesfragment.presentation.viewmodel.CategoriesViewModel
import diarynote.data.domain.CATEGORY_ID_BUNDLE
import diarynote.data.domain.CATEGORY_NAME_BUNDLE
import diarynote.data.domain.NOTE_MODEL_BUNDLE
import diarynote.data.model.NoteModel
import diarynote.navigator.Navigator
import diarynote.template.model.NotesState
import diarynote.template.presentation.adapter.NotesListAdapter
import diarynote.template.utils.OnNoteItemClickListener
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class CategoryNotesFragment : Fragment() {

    private val TAG = "VVV"
    private var _binding: FragmentCategoryNotesBinding? = null
    private val binding get() = _binding!!
    private val navigator: Navigator by inject()
    private val categoriesViewModel: CategoriesViewModel by viewModel()
    private val categoryId: Int? by lazy { arguments?.getInt(CATEGORY_ID_BUNDLE) }
    private val categoryName: String? by lazy { arguments?.getString(CATEGORY_NAME_BUNDLE) }
    private val adapter = NotesListAdapter(object : OnNoteItemClickListener{
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
        initViews()
        observeData()
        categoryId?.let { categoriesViewModel.getNotesList(it) }
    }

    private fun initViews() {
        binding.categoryNotesRecyclerView.adapter = adapter
    }

    private fun observeData() {
        val observer = Observer<NotesState> { renderData(it) }
        categoriesViewModel.notesLiveData.observe(viewLifecycleOwner, observer)
    }

    private fun renderData(notesState: NotesState) {
        when(notesState) {
            is NotesState.Success -> { setList(notesState.noteModelList) }
            is NotesState.Loading -> { showProgressBar() }
            is NotesState.Error -> { handleError(notesState.message) }
        }
    }

    private fun setList(noteModelList: List<NoteModel>) {
        with(binding){
            categoryNotesRecyclerView.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
        }

        if (noteModelList.isEmpty()) binding.textView.visibility = View.VISIBLE
        else adapter.setData(noteModelList)

    }

    private fun showProgressBar() = with(binding) {
        categoryNotesRecyclerView.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
    }

    private fun handleError(message: String) = with(binding) {
        Snackbar.make(this.categoryNotesFragmentRootLayout, message, Snackbar.LENGTH_INDEFINITE)
            .setAction(getString(diarynote.core.R.string.reload_notes_list_text)) { categoryId?.let { it1 ->
                categoriesViewModel.getNotesList(
                    it1
                )
            } }
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}