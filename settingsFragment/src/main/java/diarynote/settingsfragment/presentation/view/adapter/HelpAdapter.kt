package diarynote.settingsfragment.presentation.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import diarynote.settingsfragment.databinding.HelpRvItemViewBinding
import diarynote.template.utils.OnSettingsMenuItemClickListener

class HelpAdapter (
    private val onSettingsMenuItemClickListener: OnSettingsMenuItemClickListener
) : RecyclerView.Adapter<HelpAdapter.ViewHolder>() {

    private var data: List<String> = arrayListOf()

    fun setData(data: List<String>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = HelpRvItemViewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        val helpItemViewHolder = ViewHolder(binding)

        binding.helpItemRootLayout.setOnClickListener {
            onSettingsMenuItemClickListener.onItemClick(helpItemViewHolder.adapterPosition)
        }

        return helpItemViewHolder
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: HelpAdapter.ViewHolder, position: Int) {
        val helpItem = data[position]

        with(holder){
            itemName.text = helpItem
        }
    }

    inner class ViewHolder(binding: HelpRvItemViewBinding) : RecyclerView.ViewHolder(binding.root){
        val itemName = binding.helpItemNameTextView
    }
}