package diarynote.editnotescreen.presentation.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import diarynote.data.domain.NOTE_MODEL_BUNDLE
import diarynote.data.model.CategoryModel
import diarynote.data.model.NoteModel
import diarynote.data.model.state.CategoriesState
import diarynote.editnotescreen.R
import diarynote.editnotescreen.databinding.FragmentEditNoteBinding
import diarynote.editnotescreen.presentation.view.adapter.HorizontalCategoryListAdapter
import diarynote.editnotescreen.presentation.view.utils.OnItemClickListener
import diarynote.editnotescreen.presentation.viewmodel.EditNoteViewModel
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditNoteFragment : Fragment() {

    private var _binding: FragmentEditNoteBinding? = null
    private val binding get() = _binding!!
    private val editNoteViewModel: EditNoteViewModel by viewModel()
    private lateinit var data: List<CategoryModel>
    private val noteModel: NoteModel? by lazy { arguments?.getParcelable(NOTE_MODEL_BUNDLE) }
    private val adapter = HorizontalCategoryListAdapter(object : OnItemClickListener {
        override fun onItemClick(position: Int) {
            updateListItem(position)
        }
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentEditNoteBinding.bind(inflater.inflate(R.layout.fragment_edit_note, container, false))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    private fun initViews() = with(binding) {
        if (noteModel != null) {
            noteTitleInputLayout.editText?.setText(noteModel?.title)
            noteContentInputLayout.editText?.setText(noteModel?.text)
            noteTagsInputLayout.editText?.setText(noteModel?.tags?.joinToString(",", "", ""))
        }
    }

    private fun observeData() {
        val registrationObserver = Observer<CategoriesState> { renderCategoriesData(it)}
        editNoteViewModel.categoriesLiveData.observe(viewLifecycleOwner, registrationObserver)
/*        val noteObserver = Observer<NotesState?> { renderNotesData(it)}
        createNoteViewModel.notesLiveData.observe(viewLifecycleOwner, noteObserver)*/
    }

    private fun renderCategoriesData(categoriesState: CategoriesState?) {

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
        fun newInstance() = EditNoteFragment()
    }
}