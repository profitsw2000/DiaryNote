package diarynote.data.room.database

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.core.net.toFile
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import diarynote.core.utils.FileHelper
import diarynote.data.room.dao.CategoryDao
import diarynote.data.room.dao.NoteDao
import diarynote.data.room.dao.UserDao
import diarynote.data.room.entity.CategoryEntity
import diarynote.data.room.entity.NoteEntity
import diarynote.data.room.entity.UserEntity
import diarynote.data.room.mappers.Converter
import diarynote.data.room.utils.PassphraseGenerator
import diarynote.data.room.utils.SQLCipherUtils
import net.sqlcipher.database.SupportFactory
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream


@Database(
    entities = [CategoryEntity::class, NoteEntity::class, UserEntity::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract val categoryDao: CategoryDao
    abstract val noteDao: NoteDao
    abstract val userDao: UserDao

    companion object {
        private const val DB_NAME = "database.db"
        private var instance: AppDatabase? = null
        private val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE CategoryEntity ADD COLUMN imagePath TEXT NOT NULL DEFAULT ''")
            }
        }

        fun getInstance() = instance
            ?: throw java.lang.RuntimeException("Database has not been created. Please call create(context)")

        fun create(context: Context, passphraseGenerator: PassphraseGenerator) {
            val factory = SupportFactory(passphraseGenerator.getPassphrase())
            Log.d("VVV", "passphrase: ${passphraseGenerator.getPassphrase()}")
            if (instance == null) {
                addMigrationAndEncrypt(context, passphraseGenerator.getPassphrase(), AppDatabase::class.java, DB_NAME)
                instance = Room.databaseBuilder(context, AppDatabase::class.java, DB_NAME)
                    .openHelperFactory(factory)
                    .addMigrations(MIGRATION_1_2)
                    .build()
            }
        }

        fun exists(context: Context) = context.getDatabasePath(DB_NAME).exists()

        fun copyTo(context: Context, uri: Uri, stream: OutputStream, passphraseGenerator: PassphraseGenerator) {

            //val fileHelper = FileHelper()
            val temp = File(context.cacheDir, "export.db")
            temp.delete()

            //Decrypt DB if it is
            if(SQLCipherUtils.getDatabaseState(context, DB_NAME) == SQLCipherUtils.State.ENCRYPTED) {
                Log.d("VVV", "copyTo: ${passphraseGenerator.getPassphrase()}")
                SQLCipherUtils.decryptTo(context, context.getDatabasePath(DB_NAME), temp, passphraseGenerator.getPassphrase())
            }

            //write DB to file
            temp.inputStream().copyTo(stream)
            temp.delete()
            //context.getDatabasePath(DB_NAME).inputStream().copyTo(stream)
            stream.close()

/*            if (SQLCipherUtils.getDatabaseState(context, DB_NAME) == SQLCipherUtils.State.UNENCRYPTED) {
                Log.d("VVV", "copyTo: ${passphraseGenerator.getPassphrase()}")
                SQLCipherUtils.encrypt(context, context.getDatabasePath(DB_NAME), passphraseGenerator.getPassphrase())
            }
            //instance = null
            create(context, passphraseGenerator)*/
            //check, if DB is encrypted and decrypt it
/*            if(SQLCipherUtils.getDatabaseState(context, fileHelper.getRealPathFromURI(context, uri)) == SQLCipherUtils.State.ENCRYPTED) {
                //throw IOException()
                SQLCipherUtils.decrypt(context, context.getDatabasePath(fileHelper.getRealPathFromURI(context, uri)), defaultPassword)
            }*/
        }

        fun copyTo(context: Context, uri: Uri, stream: OutputStream, backupPassword: String, defaultPassword: ByteArray) {

            //write DB to file
            context.getDatabasePath(DB_NAME).inputStream().copyTo(stream)
            stream.close()

            //check, if DB is encrypted and decrypt it
            if(SQLCipherUtils.getDatabaseState(context, File(uri.path).name) == SQLCipherUtils.State.ENCRYPTED) {
                //throw IOException()
                SQLCipherUtils.decrypt(context, context.getDatabasePath(File(uri.path).name), defaultPassword)
            }
            //encrypt DB with user password
            if (SQLCipherUtils.getDatabaseState(context, File(uri.path).name) == SQLCipherUtils.State.UNENCRYPTED) {
                SQLCipherUtils.encrypt(context, context.getDatabasePath(File(uri.path).name), backupPassword.toByteArray())
            }

        }

        fun copyFrom(context: Context, stream: InputStream) {
            val dbFile = context.getDatabasePath(DB_NAME)

            dbFile.delete()
            stream.copyTo(dbFile.outputStream())
        }

        fun copyFrom(context: Context, stream: InputStream, userPassword: String) {
            val dbFile = context.getDatabasePath(DB_NAME)

            dbFile.delete()
            stream.copyTo(dbFile.outputStream())
        }

        private fun addMigrationAndEncrypt(
            context: Context,
            passphrase: ByteArray,
            klass: Class<out RoomDatabase>,
            dbName: String
        ) {
            //Decrypt DB if it is
            if(SQLCipherUtils.getDatabaseState(context, dbName) == SQLCipherUtils.State.ENCRYPTED) {
                SQLCipherUtils.decrypt(context, context.getDatabasePath(dbName), passphrase)
            }

            //Perform the migration
            var db = Room.databaseBuilder(context, klass, dbName)
                .addMigrations(MIGRATION_1_2)
                .build()
            //Encrypt it again
            if (db.isOpen) db.close()
            if (SQLCipherUtils.getDatabaseState(context, dbName) == SQLCipherUtils.State.UNENCRYPTED) {
                SQLCipherUtils.encrypt(context, context.getDatabasePath(dbName), passphrase)
            }
        }
    }
}