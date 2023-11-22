package diarynote.data.data.local

import android.content.Context
import diarynote.core.R
import diarynote.data.appsettings.createHelpItemsList
import diarynote.data.domain.local.HelpRepositoryLocal
import diarynote.data.model.HelpItemModel
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import java.lang.Exception

class HelpRepositoryLocalImpl(): HelpRepositoryLocal {

    override fun getHelpItemsList(context: Context): Single<List<HelpItemModel>> {

        Single.create<List<HelpItemModel>> { emitter ->
            try {
                emitter.onSuccess(createHelpItemsList(context.resources.getStringArray(R.array.help_menu_strings),
                    context.resources.getStringArray(R.array.help_menu_content_strings))
                )
            } catch (e: Exception) {
                e.printStackTrace()
                emitter.onError(e)
            }
        }
        return Single.create<List<HelpItemModel>> { emitter ->
            try {
                emitter.onSuccess(createHelpItemsList(context.resources.getStringArray(R.array.help_menu_strings),
                    context.resources.getStringArray(R.array.help_menu_content_strings))
                )
            } catch (e: Exception) {
                e.printStackTrace()
                emitter.onError(e)
            }
        }
    }
}