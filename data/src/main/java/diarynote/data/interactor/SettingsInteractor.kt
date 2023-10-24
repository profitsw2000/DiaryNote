package diarynote.data.interactor

import android.content.Context
import android.net.Uri
import diarynote.core.R
import diarynote.data.appsettings.accountSettingsIdList
import diarynote.data.appsettings.createSettingsMenuList
import diarynote.data.appsettings.settingsIdList
import diarynote.data.model.SettingsMenuItemModel
import diarynote.data.room.database.AppDatabase
import io.reactivex.rxjava3.core.Completable
import java.lang.Exception

class SettingsInteractor(
    private val context: Context,
    private var database: AppDatabase?
) {

    fun getSettingsMenuItemsList(context: Context, remote: Boolean)  : List<SettingsMenuItemModel>{

        if (remote) return arrayListOf()
        else return createSettingsMenuList(
            settingsIdList,
            context.resources.getStringArray(R.array.settings_strings)
        )
    }

    fun getAccountSettingsMenuItemsList(context: Context, remote: Boolean)  : List<SettingsMenuItemModel>{
        if (remote) return arrayListOf()
        else return  createSettingsMenuList(
            accountSettingsIdList,
            context.resources.getStringArray(R.array.account_settings_strings)
        )
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

    fun exportDB(uri: Uri): Completable {
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