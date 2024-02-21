package diarynote.categoriesfragment.presentation.view.adapter

import android.content.res.Resources
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.request.ImageRequest
import diarynote.categoriesfragment.databinding.CategoriesListItemBinding
import diarynote.data.model.CategoryModel
import diarynote.template.utils.OnCategoryItemClickListener

class CategoriesListAdapter(
    private val onCategoryItemClickListener: OnCategoryItemClickListener,
    private val imageLoader: ImageLoader
) : RecyclerView.Adapter<CategoriesListAdapter.ViewHolder>() {

    private var data: List<CategoryModel> = arrayListOf()

    fun setData(data: List<CategoryModel>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CategoriesListItemBinding.inflate(LayoutInflater.from(parent.context),
                                                    parent,
                                                    false)

        val categoryViewHolder = ViewHolder(binding)

        binding.root.setOnClickListener {
            onCategoryItemClickListener.onItemClick(data[categoryViewHolder.adapterPosition])
        }

        binding.root.setOnLongClickListener {
            onCategoryItemClickListener.onItemClick(data[categoryViewHolder.adapterPosition])
            return@setOnLongClickListener true
        }

        return categoryViewHolder
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val categoryModel = data[position]

        with(holder){
            cardView.setCardBackgroundColor(categoryModel.color)
            cardView.layoutParams = getViewParams(position)
            setCategoryImage(imageView, data[position])
            title.text = categoryModel.categoryName
        }
    }

    inner class ViewHolder(binding: CategoriesListItemBinding) : RecyclerView.ViewHolder(binding.root) {

        val cardView = binding.categoriesListCardView
        val imageView = binding.categoryIconImageView
        val title = binding.categoryTitleTextView
    }

    private fun getViewParams(position: Int) : ConstraintLayout.LayoutParams {
        val params = ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        if (position % 2 == 0) {
            params.setMargins(0,0,dipToPx(16),dipToPx(24))
        } else {
            params.setMargins(dipToPx(16),0,0,dipToPx(24))
        }
        return params
    }

    private fun dipToPx(dip: Int) : Int {
        val r: Resources = Resources.getSystem()
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dip.toFloat(),
            r.displayMetrics
        ).toInt()
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
            else -> diarynote.core.R.drawable.bottom_nav_categories_icon
        }
    }
}