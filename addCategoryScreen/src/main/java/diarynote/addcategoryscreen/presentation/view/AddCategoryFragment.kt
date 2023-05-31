package diarynote.addcategoryscreen.presentation.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import diarynote.addcategoryscreen.R
import diarynote.addcategoryscreen.databinding.FragmentAddCategoryBinding
import diarynote.addcategoryscreen.model.ColorModel
import diarynote.addcategoryscreen.model.IconModel

class AddCategoryFragment : Fragment() {

    private var _binding: FragmentAddCategoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var colorData: List<ColorModel>
    private lateinit var iconData: List<IconModel>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAddCategoryBinding.bind(inflater.inflate(R.layout.fragment_add_category, container, false))
        return binding.root
    }

    private fun initViews() = with(binding) {

    }

    companion object {
        @JvmStatic
        fun newInstance() = AddCategoryFragment()
    }
}