package diarynote.mainfragment.presentation.view

import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Note
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import diarynote.data.model.NoteModel
import diarynote.mainfragment.R
import diarynote.mainfragment.databinding.FragmentMainBinding
import diarynote.mainfragment.model.NotesState
import diarynote.mainfragment.presentation.view.adapter.NotesListAdapter
import diarynote.mainfragment.presentation.viewmodel.HomeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by viewModel()
    private val adapter = NotesListAdapter()

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
                homeViewModel.navigateToNoteCreation()
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