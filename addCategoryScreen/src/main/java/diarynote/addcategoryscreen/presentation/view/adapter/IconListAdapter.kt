package diarynote.addcategoryscreen.presentation.view.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.load
import coil.request.ImageRequest
import coil.size.ViewSizeResolver
import coil.util.CoilUtils
import diarynote.addcategoryscreen.databinding.CategoryIconPickerRecyclerviewItemBinding
import diarynote.core.utils.listener.OnItemClickListener
import java.io.File

class IconListAdapter (
    private val onItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<IconListAdapter.ViewHolder>() {

    private var data: List<Int> = arrayListOf()
    //private lateinit var context: Context
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CategoryIconPickerRecyclerviewItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false)
        //context = parent.context
        val viewHolder = ViewHolder(binding)

        binding.root.setOnClickListener {
            val position = viewHolder.adapterPosition
            val oldPosition = clickedPosition
            if (position != clickedPosition && position != (data.size - 1)) {
                clickedPosition = position
                notifyItemChanged(position)
                notifyItemChanged(oldPosition)
            }
            onItemClickListener.onItemClick(position)
        }
        return viewHolder
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

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

    override fun onViewRecycled(holder: ViewHolder) {
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

    private fun setLastItemIcon(holder: ViewHolder, position: Int) {
        if (pickedIconPath == "") holder.imageView.setImageResource(getImageFromResources(data[position]))
        else {
            val imageLoader = ImageLoader.Builder(holder.imageView.context)
                .components {
                    add(SvgDecoder.Factory())
                }
                .build()
            val request = ImageRequest.Builder(holder.imageView.context)
                .data(pickedIconPath)
                .target(holder.imageView)
                .listener(
                    onError = { request, throwable ->
                        Log.d("VVV", "setLastItemIcon: Request: ${request.data} | throwable: ${throwable.throwable.message}")
                    },
                    onSuccess = { request, metadata ->
                        // can't access bitmap here as far as I see
                        // because I can only access ImageResult::Metadata and not the ImageResult itself...
                        Log.d("VVV", "Request: ${request.data} | metadata: ${metadata.dataSource} | key: ${metadata.memoryCacheKey}")
                    }
                )
                .error(diarynote.core.R.drawable.bottom_nav_categories_icon)
                .build()
            imageLoader.enqueue(request)

/*            holder.imageView.setImageDrawable(null)
            holder.imageView.load(File(pickedIconPath)) {
                decoderFactory{ result, options, _ -> SvgDecoder( result.source, options)}
            }*/
        }
    }

    inner class ViewHolder(val binding: CategoryIconPickerRecyclerviewItemBinding) : RecyclerView.ViewHolder(binding.root) {

        val cardView = binding.iconPickerRecyclerViewItemCardView
        val imageView = binding.iconPickerItemImageView
    }
}