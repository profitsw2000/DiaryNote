package diarynote.data.domain

import android.content.Context
import diarynote.data.model.HelpItemModel

interface HelpRepository {

    fun getHelpItemsList(context: Context): List<HelpItemModel>

    fun getHelpItemsTitleList(context: Context): List<String>

    fun getHelpItemById(context: Context, id: Int): HelpItemModel

}