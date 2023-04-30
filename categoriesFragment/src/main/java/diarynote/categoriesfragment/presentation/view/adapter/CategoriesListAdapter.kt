package diarynote.categoriesfragment.presentation.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.toDrawable
import androidx.recyclerview.widget.RecyclerView
import diarynote.categoriesfragment.databinding.CategoriesListItemBinding
import diarynote.data.model.CategoryModel

class CategoriesListAdapter : RecyclerView.Adapter<CategoriesListAdapter.ViewHolder>() {

    private var data: List<CategoryModel> = arrayListOf()
    private lateinit var binding: CategoriesListItemBinding

    fun setData(data: List<CategoryModel>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = CategoriesListItemBinding.inflate(LayoutInflater.from(parent.context),
                                                    parent,
                                                    false)
        return ViewHolder(binding.root)
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
        return position
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(categoryModel: CategoryModel) {
            with(binding) {
                categoriesListItemRoot.background = categoryModel.color.toDrawable()
                categoryIconImageView.setImageResource(categoryModel.categoryImage)
                categoryTitleTextView.text = categoryModel.categoryName
            }
        }
    }

}