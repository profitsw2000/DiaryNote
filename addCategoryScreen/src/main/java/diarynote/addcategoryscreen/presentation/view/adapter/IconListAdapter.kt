package diarynote.addcategoryscreen.presentation.view.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.color.MaterialColors
import diarynote.addcategoryscreen.databinding.CategoryIconPickerRecyclerviewItemBinding
import diarynote.addcategoryscreen.model.IconModel
import diarynote.core.utils.listener.OnItemClickListener

class IconListAdapter () : RecyclerView.Adapter<IconListAdapter.ViewHolder>() {

    private lateinit var binding: CategoryIconPickerRecyclerviewItemBinding
    private var data: List<IconModel> = arrayListOf()
    private var lastClickedPosition = 0
    private lateinit var context: Context

    fun setData(data: List<IconModel>) {
        this.data = data
        notifyDataSetChanged()
    }

    fun getClickedPosition(): Int {
        return lastClickedPosition
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = CategoryIconPickerRecyclerviewItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false)
        context = parent.context
        val iconViewHolder = ViewHolder(binding)

        binding.root.setOnClickListener {
            val position = iconViewHolder.adapterPosition
            if (position != lastClickedPosition) {
                data[position].isSelected = true
                data[lastClickedPosition].isSelected = false
                notifyItemChanged(position)
                notifyItemChanged(lastClickedPosition)
                lastClickedPosition = position
            }
        }
        return iconViewHolder
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.imageView.setImageResource(getImageFromResources(data[position].icon))
        if (data[position].isSelected) {
            holder.cardView.setCardBackgroundColor(ContextCompat.getColor(context, diarynote.core.R.color.deep_purple_100))
        } else {
            holder.cardView.setCardBackgroundColor(ContextCompat.getColor(context, diarynote.core.R.color.white))
        }
    }

    private fun getImageFromResources(imgId: Int) : Int {
        return when(imgId) {
            0 -> diarynote.core.R.drawable.bottom_nav_categories_icon
            1 -> diarynote.core.R.drawable.work_icon_outline_24
            2 -> diarynote.core.R.drawable.tech_icon_24
            3 -> diarynote.core.R.drawable.auto_icon_24
            4 -> diarynote.core.R.drawable.docs_icon_24
            5 -> diarynote.core.R.drawable.android_icon_24
            else -> diarynote.core.R.drawable.add_icon_24
        }
    }

    inner class ViewHolder(binding: CategoryIconPickerRecyclerviewItemBinding) : RecyclerView.ViewHolder(binding.root) {

        val cardView = binding.iconPickerRecyclerViewItemCardView
        val imageView = binding.iconPickerItemImageView

    }
}