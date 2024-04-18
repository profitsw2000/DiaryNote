package diarynote.template.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import diarynote.template.R
import diarynote.template.databinding.CategoryColorPickerRecyclerviewItemBinding

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
            holder.cardView.strokeWidth =
                holder.cardView.resources.getDimension(diarynote.core.R.dimen.picked_item_stroke_width).toInt()
        } else {
            holder.cardView.strokeWidth = 0
        }
    }

    inner class ViewHolder(binding: CategoryColorPickerRecyclerviewItemBinding) : RecyclerView.ViewHolder(binding.root) {

        val cardView = binding.colorPickerRecyclerViewItemCardView
    }
}