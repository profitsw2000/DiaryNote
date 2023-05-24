package diarynote.readnotescreen.presentation.view

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import diarynote.data.domain.NOTE_MODEL_BUNDLE
import diarynote.data.model.NoteModel
import diarynote.readnotescreen.R
import diarynote.readnotescreen.databinding.FragmentReadNoteBinding
import java.text.SimpleDateFormat
import java.util.*

class ReadNoteFragment : Fragment() {

    private var _binding: FragmentReadNoteBinding? = null
    private val binding get() = _binding!!
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
                true
            }
            R.id.edit -> {
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (noteModel != null) {
            populateViews(noteModel!!)
        } else {
            handleError()
        }
    }

    private fun populateViews(noteModel: NoteModel) = with(binding) {
        noteTitleTextView.text = noteModel.title
        noteContentTextView.text = noteModel.text
        noteTagsTextView.text = noteModel.tags.joinToString(" #", "#", "")
        noteCreateDateTextView.text = getDateString(noteModel.date)
        if (noteModel.edited) noteEditDateTextView.text = "Изменено: ${getDateString(noteModel.editDate)}"
    }

    private fun getDateString(date: Date): String? {
        return SimpleDateFormat("dd.MM.yyyy").format(date)
    }

    private fun handleError() = with(binding) {
        noteTitleTextView.text = "Не удалось загрузить заметку. Попробуйте еще раз."
    }
}