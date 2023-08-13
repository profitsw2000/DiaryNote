package diarynote.addcategoryscreen.presentation.view.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import diarynote.addcategoryscreen.databinding.CategoryColorPickerRecyclerviewItemBinding
import diarynote.addcategoryscreen.model.ColorModel
import diarynote.core.utils.listener.OnItemClickListener

class ColorListAdapter () : RecyclerView.Adapter<ColorListAdapter.ViewHolder>() {

    private var data: List<Int> = arrayListOf()
    var clickedPosition = 0

    fun setData(data: List<Int>, clickedPosition: Int) {
        this.data = data
        this.clickedPosition = clickedPosition
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding = CategoryColorPickerRecyclerviewItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false)
        val colorViewHolder = ViewHolder(binding)

        binding.root.setOnClickListener {
            val position = colorViewHolder.adapterPosition
            if (position != clickedPosition) {
                notifyItemChanged(position)
                notifyItemChanged(clickedPosition)
                clickedPosition = position
            }
        }
        return colorViewHolder
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.cardView.setCardBackgroundColor(data[position])
        if (clickedPosition == position) {
            holder.cardView.strokeWidth = 10
        } else {
            holder.cardView.strokeWidth = 0
        }
    }

    inner class ViewHolder(binding: CategoryColorPickerRecyclerviewItemBinding) : RecyclerView.ViewHolder(binding.root) {

        val cardView = binding.colorPickerRecyclerViewItemCardView
    }
}