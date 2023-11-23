package diarynote.data.data.web

import android.content.Context
import diarynote.data.domain.web.HelpRepositoryRemote
import diarynote.data.model.HelpItemModel
import io.reactivex.rxjava3.core.Single

class HelpRepositoryRemoteImpl: HelpRepositoryRemote {
    override fun getHelpItemsList(context: Context): Single<List<HelpItemModel>> {
        TODO("Not yet implemented")
    }
}