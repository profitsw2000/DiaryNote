package diarynote.template.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import diarynote.data.model.NoteModel
import diarynote.template.databinding.NotesListItemViewBinding
import diarynote.template.utils.OnItemClickListener
import diarynote.template.utils.OnNoteItemClickListener
import java.text.SimpleDateFormat

class NotesListAdapter(
    private val onNoteItemClickListener: OnNoteItemClickListener
) : RecyclerView.Adapter<NotesListAdapter.ViewHolder>() {

    private var data: List<NoteModel> = arrayListOf()

    fun setData(data: List<NoteModel>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesListAdapter.ViewHolder {
        val binding = NotesListItemViewBinding.inflate(LayoutInflater.from(parent.context),
            parent,
            false)

        val noteViewHolder = ViewHolder(binding)

        binding.notesListItemViewRootLayout.setOnClickListener {
            onNoteItemClickListener.onItemClick(data[noteViewHolder.adapterPosition])
        }

        return noteViewHolder
    }

    override fun onBindViewHolder(holder: NotesListAdapter.ViewHolder, position: Int) {
        val noteModel = data[position]

        with(holder) {
            title.text = noteModel.title
            content.text = noteModel.text
            tags.text = noteModel.tags.joinToString(" #", "#", "")
            date.text = SimpleDateFormat("dd.MM.yyyy").format(noteModel.date)
            if (noteModel.edited) editedImage.setImageResource(diarynote.core.R.drawable.edit_icon_24)
            else editedImage.setImageResource(0)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class ViewHolder(binding: NotesListItemViewBinding) : RecyclerView.ViewHolder(binding.root) {

        val title = binding.noteTitleTextView
        val content = binding.noteContentTextView
        val tags = binding.tagsTextView
        val date = binding.creationDateTextView
        val editedImage = binding.editedNoteSignImageView
    }
}