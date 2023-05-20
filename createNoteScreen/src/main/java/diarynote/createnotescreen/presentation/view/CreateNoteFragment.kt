package diarynote.createnotescreen.presentation.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import diarynote.core.common.view.Dialoger
import diarynote.core.utils.*
import diarynote.createnotescreen.R
import diarynote.createnotescreen.databinding.FragmentCreateNoteBinding
import diarynote.createnotescreen.model.CategoriesState
import diarynote.createnotescreen.model.NotesState
import diarynote.createnotescreen.presentation.view.adapter.HorizontalCategoryListAdapter
import diarynote.createnotescreen.presentation.view.utils.OnItemClickListener
import diarynote.createnotescreen.presentation.viewmodel.CreateNoteViewModel
import diarynote.data.model.CategoryModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class CreateNoteFragment : Fragment() {

    private var _binding: FragmentCreateNoteBinding? = null
    private val binding get() = _binding!!
    private lateinit var data: List<CategoryModel>
    private var selectedCategoryIndex = 0
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
        initViews()
        createNoteViewModel.getCategoriesList()
    }

    private fun initViews() = with(binding) {
        horizontalCategoryListRecyclerView.adapter = adapter
        addNoteButton.setOnClickListener {
            val noteTitle = noteTitleInputLayout.editText?.text.toString()
            val noteContent = noteContentInputLayout.editText?.text.toString()
            val noteTags = noteTagsInputLayout.editText?.text.toString()

            clearInputErrors()

            createNoteViewModel.getNotesData(
                noteTitle,
                noteContent,
                noteTags,
                data[selectedCategoryIndex].categoryName
            )
        }
    }

    override fun onResume() {
        super.onResume()
        clearInputErrors()
    }

    private fun clearInputErrors() = with(binding) {
        noteTitleInputLayout.error = null
        noteContentInputLayout.error= null
        noteTagsInputLayout.error = null
    }

    private fun observeData() {
        val registrationObserver = Observer<CategoriesState> { renderCategoriesData(it)}
        createNoteViewModel.categoriesLiveData.observe(viewLifecycleOwner, registrationObserver)
        val noteObserver = Observer<NotesState> { renderNotesData(it)}
        createNoteViewModel.notesLiveData.observe(viewLifecycleOwner, noteObserver)
    }

    private fun renderCategoriesData(categoriesState: CategoriesState) {
        when(categoriesState) {
            is CategoriesState.Success -> loadingCategoriesSuccess(categoriesState)
            is CategoriesState.Loading -> showProgressBar()
            is CategoriesState.Error -> handleError(categoriesState.message)
        }
    }

    private fun renderNotesData(notesState: NotesState) {
        when(notesState) {
            is NotesState.Success -> successfulNoteCreation()
            is NotesState.Loading -> showProgressBar()
            is NotesState.Error -> noteCreationError(notesState)
        }
    }

    private fun successfulNoteCreation() = with(binding) {
        progressBar.visibility = View.GONE
        noteTitleInputLayout.editText?.setText("")
        noteContentInputLayout.editText?.setText("")
        noteTagsInputLayout.editText?.setText("")

        Toast.makeText(requireContext(), "Заметка добавлена!", Toast.LENGTH_SHORT).show()
        createNoteViewModel.navigateUp()
    }

    private fun loadingCategoriesSuccess(categoriesState: CategoriesState.Success)  = with(binding) {
        progressBar.visibility = View.GONE
        data = categoriesState.categoryModelList
        adapter.setData(data)
    }

    private fun noteCreationError(notesState: NotesState.Error) = with(binding) {
        val dialoger = Dialoger(requireActivity())
        progressBar.visibility = View.GONE

        if((1 shl NOTE_TITLE_BIT_NUMBER) and notesState.errorCode != 0) noteTitleInputLayout.error = "Заголовок не менее $NOTE_TITLE_MIN_LENGTH символов"
        if((1 shl NOTE_CONTENT_BIT_NUMBER) and notesState.errorCode != 0) noteContentInputLayout.error = "Заметка не менее $NOTE_CONTENT_MIN_LENGTH символов"
        if((1 shl NOTE_TAGS_BIT_NUMBER) and notesState.errorCode != 0) noteTagsInputLayout.error = "Строка тэгов не менее $NOTE_TAGS_MIN_LENGTH символов"
        if((1 shl NOTE_TAG_WORDS_BIT_NUMBER) and notesState.errorCode != 0) noteTagsInputLayout.error = "Один тэг не более $NOTE_TAG_WORDS_LIMIT слов"
        if((1 shl ROOM_BIT_NUMBER) and notesState.errorCode != 0) dialoger.showAlertDialog(getString(diarynote.core.R.string.error_dialog_title_text), getString(diarynote.core.R.string.room_note_adding_error_message))
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