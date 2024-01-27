package diarynote.mainfragment.presentation.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.snackbar.Snackbar
import diarynote.data.domain.NOTE_MODEL_BUNDLE
import diarynote.data.model.NoteModel
import diarynote.data.model.state.NotesCountChangeState
import diarynote.data.model.state.NotesState
import diarynote.mainfragment.R
import diarynote.mainfragment.databinding.FragmentMainBinding
import diarynote.mainfragment.presentation.viewmodel.HomeViewModel
import diarynote.navigator.Navigator
import diarynote.template.presentation.adapter.NotesPagedListAdapter
import diarynote.template.utils.OnNoteItemClickListener
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by viewModel()
    private val navigator: Navigator by inject()
    private var isCreated = true

    private val adapter = NotesPagedListAdapter(object : OnNoteItemClickListener{
        override fun onItemClick(noteModel: NoteModel) {
            val bundle = Bundle().apply {
                putParcelable(NOTE_MODEL_BUNDLE, noteModel)
            }
            this@MainFragment.arguments = bundle
            navigator.actionMainToReadNote(bundle)
        }
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCreated = (savedInstanceState == null)
    }

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
        if (savedInstanceState == null) {
            if (isCreated) {
                isCreated = false
                observeData()
            } else {
                homeViewModel.checkUserNotesCountChanged()
                observeChanges()
            }
        } else {
            observeData()
        }
    }

    private fun initViews() {
        with(binding) {
            mainNotesListRecyclerView.adapter = adapter
            mainNotesListRecyclerView.setHasFixedSize(false)
            addNoteFab.setOnClickListener {
                navigator.actionMainToCreateNote()
            }
            searchNoteTextInputLayout.setEndIconOnClickListener {
                val search = searchInputEditText.text.toString()
                homeViewModel.getSearchNotesPagedList(search)
                observeData()
            }
        }
    }

    private fun observeData() {
        homeViewModel.notesState.observe(viewLifecycleOwner) {
            when (it) {
                is NotesState.Error -> handleError(it.message)
                NotesState.Loaded -> setProgressBarVisible(false)
                NotesState.Loading -> setProgressBarVisible(true)
                is NotesState.Success -> setProgressBarVisible(false)
            }
        }

        homeViewModel.notesPagedList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    private fun observeChanges() {
        homeViewModel.userNotesCountChanged.observe(viewLifecycleOwner) {
            when(it) {
                is NotesCountChangeState.Error -> {}
                NotesCountChangeState.Loading -> {}
                is NotesCountChangeState.Success -> {
                    if (it.notesCountChanged) {
                        homeViewModel.getUserNotesPagedList()
                        observeData()
                        binding.searchNoteTextInputLayout.editText?.setText("")
                    } else {
                        observeData()
                    }
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
        } else {
            progressBar.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}