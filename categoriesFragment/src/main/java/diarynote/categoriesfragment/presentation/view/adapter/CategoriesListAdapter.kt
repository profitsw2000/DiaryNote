package diarynote.categoriesfragment.presentation.view.adapter

import android.content.res.Resources
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.marginLeft
import androidx.recyclerview.widget.RecyclerView
import diarynote.categoriesfragment.R
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
        holder.bind(data[position], position)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(categoryModel: CategoryModel, position: Int) {
            with(binding) {
                categoriesListItemRoot.setBackgroundResource(categoryModel.color)
                categoriesListItemRoot.layoutParams = getViewParams(position)
                categoryIconImageView.setImageResource(categoryModel.categoryImage)
                categoryTitleTextView.text = categoryModel.categoryName
            }
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
    }
}