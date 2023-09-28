package diarynote.calendarfragment.presentation.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.util.Pair
import androidx.lifecycle.Observer
import com.google.android.material.chip.Chip
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import diarynote.calendarfragment.R
import diarynote.calendarfragment.databinding.FragmentCalendarBinding
import diarynote.calendarfragment.presentation.viewmodel.CalendarViewModel
import diarynote.data.domain.NOTE_MODEL_BUNDLE
import diarynote.data.model.NoteModel
import diarynote.navigator.Navigator
import diarynote.template.model.NotesState
import diarynote.template.presentation.adapter.NotesListAdapter
import diarynote.template.utils.OnNoteItemClickListener
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Date

class CalendarFragment : Fragment() {

    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding!!
    private val calendarViewModel: CalendarViewModel by viewModel()
    private val navigator: Navigator by inject()
    private val adapter = NotesListAdapter(object : OnNoteItemClickListener{
        override fun onItemClick(noteModel: NoteModel) {
            val bundle = Bundle().apply {
                putParcelable(NOTE_MODEL_BUNDLE, noteModel)
            }
            this@CalendarFragment.arguments = bundle
            navigator.navigateToNoteRead(bundle)
        }
    })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentCalendarBinding.bind(inflater.inflate(R.layout.fragment_calendar, container, false))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observeData()
        if (calendarViewModel.notesLiveData.value == null) {
            calendarViewModel.getAllNotes()
        }
    }

    private fun initViews() {
        setChipOnClickListeners()
        calendarViewModel.setSelectPeriodChipDefaultText(
            resources.getString(diarynote.core.R.string.select_period_notes_chip_text)
        )
        binding.pickedDateNotesRecyclerView.adapter = adapter
    }

    private fun observeData() {
        val observer = Observer<NotesState> { renderData(it) }
        calendarViewModel.notesLiveData.observe(viewLifecycleOwner, observer)
        val chipTextObserver = Observer<String> { setSelectPeriodChipText(it) }
        calendarViewModel.selectPeriodChipTextLiveData.observe(viewLifecycleOwner, chipTextObserver)
    }

    private fun renderData(notesState: NotesState) {
        when(notesState) {
            is NotesState.Success -> setList(notesState.noteModelList)
            is NotesState.Loading -> showProgressBar()
            is NotesState.Error -> handleError(notesState.message)
            else -> {}
        }
    }

    private fun setChipOnClickListeners() = with(binding) {
        allTimeNotesChip.setOnClickListener { calendarViewModel.getAllNotes() }
        todayNotesChip.setOnClickListener { calendarViewModel.getTodayNotes() }
        lastWeekNotesChip.setOnClickListener { calendarViewModel.getLastWeekNotes() }
        lastMonthNotesChip.setOnClickListener { calendarViewModel.getLastMonthNotes() }
        lastYearNotesChip.setOnClickListener { calendarViewModel.getLastYearNotes() }
        selectPeriodNotesChip.setOnClickListener { selectPeriodDialog() }
    }

    private fun selectPeriodDialog() {

        val dateRangePicker = MaterialDatePicker
            .Builder
            .dateRangePicker()
            .setTitleText("Выбрать временной период")
            .setSelection(
                Pair(
                    MaterialDatePicker.thisMonthInUtcMilliseconds(),
                    MaterialDatePicker.todayInUtcMilliseconds()
                )
            )
            .build()
        dateRangePicker.show(childFragmentManager, "DATE_PICKER")
        dateRangePicker.addOnPositiveButtonClickListener {
            val beginDate = Date(it.first)
            val endDate = Date(it.second)
            calendarViewModel.getNotesInDatePeriod(beginDate, endDate)
        }
    }

    private fun setSelectPeriodChipText(text: String) = with(binding) {
        selectPeriodNotesChip.text = text
    }

    private fun setList(noteModelList: List<NoteModel>) = with(binding) {
        progressBar.visibility = View.GONE
        pickedDateNotesRecyclerView.visibility = View.VISIBLE
        adapter.setData(noteModelList)
    }

    private fun showProgressBar() = with(binding) {
        pickedDateNotesRecyclerView.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
    }

    private fun handleError(message: String) = with(binding) {
        pickedDateNotesRecyclerView.visibility = View.GONE
        progressBar.visibility= View.GONE
        Snackbar.make(this.calendarFragmentRootLayout, message, Snackbar.LENGTH_INDEFINITE)
            .setAction(getString(diarynote.core.R.string.reload_notes_list_text)){
                allTimeNotesChip.isChecked = true}
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}