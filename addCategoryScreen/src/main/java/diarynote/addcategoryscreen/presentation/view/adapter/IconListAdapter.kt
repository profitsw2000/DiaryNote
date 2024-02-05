package diarynote.addcategoryscreen.presentation.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import diarynote.addcategoryscreen.databinding.CategoryIconPickerRecyclerviewItemBinding
import diarynote.core.utils.listener.OnItemClickListener

class IconListAdapter (
    private val onItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<IconListAdapter.ViewHolder>() {

    private lateinit var binding: CategoryIconPickerRecyclerviewItemBinding
    private var data: List<Int> = arrayListOf()
    private lateinit var context: Context
    var clickedPosition = 0
    private var pickedIconPath = ""

    fun setData(data: List<Int>, clickedPosition: Int) {
        this.data = data
        this.clickedPosition = clickedPosition
        notifyDataSetChanged()
    }

    fun updateIconImage(iconPath: String) {
        pickedIconPath = iconPath
        notifyItemChanged(data.last())
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
            val oldPosition = clickedPosition
            if (position != clickedPosition) {
                clickedPosition = position
                notifyItemChanged(position)
                notifyItemChanged(oldPosition)
            }
            onItemClickListener.onItemClick(position)
        }
        return iconViewHolder
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if (position != data.last()) holder.imageView.setImageResource(getImageFromResources(data[position]))
        else setLastItemIcon(holder, position)

        if (clickedPosition == position) {
            holder.cardView.setCardBackgroundColor(ContextCompat.getColor(context, diarynote.core.R.color.purple_200))
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

    private fun setLastItemIcon(holder: ViewHolder, position: Int) {
        if (pickedIconPath == "") holder.imageView.setImageResource(getImageFromResources(data[position]))
        else {
            //set image by it's path using Picasso or smthng that kind
            Picasso.get().load(pickedIconPath).into(holder.imageView)
        }
    }

    inner class ViewHolder(binding: CategoryIconPickerRecyclerviewItemBinding) : RecyclerView.ViewHolder(binding.root) {

        val cardView = binding.iconPickerRecyclerViewItemCardView
        val imageView = binding.iconPickerItemImageView

    }
}