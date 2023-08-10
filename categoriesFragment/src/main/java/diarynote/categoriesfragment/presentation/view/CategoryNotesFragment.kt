package diarynote.categoriesfragment.presentation.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import diarynote.categoriesfragment.R
import diarynote.categoriesfragment.databinding.FragmentCategoryNotesBinding

class CategoryNotesFragment : Fragment() {

    private var _binding: FragmentCategoryNotesBinding? = null
    private val binding = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCategoryNotesBinding.inflate(inflater)
        return binding.root
    }
}