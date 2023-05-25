package diarynote.editnotescreen.presentation.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import diarynote.data.model.CategoryModel
import diarynote.editnotescreen.databinding.HorizontalCategoryListItemRecyclerViewBinding
import diarynote.editnotescreen.presentation.view.utils.OnItemClickListener

class HorizontalCategoryListAdapter(
    private val onItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<HorizontalCategoryListAdapter.ViewHolder>() {

    private lateinit var binding: HorizontalCategoryListItemRecyclerViewBinding
    private var lastClickedPosition = 0
    private var data: List<CategoryModel> = arrayListOf()
    private val addElement = CategoryModel(0, 0, "", 333, 0)
    private lateinit var context: Context

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
        binding = HorizontalCategoryListItemRecyclerViewBinding.inflate(LayoutInflater.from(parent.context),
                    parent,
        false)
        context = parent.context
        return ViewHolder(binding.root)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

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
            categoryNameTextView.setTextColor(ContextCompat.getColor(context, diarynote.core.R.color.black))
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