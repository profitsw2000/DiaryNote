package diarynote.addcategoryscreen.presentation.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import diarynote.addcategoryscreen.databinding.CategoryColorPickerRecyclerviewItemBinding
import diarynote.core.utils.listener.OnItemClickListener

class ColorListAdapter (
    private val onItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<ColorListAdapter.ViewHolder>() {

    private lateinit var binding: CategoryColorPickerRecyclerviewItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = CategoryColorPickerRecyclerviewItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false)
        return ViewHolder(binding.root)
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    }
}