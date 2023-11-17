package diarynote.data.data.local

import android.content.Context
import diarynote.core.R
import diarynote.data.appsettings.createHelpItemsList
import diarynote.data.domain.local.HelpRepositoryLocal
import diarynote.data.model.HelpItemModel

class HelpRepositoryLocalImpl(): HelpRepositoryLocal {

    override fun getHelpItemsList(context: Context): List<HelpItemModel> {
        return createHelpItemsList(context.resources.getStringArray(R.array.help_menu_strings),
            context.resources.getStringArray(R.array.help_menu_content_strings)
        )
    }

    override fun getHelpItemsTitleList(context: Context): List<String> {
        return mutableListOf<String>().apply {
            context.resources.getStringArray(R.array.help_menu_strings).forEach {
                this.add(it)
            }
        }
    }

    override fun getHelpItemById(context: Context, id: Int): HelpItemModel {
        return createHelpItemsList(context.resources.getStringArray(R.array.help_menu_strings),
            context.resources.getStringArray(R.array.help_menu_content_strings)
        )[id]
    }
}