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
import diarynote.data.model.state.NotesCountChangeState
import diarynote.data.model.state.NotesState
import diarynote.mainfragment.R
import diarynote.mainfragment.databinding.FragmentMainBinding
import diarynote.mainfragment.presentation.viewmodel.HomeViewModel
import diarynote.navigator.Navigator
import diarynote.template.presentation.adapter.NotesListAdapter
import diarynote.template.presentation.adapter.NotesPagedListAdapter
import diarynote.template.utils.OnNoteItemClickListener
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by viewModel()
    private val navigator: Navigator by inject()

    private val adapter = NotesPagedListAdapter(object : OnNoteItemClickListener{
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
        homeViewModel.getUserNotesPagedList()
    }

    private fun initViews() {
        with(binding) {
            mainNotesListRecyclerView.adapter = adapter
            mainNotesListRecyclerView.setHasFixedSize(false)
            addNoteFab.setOnClickListener {
                navigator.navigateToNoteCreation()
            }
            searchNoteTextInputLayout.setEndIconOnClickListener {
                val search = searchInputEditText.text.toString()
                homeViewModel.getSearchNotesPagedList(search)
                observeData()
            }
        }
    }

    private fun observeData() {
        homeViewModel.notesState.observe(this) {
            when (it) {
                is NotesState.Error -> handleError(it.message)
                NotesState.Loaded -> setProgressBarVisible(false)
                NotesState.Loading -> setProgressBarVisible(true)
                is NotesState.Success -> setProgressBarVisible(false)
            }
        }

        homeViewModel.notesPagedList.observe(this) {
            adapter.submitList(it)
        }

        homeViewModel.userNotesCountChanged.observe(this) {
            when(it) {
                is NotesCountChangeState.Error -> {}
                NotesCountChangeState.Loading -> {}
                is NotesCountChangeState.Success -> if (it.notesCountChanged) {
                    homeViewModel.getUserNotesPagedList()
                }
            }
        }
    }

    private fun handleError(message: String) = with(binding) {
        setProgressBarVisible(false)
        Snackbar.make(this.mainFragmentRootLayout, message, Snackbar.LENGTH_INDEFINITE)
            .setAction(getString(diarynote.core.R.string.reload_notes_list_text)) { homeViewModel.getUserNotesPagedList() }
            .show()
    }

    private fun setProgressBarVisible(visible: Boolean) = with(binding) {
        if (visible) {
            progressBar.visibility = View.VISIBLE
            mainNotesListRecyclerView.visibility = View.GONE
            searchNoteTextInputLayout.visibility = View.GONE
        } else {
            progressBar.visibility = View.GONE
            mainNotesListRecyclerView.visibility = View.VISIBLE
            searchNoteTextInputLayout.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}