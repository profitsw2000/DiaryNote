package diarynote.createnotescreen.presentation.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import diarynote.createnotescreen.databinding.HorizontalCategoryListItemRecyclerViewBinding
import diarynote.data.model.CategoryModel

class HorizontalCategoryListAdapter : RecyclerView.Adapter<HorizontalCategoryListAdapter.ViewHolder>() {

    private lateinit var binding: HorizontalCategoryListItemRecyclerViewBinding
    private var data: List<CategoryModel> = arrayListOf()
    private val addElement = CategoryModel(0, 0, "", 333, 0)

    fun setData(data: List<CategoryModel>) {
        this.data = data + addElement
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = HorizontalCategoryListItemRecyclerViewBinding.inflate(LayoutInflater.from(parent.context),
                    parent,
        false)
        return ViewHolder(binding.root)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position], position)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(categoryModel: CategoryModel, position: Int) = with(binding) {
            categoryImageView.setImageResource(getImageFromResources(categoryModel.categoryImage))
            categoryNameTextView.text = categoryModel.categoryName
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