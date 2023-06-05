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

    private var data: List<ColorModel> = arrayListOf()
    private var lastClickedPosition = 0

    fun setData(data: List<ColorModel>) {
        this.data = data
        notifyDataSetChanged()
    }

    fun getClickedPosition(): Int {
        return lastClickedPosition
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding = CategoryColorPickerRecyclerviewItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false)
        val colorViewHolder = ViewHolder(binding)

        binding.root.setOnClickListener {
            val position = colorViewHolder.adapterPosition
            if (position != lastClickedPosition) {
                data[position].isSelected = true
                data[lastClickedPosition].isSelected = false
                notifyItemChanged(position)
                notifyItemChanged(lastClickedPosition)
                lastClickedPosition = position
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

        holder.cardView.setCardBackgroundColor(data[position].color)
        if (data[position].isSelected) {
            holder.cardView.strokeWidth = 10
        } else {
            holder.cardView.strokeWidth = 0
        }
    }

    inner class ViewHolder(binding: CategoryColorPickerRecyclerviewItemBinding) : RecyclerView.ViewHolder(binding.root) {

        val cardView = binding.colorPickerRecyclerViewItemCardView
    }
}