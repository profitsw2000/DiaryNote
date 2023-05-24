package diarynote.readnotescreen.presentation.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import diarynote.data.domain.NOTE_MODEL_BUNDLE
import diarynote.data.model.NoteModel
import diarynote.readnotescreen.R
import diarynote.readnotescreen.databinding.FragmentReadNoteBinding
import java.text.SimpleDateFormat
import java.util.*

class ReadNoteFragment : Fragment() {

    private var _binding: FragmentReadNoteBinding? = null
    private val binding get() = _binding!!
    private var noteModel: NoteModel? = null
    private var title: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        arguments?.let {
            noteModel = it.getParcelable(NOTE_MODEL_BUNDLE)
            //title = it.getParcelable(NOTE_MODEL_BUNDLE)
        }
        _binding = FragmentReadNoteBinding.bind(inflater.inflate(R.layout.fragment_read_note, container, false))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //binding.noteTitleTextView.text = title
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