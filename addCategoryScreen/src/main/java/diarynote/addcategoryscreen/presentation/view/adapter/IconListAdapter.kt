package diarynote.addcategoryscreen.presentation.view.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.color.MaterialColors
import diarynote.addcategoryscreen.databinding.CategoryIconPickerRecyclerviewItemBinding
import diarynote.addcategoryscreen.model.IconModel
import diarynote.core.utils.listener.OnItemClickListener

class IconListAdapter (
    private val onItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<IconListAdapter.ViewHolder>() {

    private lateinit var binding: CategoryIconPickerRecyclerviewItemBinding
    private var data: List<IconModel> = arrayListOf()
    private var lastClickedPosition = 0
    private val addElement = IconModel(0, false)

    fun setData(data: List<IconModel>) {
        this.data = data    // + addElement
        notifyDataSetChanged()
    }

    fun setData(data: List<IconModel>, selectedItemPosition: Int) {
        lastClickedPosition = selectedItemPosition
        this.data = data    // + addElement
        notifyDataSetChanged()
    }

    fun setData(data: List<IconModel>, selectedItemPosition: Int, lastSelectedItemPosition: Int) {
        this.data = data    // + addElement
        notifyItemChanged(selectedItemPosition)
        notifyItemChanged(lastSelectedItemPosition)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = CategoryIconPickerRecyclerviewItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false)
        return ViewHolder(binding.root)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d("VVV", "bind, position $position")
        holder.bind(data[position])
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(iconModel: IconModel) = with(binding) {
            iconPickerItemImageView.setImageResource(getImageFromResources(iconModel.icon))
            if (iconModel.isSelected) iconPickerRecyclerViewItemCardView.setCardBackgroundColor(0xFF555555.toInt())//MaterialColors.getColor(itemView, com.google.android.material.R.attr.colorOnSurfaceVariant))

            iconPickerRecyclerViewItemCardView.setOnClickListener {
                onItemClickListener.onItemClick(layoutPosition)
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
    }
}