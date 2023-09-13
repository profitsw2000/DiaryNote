package diarynote.settingsfragment.presentation.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import diarynote.data.appsettings.SETTINGS_ABOUT_ID
import diarynote.data.appsettings.SETTINGS_ACCOUNT_ID
import diarynote.data.appsettings.SETTINGS_GENERAL_ID
import diarynote.data.appsettings.SETTINGS_HELP_ID
import diarynote.data.appsettings.SETTINGS_LANGUAGE_ID
import diarynote.data.appsettings.SETTINGS_THEME_ID
import diarynote.data.model.SettingsMenuItemModel
import diarynote.settingsfragment.databinding.SettingsMenuRvItemViewBinding
import diarynote.settingsfragment.databinding.SubSettingsRvItemViewBinding
import diarynote.template.utils.OnSettingsMenuItemClickListener

class SubSettingsAdapter(
    private val onSettingsMenuItemClickListener: OnSettingsMenuItemClickListener
) : RecyclerView.Adapter<SubSettingsAdapter.ViewHolder>() {

    private var data: List<SettingsMenuItemModel> = arrayListOf()
    private var selectedLanguageId: Int = 0

    fun setData(data: List<SettingsMenuItemModel>) {
        this.data = data
        notifyDataSetChanged()
    }

    fun setData(data: List<SettingsMenuItemModel>, selectedLanguageId: Int) {
        this.data = data
        this.selectedLanguageId = selectedLanguageId
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = SubSettingsRvItemViewBinding.inflate(LayoutInflater.from(parent.context),
            parent,
            false)
        val subSettingsMenuItemViewHolder = ViewHolder(binding)

        binding.accountSettingsRvItemViewRootLayout.setOnClickListener {
            onSettingsMenuItemClickListener.onItemClick(
                data[subSettingsMenuItemViewHolder.adapterPosition].itemId
            )
        }

        return subSettingsMenuItemViewHolder
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val settingsMenuItem = data[position]

        with(holder) {
            itemName.text = settingsMenuItem.itemName
            if (settingsMenuItem.itemId == selectedLanguageId) {
                itemImage.visibility = View.VISIBLE
            }
        }
    }

    inner class ViewHolder(binding: SubSettingsRvItemViewBinding) : RecyclerView.ViewHolder(binding.root){
        val itemName = binding.accountMenuItemNameTextView
        val itemImage = binding.subSettingsItemImageView
    }
}