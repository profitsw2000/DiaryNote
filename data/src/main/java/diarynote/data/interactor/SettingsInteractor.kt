package diarynote.data.interactor

import android.content.Context
import android.net.Uri
import diarynote.core.R
import diarynote.data.appsettings.accountSettingsIdList
import diarynote.data.appsettings.createSettingsMenuList
import diarynote.data.appsettings.settingsIdList
import diarynote.data.domain.local.HelpRepositoryLocal
import diarynote.data.domain.web.HelpRepositoryRemote
import diarynote.data.model.HelpItemModel
import diarynote.data.model.SettingsMenuItemModel
import diarynote.data.room.database.AppDatabase
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import java.lang.Exception

class SettingsInteractor(
    private val context: Context,
    private var database: AppDatabase?,
    private val helpRepositoryLocal: HelpRepositoryLocal,
    private val helpRepositoryRemote: HelpRepositoryRemote
) {

    fun getSettingsMenuItemsList(context: Context, remote: Boolean)  : List<SettingsMenuItemModel>{

        if (remote) return arrayListOf()
        else return createSettingsMenuList(
            settingsIdList,
            context.resources.getStringArray(R.array.settings_strings)
        )
    }

    fun getAccountSettingsMenuItemsList(context: Context, remote: Boolean)  : List<SettingsMenuItemModel>{
        return if (remote) arrayListOf()
        else createSettingsMenuList(
            accountSettingsIdList,
            context.resources.getStringArray(R.array.account_settings_strings)
        )
    }

    fun getHelpItemsList(context: Context, remote: Boolean): Single<List<HelpItemModel>> {
        return if (remote) {
            helpRepositoryRemote.getHelpItemsList(context)
        } else {
            helpRepositoryLocal.getHelpItemsList(context)
        }
    }

    fun importDB(uri: Uri): Completable {
        database?.close()
        database = null

        return Completable.create { emitter ->
            try {
                context.contentResolver.openInputStream(uri)?.use {
                    AppDatabase.copyFrom(context, it)
                }
                emitter.onComplete()
            } catch (e: Exception) {
                e.printStackTrace()
                emitter.onError(e)
            }
        }
    }

    fun exportDB(uri: Uri, backupPassword: String): Completable {
        database?.close()
        database = null

        return Completable.create { emitter ->
            try {
                context.contentResolver.openOutputStream(uri)?.use {
                    AppDatabase.copyTo(context, it)
                }
                emitter.onComplete()
            } catch (e: Exception) {
                e.printStackTrace()
                emitter.onError(e)
            }
        }
    }
}