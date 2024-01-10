package diarynote.template.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import diarynote.data.model.NoteModel
import diarynote.template.databinding.NotesListItemViewBinding
import diarynote.template.utils.OnNoteItemClickListener
import java.text.SimpleDateFormat

class NotesPagedListAdapter(
    private val onNoteItemClickListener: OnNoteItemClickListener
) : PagingDataAdapter<NoteModel, NotesPagedListAdapter.NoteItemViewHolder>(NoteDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteItemViewHolder {
        val binding = NotesListItemViewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false)

        val noteViewHolder = NoteItemViewHolder(binding)

        binding.notesListItemViewRootLayout.setOnClickListener {
            getItem(noteViewHolder.adapterPosition)?.let { it1 ->
                onNoteItemClickListener.onItemClick(
                    it1
                )
            }
        }

        return noteViewHolder
    }

    override fun onBindViewHolder(holder: NoteItemViewHolder, position: Int) {
        val noteModel = getItem(position)

        if (noteModel != null) {
            with(holder) {
                title.text = noteModel.title
                content.text = noteModel.text
                tags.text = noteModel.tags.joinToString(" #", "#", "")
                date.text = SimpleDateFormat("dd.MM.yyyy").format(noteModel.date)
                if (noteModel.edited) editedImage.setImageResource(diarynote.core.R.drawable.edit_icon_24)
                else editedImage.setImageResource(0)
            }
        }
    }

    inner class NoteItemViewHolder(val binding: NotesListItemViewBinding) : RecyclerView.ViewHolder(binding.root) {
        val title = binding.noteTitleTextView
        val content = binding.noteContentTextView
        val tags = binding.tagsTextView
        val date = binding.creationDateTextView
        val editedImage = binding.editedNoteSignImageView
    }

    class NoteDiffCallback : DiffUtil.ItemCallback<NoteModel>() {
        override fun areItemsTheSame(oldItem: NoteModel, newItem: NoteModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: NoteModel, newItem: NoteModel): Boolean {
            return oldItem == newItem
        }
    }
}