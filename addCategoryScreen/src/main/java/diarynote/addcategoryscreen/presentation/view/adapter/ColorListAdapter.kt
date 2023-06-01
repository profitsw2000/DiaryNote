package diarynote.addcategoryscreen.presentation.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import diarynote.addcategoryscreen.databinding.CategoryColorPickerRecyclerviewItemBinding
import diarynote.addcategoryscreen.model.ColorModel
import diarynote.core.utils.listener.OnItemClickListener

class ColorListAdapter (
    private val onItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<ColorListAdapter.ViewHolder>() {

    private lateinit var binding: CategoryColorPickerRecyclerviewItemBinding
    private var data: List<ColorModel> = arrayListOf()
    private var lastClickedPosition = 0
    private val addElement = ColorModel(0, false)

    fun setData(data: List<ColorModel>) {
        this.data = data + addElement
        notifyDataSetChanged()
    }

    fun setData(data: List<ColorModel>, selectedItemPosition: Int) {
        lastClickedPosition = selectedItemPosition
        this.data = data + addElement
        notifyDataSetChanged()
    }

    fun setData(data: List<ColorModel>, selectedItemPosition: Int, lastSelectedItemPosition: Int) {
        this.data = data + addElement
        notifyItemChanged(selectedItemPosition)
        notifyItemChanged(lastSelectedItemPosition)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = CategoryColorPickerRecyclerviewItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false)
        return ViewHolder(binding.root)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(colorModel: ColorModel) = with(binding) {
            colorPickerRecyclerViewItemCardView.setCardBackgroundColor(colorModel.color)
            if (colorModel.isSelected) colorPickerRecyclerViewItemCardView.strokeWidth = 2

            colorPickerRecyclerViewItemCardView.setOnClickListener {
                onItemClickListener.onItemClick(layoutPosition)
            }
        }
    }
}