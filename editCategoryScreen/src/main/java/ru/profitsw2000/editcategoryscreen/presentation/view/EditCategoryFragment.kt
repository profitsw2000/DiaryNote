package ru.profitsw2000.editcategoryscreen.presentation.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import diarynote.template.databinding.FragmentCategoryCreationBinding
import ru.profitsw2000.editcategoryscreen.R
import ru.profitsw2000.editcategoryscreen.databinding.FragmentEditCategoryBinding


class EditCategoryFragment : Fragment() {

    private var _binding: FragmentCategoryCreationBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_category, container, false)
    }
}