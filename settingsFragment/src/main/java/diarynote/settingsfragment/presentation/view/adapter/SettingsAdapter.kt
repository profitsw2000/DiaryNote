package diarynote.settingsfragment.presentation.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import diarynote.data.model.SettingsMenuItemModel
import diarynote.settingsfragment.databinding.SettingsMenuRvItemViewBinding
import diarynote.template.utils.OnSettingsMenuItemClickListener

class SettingsAdapter(
    private val onSettingsMenuItemClickListener: OnSettingsMenuItemClickListener
) : RecyclerView.Adapter<SettingsAdapter.ViewHolder>() {

    private var data: List<SettingsMenuItemModel> = arrayListOf()

    fun setData(data: List<SettingsMenuItemModel>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = SettingsMenuRvItemViewBinding.inflate(LayoutInflater.from(parent.context),
            parent,
            false)
        val settingsMenuItemViewHolder = ViewHolder(binding)

        binding.menuItemTitleTextView.setOnClickListener {
            onSettingsMenuItemClickListener.onItemClick(
                data[settingsMenuItemViewHolder.adapterPosition].itemId
            )
        }

        return settingsMenuItemViewHolder
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val settingsMenuItem = data[position]

        with(holder) {
            icon.setImageResource(setItemIcon(settingsMenuItem.itemId))
            itemName.text = settingsMenuItem.itemName
        }
    }

    inner class ViewHolder(binding: SettingsMenuRvItemViewBinding) : RecyclerView.ViewHolder(binding.root){
        val icon = binding.menuIconImageView
        val itemName = binding.menuItemTitleTextView
    }

    private fun setItemIcon(itemId: Int) : Int {
        return when(itemId) {
            1 -> diarynote.core.R.drawable.person_icon_32
            2 -> diarynote.core.R.drawable.theme_light_dark_icon_32
            3 -> diarynote.core.R.drawable.lang_icon_32
            4 -> diarynote.core.R.drawable.instrum_icon_32
            5 -> diarynote.core.R.drawable.help_icon_32
            6 -> diarynote.core.R.drawable.about_icon_32
            else -> diarynote.core.R.drawable.person_icon_32
        }
    }
}