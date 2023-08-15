package diarynote.mainfragment.presentation.view.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import diarynote.data.model.NoteModel
import diarynote.mainfragment.databinding.NotesListItemViewBinding
import diarynote.mainfragment.presentation.view.utils.OnItemClickListener
import java.text.SimpleDateFormat

class NotesListAdapter(
    private val onItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<NotesListAdapter.ViewHolder>() {

    private lateinit var binding: NotesListItemViewBinding
    private var data: List<NoteModel> = arrayListOf()

    fun setData(data: List<NoteModel>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesListAdapter.ViewHolder {
        binding = NotesListItemViewBinding.inflate(LayoutInflater.from(parent.context),
            parent,
            false)
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: NotesListAdapter.ViewHolder, position: Int) {
        holder.bind(data[position], position)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(noteModel: NoteModel, position: Int) {
            with(binding) {
                noteTitleTextView.text = noteModel.title
                noteContentTextView.text = noteModel.text
                tagsTextView.text = noteModel.tags.joinToString(" #", "#", "")
                creationDateTextView.text = SimpleDateFormat("dd.MM.yyyy").format(noteModel.date)
                if (noteModel.edited) editedNoteSignImageView.setImageResource(diarynote.core.R.drawable.edit_icon_24)

                notesListItemViewRootLayout.setOnClickListener {
                    onItemClickListener.onItemClick(noteModel)
                }
            }
        }
    }
}