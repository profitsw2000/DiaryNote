package diarynote.mainfragment.presentation.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import diarynote.data.domain.NOTE_MODEL_BUNDLE
import diarynote.data.model.NoteModel
import diarynote.mainfragment.R
import diarynote.mainfragment.databinding.FragmentMainBinding
import diarynote.mainfragment.presentation.viewmodel.HomeViewModel
import diarynote.navigator.Navigator
import diarynote.template.model.NotesState
import diarynote.template.presentation.adapter.NotesListAdapter
import diarynote.template.utils.OnNoteItemClickListener
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by viewModel()
    private val navigator: Navigator by inject()
    private val adapter = NotesListAdapter(object : OnNoteItemClickListener{
        override fun onItemClick(noteModel: NoteModel) {
            val bundle = Bundle().apply {
                putParcelable(NOTE_MODEL_BUNDLE, noteModel)
            }
            this@MainFragment.arguments = bundle
            navigator.navigateToNoteRead(bundle)
        }
    })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentMainBinding.bind(inflater.inflate(R.layout.fragment_main, container, false))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observeData()
        homeViewModel.getNotesList()
    }

    private fun initViews() {
        with(binding) {
            mainNotesListRecyclerView.adapter = adapter
            addNoteFab.setOnClickListener {
                navigator.navigateToNoteCreation()
            }
            searchNoteTextInputLayout.setEndIconOnClickListener {
                val search = searchInputEditText.text.toString()
                homeViewModel.getUserNotesWithWordInTags(search)
                Toast.makeText(requireContext(), search, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun observeData() {
        val observer = Observer<NotesState> { renderData(it)}
        homeViewModel.notesLiveData.observe(viewLifecycleOwner, observer)
    }

    private fun renderData(notesState: NotesState) {
        when(notesState) {
            is NotesState.Success -> setList(notesState.noteModelList)
            is NotesState.Loading -> showProgressBar()
            is NotesState.Error -> handleError(notesState.message)
        }
    }

    private fun setList(noteModelList: List<NoteModel>) {
        with(binding) {
            mainNotesListRecyclerView.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
        }
        if (noteModelList.isEmpty()) Toast.makeText(requireContext(), "Empty search list", Toast.LENGTH_SHORT).show()
        adapter.setData(noteModelList)
    }

    private fun handleError(message: String) = with(binding) {
        Snackbar.make(this.mainFragmentRootLayout, message, Snackbar.LENGTH_INDEFINITE)
            .setAction(getString(diarynote.core.R.string.reload_notes_list_text)) { homeViewModel.getNotesList() }
            .show()
    }

    private fun showProgressBar() {
        with(binding) {
            mainNotesListRecyclerView.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = MainFragment()
    }
}