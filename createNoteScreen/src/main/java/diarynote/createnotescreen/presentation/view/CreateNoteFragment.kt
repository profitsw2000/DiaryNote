package diarynote.createnotescreen.presentation.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.Observer
import diarynote.core.common.dialog.data.DialogerImpl
import diarynote.core.utils.*
import diarynote.core.utils.listener.OnDialogPositiveButtonClickListener
import diarynote.createnotescreen.R
import diarynote.createnotescreen.databinding.FragmentCreateNoteBinding
import diarynote.createnotescreen.model.CategoriesState
import diarynote.createnotescreen.model.NotesState
import diarynote.createnotescreen.presentation.view.adapter.HorizontalCategoryListAdapter
import diarynote.createnotescreen.presentation.view.utils.OnItemClickListener
import diarynote.createnotescreen.presentation.viewmodel.CreateNoteViewModel
import diarynote.data.model.CategoryModel
import diarynote.navigator.Navigator
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class CreateNoteFragment : Fragment() {

    private var _binding: FragmentCreateNoteBinding? = null
    private val binding get() = _binding!!
    private lateinit var data: List<CategoryModel>
    private var selectedCategoryIndex = 0
    private val createNoteViewModel: CreateNoteViewModel by viewModel()
    private val navigator: Navigator by inject()
    private val adapter = HorizontalCategoryListAdapter(object : OnItemClickListener {
        override fun onItemClick(position: Int) {
            updateListItem(position)
        }
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                exitNoteCreationDialog()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            exitNoteCreationDialog()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun exitNoteCreationDialog() {
        val dialoger =
            DialogerImpl(requireActivity(), object : OnDialogPositiveButtonClickListener {
                override fun onClick() {
                    navigator.navigateUp()
                }
            })

        dialoger.showTwoButtonDialog(getString(diarynote.core.R.string.exit_note_creation_dialog_title_text), getString(
                    diarynote.core.R.string.exit_note_creation_dialog_message_text), getString(
            diarynote.core.R.string.dialog_button_yes_text), getString(
                                diarynote.core.R.string.dialog_button_no_text))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
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
                data[selectedCategoryIndex].categoryName,
                data[selectedCategoryIndex].id
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
        val noteObserver = Observer<NotesState?> { renderNotesData(it)}
        createNoteViewModel.notesLiveData.observe(viewLifecycleOwner, noteObserver)
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
            is NotesState.Success -> successfulNoteCreation()
            is NotesState.Loading -> showProgressBar()
            is NotesState.Error -> noteCreationError(notesState)
            else -> {}
        }
    }

    private fun successfulNoteCreation() = with(binding) {
        val dialoger = DialogerImpl(requireActivity(), object : OnDialogPositiveButtonClickListener {
            override fun onClick() {
                navigator.navigateUp()
                clearInputForms()
                createNoteViewModel.clear()
            }
        })

        progressBar.visibility = View.GONE

        dialoger.showAlertDialog(getString(diarynote.core.R.string.create_note_success_dialog_title_text),
            getString(diarynote.core.R.string.create_note_success_dialog_message_text),
            getString(diarynote.core.R.string.dialog_button_ok_text)
        )
    }

    private fun FragmentCreateNoteBinding.clearInputForms() {
        noteTitleInputLayout.editText?.setText("")
        noteContentInputLayout.editText?.setText("")
        noteTagsInputLayout.editText?.setText("")
    }

    private fun loadingCategoriesSuccess(categoriesState: CategoriesState.Success)  = with(binding) {
        progressBar.visibility = View.GONE
        data = categoriesState.categoryModelList
        adapter.setData(data)
    }

    private fun noteCreationError(notesState: NotesState.Error) = with(binding) {
        val dialoger = DialogerImpl(requireActivity())
        progressBar.visibility = View.GONE

        if((1 shl NOTE_TITLE_BIT_NUMBER) and notesState.errorCode != 0) noteTitleInputLayout.error = getString(diarynote.core.R.string.input_note_title_error_text, NOTE_TITLE_MIN_LENGTH.toString())
        if((1 shl NOTE_CONTENT_BIT_NUMBER) and notesState.errorCode != 0) noteContentInputLayout.error = getString(diarynote.core.R.string.input_note_content_error_text, NOTE_CONTENT_MIN_LENGTH.toString())
        if((1 shl NOTE_TAGS_BIT_NUMBER) and notesState.errorCode != 0) noteTagsInputLayout.error = getString(diarynote.core.R.string.input_note_tags_line_error_text, NOTE_TAGS_MIN_LENGTH.toString())
        if((1 shl NOTE_TAG_WORDS_BIT_NUMBER) and notesState.errorCode != 0) noteTagsInputLayout.error = getString(diarynote.core.R.string.input_note_tags_words_error_text, NOTE_TAG_WORDS_LIMIT.toString())
        if((1 shl ROOM_BIT_NUMBER) and notesState.errorCode != 0) dialoger.showAlertDialog(getString(diarynote.core.R.string.error_dialog_title_text), getString(diarynote.core.R.string.room_note_adding_error_message), getString(diarynote.core.R.string.dialog_button_ok_text))
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
            selectedCategoryIndex = position
        } else {
            navigator.navigateToCategoryCreation()
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