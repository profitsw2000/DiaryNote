package diarynote.editnotescreen.presentation.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import diarynote.core.common.dialog.data.DialogerImpl
import diarynote.core.utils.*
import diarynote.core.utils.listener.OnDialogPositiveButtonClickListener
import diarynote.data.domain.NOTE_MODEL_BUNDLE
import diarynote.data.model.CategoryModel
import diarynote.data.model.NoteModel
import diarynote.data.model.state.CategoriesState
import diarynote.data.model.state.NotesState
import diarynote.editnotescreen.R
import diarynote.editnotescreen.databinding.FragmentEditNoteBinding
import diarynote.editnotescreen.presentation.view.adapter.HorizontalCategoryListAdapter
import diarynote.editnotescreen.presentation.view.utils.OnItemClickListener
import diarynote.editnotescreen.presentation.viewmodel.EditNoteViewModel
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class EditNoteFragment : Fragment() {

    private var _binding: FragmentEditNoteBinding? = null
    private val binding get() = _binding!!
    private val editNoteViewModel: EditNoteViewModel by viewModel()
    private var selectedCategoryIndex = 0
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
        initViews()
        observeData()
        editNoteViewModel.getCategoriesList()
    }

    private fun initViews() = with(binding) {
        if (noteModel != null) {
            horizontalCategoryListRecyclerView.adapter = adapter
            noteContentInputLayout.editText?.setText(noteModel?.text)
            noteTitleInputLayout.editText?.setText(noteModel?.title)
            noteTagsInputLayout.editText?.setText(noteModel?.tags?.joinToString(",", "", ""))

            editNoteButton.setOnClickListener {
                editNoteViewModel.getNotesData(
                    noteModel!!.copy(
                        category = data[selectedCategoryIndex].categoryName,
                        title = noteTitleInputLayout.editText?.text.toString(),
                        text = noteContentInputLayout.editText?.text.toString(),
                        tags = getNoteTagsList(noteTagsInputLayout.editText?.text.toString()),
                        edited = true,
                        editDate = Calendar.getInstance().time
                    )
                )
            }
        } else {

        }
    }

    private fun observeData() {
        val registrationObserver = Observer<CategoriesState> { renderCategoriesData(it)}
        editNoteViewModel.categoriesLiveData.observe(viewLifecycleOwner, registrationObserver)
        val noteObserver = Observer<NotesState?> { renderNotesData(it)}
        editNoteViewModel.notesLiveData.observe(viewLifecycleOwner, noteObserver)
    }

    private fun renderCategoriesData(categoriesState: CategoriesState) {
        when(categoriesState) {
            is CategoriesState.Success -> loadingCategoriesSuccess(categoriesState)
            is CategoriesState.Loading -> showProgressBar()
            is CategoriesState.Error -> handleError(categoriesState.message)
        }
    }

    private fun renderNotesData(notesState: NotesState?) {
        when(notesState) {
            is NotesState.Success -> successfulNoteUpdate()
            is NotesState.Loading -> showProgressBar()
            is NotesState.Error -> noteUpdateError(notesState)
            else -> {}
        }
    }

    private fun loadingCategoriesSuccess(categoriesState: CategoriesState.Success)  = with(binding) {
        progressBar.visibility = View.GONE
        data = categoriesState.categoryModelList
        adapter.setData(data, getEditedNoteCategoryIndex())
    }

    private fun handleError(message: String) = with(binding) {
        progressBar.visibility = View.GONE
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun successfulNoteUpdate() = with(binding) {
        val dialoger = DialogerImpl(requireActivity(), object : OnDialogPositiveButtonClickListener {
            override fun onClick() {
                //editNoteViewModel.navigateUp()
                //clearInputForms()
                editNoteViewModel.clear()
                requireActivity().onBackPressed()
            }
        })

        progressBar.visibility = View.GONE

        dialoger.showAlertDialog(getString(diarynote.core.R.string.edit_note_success_dialog_title_text),
            getString(diarynote.core.R.string.edit_note_success_dialog_message_text),
            getString(diarynote.core.R.string.dialog_button_ok_text)
        )
    }

    private fun noteUpdateError(notesState: NotesState.Error) = with(binding) {
        val dialoger = DialogerImpl(requireActivity())
        progressBar.visibility = View.GONE

        if((1 shl NOTE_TITLE_BIT_NUMBER) and notesState.errorCode != 0) noteTitleInputLayout.error = getString(diarynote.core.R.string.input_note_title_error_text, NOTE_TITLE_MIN_LENGTH.toString())
        if((1 shl NOTE_CONTENT_BIT_NUMBER) and notesState.errorCode != 0) noteContentInputLayout.error = getString(diarynote.core.R.string.input_note_content_error_text, NOTE_CONTENT_MIN_LENGTH.toString())
        if((1 shl NOTE_TAGS_BIT_NUMBER) and notesState.errorCode != 0) noteTagsInputLayout.error = getString(diarynote.core.R.string.input_note_tags_line_error_text, NOTE_TAGS_MIN_LENGTH.toString())
        if((1 shl NOTE_TAG_WORDS_BIT_NUMBER) and notesState.errorCode != 0) noteTagsInputLayout.error = getString(diarynote.core.R.string.input_note_tags_words_error_text, NOTE_TAG_WORDS_LIMIT.toString())
        if((1 shl ROOM_BIT_NUMBER) and notesState.errorCode != 0) dialoger.showAlertDialog(getString(diarynote.core.R.string.error_dialog_title_text), getString(diarynote.core.R.string.room_note_adding_error_message), getString(diarynote.core.R.string.dialog_button_ok_text))
    }

    private fun showProgressBar() = with(binding) {
        progressBar.visibility = View.VISIBLE
    }

    private fun getEditedNoteCategoryIndex() : Int {
        data.forEachIndexed { index, element ->
            if (element.categoryName == noteModel?.category) return index
        }
        return 0
    }

    private fun getNoteTagsList(tags: String): List<String> {
        var tagsList = tags.split(",").toList()
        val newTagsList = mutableListOf<String>()

        tagsList.forEach {
            newTagsList.add(it.trimStart())
        }
        return newTagsList
    }

    private fun updateListItem(position: Int) {
        if(position < data.size) {
            binding.horizontalCategoryListRecyclerView.removeAllViews()
            adapter.setData(data, position)
            selectedCategoryIndex = position
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