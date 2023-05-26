package diarynote.readnotescreen.presentation.view

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import diarynote.core.common.dialog.data.DialogerImpl
import diarynote.core.utils.listener.OnDialogPositiveButtonClickListener
import diarynote.data.domain.NOTE_MODEL_BUNDLE
import diarynote.data.model.NoteModel
import diarynote.data.model.state.NotesState
import diarynote.readnotescreen.R
import diarynote.readnotescreen.databinding.FragmentReadNoteBinding
import diarynote.readnotescreen.presentation.viewmodel.ReadNoteViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*

class ReadNoteFragment : Fragment() {

    private var _binding: FragmentReadNoteBinding? = null
    private val binding get() = _binding!!
    private val readNoteViewModel: ReadNoteViewModel by viewModel()
    private val noteModel: NoteModel? by lazy { arguments?.getParcelable(NOTE_MODEL_BUNDLE) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentReadNoteBinding.bind(inflater.inflate(R.layout.fragment_read_note, container, false))
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.top_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.delete -> {
                deleteNotePressed()
                true
            }
            R.id.edit -> {
                editNotePressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun editNotePressed() {
        if (noteModel != null){
            val bundle = Bundle().apply {
                putParcelable(NOTE_MODEL_BUNDLE, noteModel)
            }
            this@ReadNoteFragment.arguments = bundle
            readNoteViewModel.navigateToEditNoteFragment(bundle)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (noteModel != null) {
            populateViews(noteModel!!)
        } else {
            handleError()
        }
        observeData()
    }

    private fun observeData() {
        val observer = androidx.lifecycle.Observer<NotesState?> { renderData(it) }
        readNoteViewModel.notesLiveData.observe(viewLifecycleOwner, observer)
    }

    private fun renderData(notesState: NotesState?) {
        when (notesState) {
            is NotesState.Success -> {
                readNoteViewModel.clear()
                requireActivity().onBackPressed()
            }
            is NotesState.Loading -> {}
            is NotesState.Error -> Toast.makeText(requireActivity(), notesState.message, Toast.LENGTH_SHORT).show()
            else -> {}
        }
    }

    private fun populateViews(noteModel: NoteModel) = with(binding) {
        noteTitleTextView.text = noteModel.title
        noteContentTextView.text = noteModel.text
        noteTagsTextView.text = noteModel.tags.joinToString(" #", "#", "")
        noteCreateDateTextView.text = getDateString(noteModel.date)
        if (noteModel.edited) noteEditDateTextView.text = getString(R.string.edit_note_date_text, getDateString(noteModel.editDate))
    }

    private fun getDateString(date: Date): String? {
        return SimpleDateFormat("dd.MM.yyyy").format(date)
    }

    private fun handleError() = with(binding) {
        noteTitleTextView.text = getString(R.string.read_note_error_text)
    }

    private fun deleteNotePressed() {
        val dialoger = DialogerImpl(
            requireActivity(),
            onDialogPositiveButtonClickListener = object : OnDialogPositiveButtonClickListener {
                override fun onClick() {
                    noteModel?.let { readNoteViewModel.deleteNote(it) }
                }
            }
        )

        dialoger.showTwoButtonDialog(getString(diarynote.core.R.string.delete_note_dialog_title_text),
            getString(diarynote.core.R.string.delete_note_dialog_message_text),
            getString(diarynote.core.R.string.dialog_button_yes_text),
            getString(diarynote.core.R.string.dialog_button_no_text)
        )
    }

    private fun editNoteRequested() {

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        fun newInstance() = ReadNoteFragment()
    }
}