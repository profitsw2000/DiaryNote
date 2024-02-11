package diarynote.createnotescreen.presentation.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.request.ImageRequest
import diarynote.createnotescreen.databinding.HorizontalCategoryListItemRecyclerViewBinding
import diarynote.createnotescreen.presentation.view.utils.OnItemClickListener
import diarynote.data.model.CategoryModel

class HorizontalCategoryListAdapter(
    private val onItemClickListener: OnItemClickListener,
    private val imageLoader: ImageLoader
) : RecyclerView.Adapter<HorizontalCategoryListAdapter.ViewHolder>() {

    private var lastClickedPosition = 0
    private var data: List<CategoryModel> = arrayListOf()
    private val addElement = CategoryModel(0, 0, "", 333, "",0)

    fun setData(data: List<CategoryModel>) {
        this.data = data + addElement
        notifyDataSetChanged()
    }

    fun updateData(data: List<CategoryModel>, selectedItemPosition: Int) {
        lastClickedPosition = selectedItemPosition
        this.data = data + addElement
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = HorizontalCategoryListItemRecyclerViewBinding.inflate(LayoutInflater.from(parent.context),
                    parent,
        false)
        val viewHolder = ViewHolder(binding)

        binding.root.setOnClickListener {
            onItemClickListener.onItemClick(viewHolder.adapterPosition)
        }
        return viewHolder
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val categoryModel = data[position]

        with(holder) {

        }
    }

    inner class ViewHolder(binding: HorizontalCategoryListItemRecyclerViewBinding) : RecyclerView.ViewHolder(binding.root) {

        val imageView = binding.categoryImageView
        val categoryName = binding.categoryNameTextView

        fun bind(categoryModel: CategoryModel) = with(binding) {
            categoryImageView.setImageResource(getImageFromResources(categoryModel.categoryImage))
            categoryNameTextView.text = categoryModel.categoryName

            if (layoutPosition == lastClickedPosition) {
                selectedItemView()
            }

            horizontalCategoryListItemRootLayout.setOnClickListener {
                onItemClickListener.onItemClick(layoutPosition)
            }

        }

        private fun selectedItemView() = with(binding) {
            imageConstraintLayout.background = ContextCompat.getDrawable(context, diarynote.core.R.drawable.horizontal_category_list_selected_item_background)
            categoryImageView.setColorFilter(ContextCompat.getColor(context, diarynote.core.R.color.black))
            categoryNameTextView.setTextAppearance(diarynote.core.R.style.horizontal_category_list_selected_item_name_text_style)
        }

        private fun setCategoryImage(imageView: ImageView, categoryModel: CategoryModel) {
            if (categoryModel.imagePath == "") {
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

    private fun selectedItemView() {
        imageConstraintLayout.background = ContextCompat.getDrawable(context, diarynote.core.R.drawable.horizontal_category_list_selected_item_background)
        categoryImageView.setColorFilter(ContextCompat.getColor(context, diarynote.core.R.color.black))
        categoryNameTextView.setTextAppearance(diarynote.core.R.style.horizontal_category_list_selected_item_name_text_style)
    }

    private fun setCategoryImage(imageView: ImageView, categoryModel: CategoryModel) {
        if (categoryModel.imagePath == "") {
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