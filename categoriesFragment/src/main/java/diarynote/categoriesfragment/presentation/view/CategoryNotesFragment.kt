package diarynote.categoriesfragment.presentation.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import diarynote.categoriesfragment.R
import diarynote.categoriesfragment.databinding.FragmentCategoryNotesBinding
import diarynote.categoriesfragment.presentation.viewmodel.CategoriesViewModel
import diarynote.core.common.dialog.data.DialogerImpl
import diarynote.core.utils.listener.OnDialogPositiveButtonClickListener
import diarynote.data.domain.CATEGORY_ID_BUNDLE
import diarynote.data.domain.CATEGORY_MODEL_BUNDLE
import diarynote.data.domain.CATEGORY_NAME_BUNDLE
import diarynote.data.domain.NOTE_MODEL_BUNDLE
import diarynote.data.model.CategoryModel
import diarynote.data.model.NoteModel
import diarynote.data.model.state.CategoryDeleteState
import diarynote.data.model.state.NotesState
import diarynote.navigator.Navigator
import diarynote.template.presentation.adapter.NotesPagedListAdapter
import diarynote.template.utils.OnNoteItemClickListener
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class CategoryNotesFragment : Fragment() {

    private var _binding: FragmentCategoryNotesBinding? = null
    private val binding get() = _binding!!
    private val navigator: Navigator by inject()
    private val categoriesViewModel: CategoriesViewModel by viewModel()
    private val categoryId: Int? by lazy { arguments?.getInt(CATEGORY_ID_BUNDLE) }
    private val categoryName: String? by lazy { arguments?.getString(CATEGORY_NAME_BUNDLE) }
    private val categoryModel: CategoryModel? by lazy { arguments?.getParcelable(CATEGORY_MODEL_BUNDLE) }
    private val adapter = NotesPagedListAdapter(object : OnNoteItemClickListener{
        override fun onItemClick(noteModel: NoteModel) {
            val bundle = Bundle().apply {
                putParcelable(NOTE_MODEL_BUNDLE, noteModel)
            }
            this@CategoryNotesFragment.arguments = bundle
            navigator.actionCategoryNotesToReadNote(bundle)
        }
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = categoryName
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentCategoryNotesBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        categoryId?.let { categoriesViewModel.getCategoryNotesPagedList(it) }
        initViews()
        observeData()
        observeCategoryDeleteData()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.top_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.delete -> {
                deleteCategory()
                true
            }
            R.id.edit -> {
                editCategory()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initViews() = with(binding) {
        categoryNotesRecyclerView.adapter = adapter
        categoryNotesRecyclerView.setHasFixedSize(false)
    }

    private fun observeData() {
        categoriesViewModel.notesState.observe(viewLifecycleOwner) {
            when (it) {
                is NotesState.Error -> handleError(it.message)
                NotesState.Loaded -> setProgressBarVisible(false)
                NotesState.Loading -> setProgressBarVisible(true)
                is NotesState.Success -> setProgressBarVisible(false)
            }
        }

        categoriesViewModel.notesPagedList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    private fun observeCategoryDeleteData() {
        categoriesViewModel.categoryDeleteLiveData.observe(viewLifecycleOwner) {
            when (it) {
                CategoryDeleteState.Deleting -> setProgressBarVisible(true)
                is CategoryDeleteState.Error -> handleError(it.message)
                CategoryDeleteState.Idle -> {}
                CategoryDeleteState.Success -> successfulCategoryDelete()
            }
        }

        categoriesViewModel.notesPagedList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    private fun handleError(message: String) = with(binding) {
        setProgressBarVisible(false)
        Snackbar.make(this.categoryNotesFragmentRootLayout, message, Snackbar.LENGTH_INDEFINITE)
            .setAction(getString(diarynote.core.R.string.reload_notes_list_text)) { categoryId?.let { it1 ->
                categoriesViewModel.getCategoryNotesPagedList(
                    it1
                )
            } }
            .show()
    }

    private fun setProgressBarVisible(visible: Boolean) = with(binding) {
        if (visible) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }

    private fun deleteCategory() {
        val dialoger = DialogerImpl(
            requireActivity(),
            onDialogPositiveButtonClickListener = object  : OnDialogPositiveButtonClickListener{
                override fun onClick() {
                    categoryModel?.let { categoriesViewModel.deleteCategory(it) }
                }
            }
        )

        dialoger.showTwoButtonDialog(getString(diarynote.core.R.string.delete_category_dialog_title_text),
            getString(diarynote.core.R.string.delete_category_dialog_message_text),
            getString(diarynote.core.R.string.dialog_button_yes_text),
            getString(diarynote.core.R.string.dialog_button_no_text)
        )
    }

    private fun editCategory() {
        val dialoger = DialogerImpl(
            requireActivity(),
            onDialogPositiveButtonClickListener = object  : OnDialogPositiveButtonClickListener{
                override fun onClick() {
                    val bundle = Bundle().apply {
                        putParcelable(CATEGORY_MODEL_BUNDLE, categoryModel)
                    }
                    this@CategoryNotesFragment.arguments = bundle
                    navigator.actionCategoryNotesToEditCategory(bundle)
                }
            }
        )

        dialoger.showTwoButtonDialog(getString(diarynote.core.R.string.edit_category_dialog_title_text),
            getString(diarynote.core.R.string.edit_category_dialog_message_text),
            getString(diarynote.core.R.string.dialog_button_yes_text),
            getString(diarynote.core.R.string.dialog_button_no_text)
        )
    }

    private fun successfulCategoryDelete() = with(binding) {
        val dialoger = DialogerImpl(requireActivity(), object : OnDialogPositiveButtonClickListener {
            override fun onClick() {
                navigator.navigateUp()
            }
        })

        setProgressBarVisible(false)
        categoriesViewModel.idleDelete()

        dialoger.showAlertDialog(getString(diarynote.core.R.string.delete_category_dialog_title_text),
            getString(diarynote.core.R.string.delete_category_success_dialog_message_text),
            getString(diarynote.core.R.string.dialog_button_ok_text)
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}