package diarynote.editnotescreen.presentation.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import diarynote.data.model.CategoryModel
import diarynote.editnotescreen.R
import diarynote.editnotescreen.databinding.FragmentEditNoteBinding
import org.koin.android.ext.android.get

class EditNoteFragment : Fragment() {

    private var _binding: FragmentEditNoteBinding? = null
    private val binding get() = _binding!!
    private lateinit var data: List<CategoryModel>

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

    companion object {
        @JvmStatic
        fun newInstance() = EditNoteFragment()
    }
}