package diarynote.readnotescreen.presentation.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import diarynote.readnotescreen.R
import diarynote.readnotescreen.databinding.FragmentReadNoteBinding

class ReadNoteFragment : Fragment() {

    private var _binding: FragmentReadNoteBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentReadNoteBinding.bind(inflater.inflate(R.layout.fragment_read_note, container, false))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun observeData() {

    }
}