package diarynote.template.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.request.ImageRequest
import coil.util.CoilUtils
import diarynote.core.utils.listener.OnItemClickListener
import diarynote.template.databinding.CategoryIconPickerRecyclerviewItemBinding

class IconListAdapter (
    private val onItemClickListener: OnItemClickListener,
    private val imageLoader: ImageLoader
) : RecyclerView.Adapter<IconListAdapter.IconViewHolder>() {

    private var data: List<Int> = arrayListOf()
    var clickedPosition = 0
    private var pickedIconPath = ""

    fun setData(data: List<Int>, clickedPosition: Int) {
        this.data = data
        this.clickedPosition = clickedPosition
        notifyDataSetChanged()
    }

    fun updateIconImage(iconPath: String) {
        pickedIconPath = iconPath
        clickedPosition = data.size - 1
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IconViewHolder {
        val binding = CategoryIconPickerRecyclerviewItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false)
        val IconViewHolder = IconViewHolder(binding)

        binding.root.setOnClickListener {
            val position = IconViewHolder.adapterPosition
            val oldPosition = clickedPosition
            if (position != clickedPosition && position != (data.size - 1)) {
                clickedPosition = position
                notifyItemChanged(position)
                notifyItemChanged(oldPosition)
            }
            onItemClickListener.onItemClick(position)
        }
        return IconViewHolder
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: IconViewHolder, position: Int) {

        if (position != (data.size - 1)) {
            holder.imageView.setImageResource(getImageFromResources(data[position]))
        }
        else {
            setLastItemIcon(holder, position)
        }

        if (clickedPosition == position) {
            holder.cardView.setCardBackgroundColor(ContextCompat.getColor(holder.imageView.context, diarynote.core.R.color.purple_200))
        } else {
            holder.cardView.setCardBackgroundColor(ContextCompat.getColor(holder.imageView.context, diarynote.core.R.color.white))
        }
    }

    override fun onViewRecycled(holder: IconViewHolder) {
        CoilUtils.dispose(holder.imageView)
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

    private fun setLastItemIcon(holder: IconViewHolder, position: Int) {
        if (pickedIconPath == "") holder.imageView.setImageResource(getImageFromResources(data[position]))
        else {
            val request = ImageRequest.Builder(holder.imageView.context)
                .data(pickedIconPath)
                .target(holder.imageView)
                .error(diarynote.core.R.drawable.error_icon_24)
                .build()
            imageLoader.enqueue(request)
        }
    }

    inner class IconViewHolder(val binding: CategoryIconPickerRecyclerviewItemBinding) : RecyclerView.ViewHolder(binding.root) {

        val cardView = binding.iconPickerRecyclerViewItemCardView
        val imageView = binding.iconPickerItemImageView
    }
}