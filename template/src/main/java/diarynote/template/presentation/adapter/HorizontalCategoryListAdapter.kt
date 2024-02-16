package diarynote.template.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.request.ImageRequest
import coil.util.CoilUtils
import diarynote.data.model.CategoryModel
import diarynote.template.databinding.HorizontalCategoryListItemRecyclerViewBinding
import diarynote.template.utils.OnHorizontalCategoryListItemClickListener

class HorizontalCategoryListAdapter(
    private val onHorizontalCategoryListItemClickListener: OnHorizontalCategoryListItemClickListener,
    private val imageLoader: ImageLoader
) : RecyclerView.Adapter<HorizontalCategoryListAdapter.ViewHolder>() {

    private var clickedPositionNumber = 0
    private var data: List<CategoryModel> = arrayListOf()
    private val addElement = CategoryModel(0, 0, "", 333, "",0)

    fun setData(data: List<CategoryModel>, clickedPositionNumber: Int) {
        this.data = data + addElement
        this.clickedPositionNumber = clickedPositionNumber
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = HorizontalCategoryListItemRecyclerViewBinding.inflate(LayoutInflater.from(parent.context),
                    parent,
        false)
        val viewHolder = ViewHolder(binding)

        binding.root.setOnClickListener {
            val position = viewHolder.adapterPosition
            if (position != clickedPositionNumber) {
                notifyItemChanged(position)
                notifyItemChanged(clickedPositionNumber)
                clickedPositionNumber = position
                onHorizontalCategoryListItemClickListener.onItemClick(viewHolder.adapterPosition)
            }
        }
        return viewHolder
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val categoryModel = data[position]

        with(holder) {
            if (position == clickedPositionNumber) selectedItemView(this)
            else nonSelectedItemView(this)
            setCategoryImage(this.imageView, categoryModel)
            categoryName.text = categoryModel.categoryName
        }
    }

    override fun onViewRecycled(holder: ViewHolder) {
        CoilUtils.dispose(holder.imageView)
    }

    inner class ViewHolder(binding: HorizontalCategoryListItemRecyclerViewBinding) : RecyclerView.ViewHolder(binding.root) {
        val imageView = binding.categoryImageView
        val categoryName = binding.categoryNameTextView
        val imageLayout = binding.imageConstraintLayout
    }

    private fun selectedItemView(holder: ViewHolder) {
        with(holder) {
            imageLayout.background = ContextCompat.getDrawable(holder.imageView.context, diarynote.core.R.drawable.horizontal_category_list_selected_item_background)
            imageView.setColorFilter(ContextCompat.getColor(holder.imageView.context, diarynote.core.R.color.black))
            categoryName.setTextAppearance(diarynote.core.R.style.horizontal_category_list_selected_item_name_text_style)
        }
    }

    private fun nonSelectedItemView(holder: ViewHolder) {
        with(holder) {
            imageLayout.background = ContextCompat.getDrawable(holder.imageView.context, diarynote.core.R.drawable.horizontal_category_list_item_background)
            imageView.setColorFilter(ContextCompat.getColor(holder.imageView.context, diarynote.core.R.color.grey_600))
            categoryName.setTextAppearance(diarynote.core.R.style.horizontal_category_list_item_name_text_style)
        }
    }

    private fun setCategoryImage(imageView: ImageView, categoryModel: CategoryModel) {
        if (categoryModel.imagePath.isNullOrEmpty()) {
            imageView.setImageResource(getImageFromResources(categoryModel.categoryImage))
        } else {
            //set image by Coil
            val request = ImageRequest.Builder(imageView.context)
                .data(categoryModel.imagePath)
                .target(imageView)
                .error(diarynote.core.R.drawable.bottom_nav_categories_icon)
                .build()
            imageLoader.enqueue(request)
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