package diarynote.data.data.web

import android.content.Context
import diarynote.data.domain.web.HelpRepositoryRemote
import diarynote.data.model.HelpItemModel

class HelpRepositoryRemoteImpl: HelpRepositoryRemote {
    override fun getHelpItemsList(context: Context): List<HelpItemModel> {
        TODO("Not yet implemented")
    }

    override fun getHelpItemsTitleList(context: Context): List<String> {
        TODO("Not yet implemented")
    }

    override fun getHelpItemById(context: Context, id: Int): HelpItemModel {
        TODO("Not yet implemented")
    }
}