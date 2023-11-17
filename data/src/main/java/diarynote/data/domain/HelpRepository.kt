package diarynote.data.domain

import diarynote.data.model.HelpItemModel

interface HelpRepository {

    fun getHelpItemsList(): List<HelpItemModel>

    fun getHelpItemsTitleList(): List<String>

    fun getHelpItemById(id: Int): HelpItemModel

}