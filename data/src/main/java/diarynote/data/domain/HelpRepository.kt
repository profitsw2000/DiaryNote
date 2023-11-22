package diarynote.data.domain

import android.content.Context
import diarynote.data.model.HelpItemModel
import io.reactivex.rxjava3.core.Single

interface HelpRepository {

    fun getHelpItemsList(context: Context): Single<List<HelpItemModel>>

}