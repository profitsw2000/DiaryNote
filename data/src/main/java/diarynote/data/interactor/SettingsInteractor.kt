package diarynote.data.interactor

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.webkit.MimeTypeMap
import androidx.activity.result.contract.ActivityResultContracts
import diarynote.core.R
import diarynote.core.utils.FileHelper
import diarynote.data.appsettings.accountSettingsIdList
import diarynote.data.appsettings.createSettingsMenuList
import diarynote.data.appsettings.settingsIdList
import diarynote.data.domain.local.HelpRepositoryLocal
import diarynote.data.domain.web.HelpRepositoryRemote
import diarynote.data.model.HelpItemModel
import diarynote.data.model.SettingsMenuItemModel
import diarynote.data.room.database.AppDatabase
import diarynote.data.room.utils.PassphraseGenerator
import diarynote.data.room.utils.SQLCipherUtils
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import java.io.File
import java.lang.Exception

class SettingsInteractor(
    private val context: Context,
    private var database: AppDatabase?,
    private val helpRepositoryLocal: HelpRepositoryLocal,
    private val helpRepositoryRemote: HelpRepositoryRemote,
    private val passphraseGenerator: PassphraseGenerator
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

    fun importDecryptedDB(uri: Uri): Completable {

        return Completable.create { emitter ->
            try {
                context.contentResolver.openInputStream(uri)?.use {
                    AppDatabase.copyFromDecrypted(context, it, passphraseGenerator)
                }
                emitter.onComplete()
            } catch (e: Exception) {
                e.printStackTrace()
                emitter.onError(e)
            }
        }
    }

    fun importEncryptedDB(uri: Uri, backupPassword: String): Completable {

        return Completable.create { emitter ->
            try {
                context.contentResolver.openInputStream(uri)?.use {
                    AppDatabase.copyFromEncrypted(context, it, passphraseGenerator, backupPassword)
                }
                emitter.onComplete()
            } catch (e: Exception) {
                e.printStackTrace()
                emitter.onError(e)
            }
        }
    }

    //check database encryption
    fun checkPickedFile(uri: Uri): Single<Boolean> {

        return Single.create { emitter ->
            try {
                //create temp file and copy stream to it
                val temp = File(context.cacheDir, "temp.db")
                temp.delete()
                context.contentResolver.openInputStream(uri)?.use {
                    it.copyTo(temp.outputStream())
                }
                if(SQLCipherUtils.getDatabaseState(temp) == SQLCipherUtils.State.ENCRYPTED) {
                    emitter.onSuccess(true)
                } else {
                    emitter.onSuccess(false)
                }
            } catch (e: Exception) {
                emitter.onError(e)
            }
        }
    }

    fun exportDB(uri: Uri): Completable {

        return Completable.create { emitter ->
            try {
                context.contentResolver.openOutputStream(uri)?.use {
                    AppDatabase.copyTo(context, it, passphraseGenerator)
                }
                emitter.onComplete()
            } catch (e: Exception) {
                e.printStackTrace()
                emitter.onError(e)
            }
        }
    }

    fun exportDB(uri: Uri, backupPassword: String): Completable {

        return Completable.create { emitter ->
            try {
                context.contentResolver.openOutputStream(uri)?.use {
                    AppDatabase.copyTo(context, it, backupPassword, passphraseGenerator)
                }
                emitter.onComplete()
            } catch (e: Exception) {
                e.printStackTrace()
                emitter.onError(e)
            }
        }
    }
}