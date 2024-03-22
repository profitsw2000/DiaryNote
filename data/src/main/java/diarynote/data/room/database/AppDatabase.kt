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

        fun copyTo(context: Context, stream: OutputStream, passphraseGenerator: PassphraseGenerator) {

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
        }

        fun copyTo(context: Context, stream: OutputStream, backupPassword: String, passphraseGenerator: PassphraseGenerator) {

            val tempEnc = File(context.cacheDir, "exportenc.db")
            val tempDec = File(context.cacheDir, "exportdec.db")
            tempEnc.delete()
            tempDec.delete()

            //Decrypt DB if it is to temp decrypted file
            if(SQLCipherUtils.getDatabaseState(context, DB_NAME) == SQLCipherUtils.State.ENCRYPTED) {
                Log.d("VVV", "copyTo: ${passphraseGenerator.getPassphrase()}")
                SQLCipherUtils.decryptTo(context, context.getDatabasePath(DB_NAME), tempDec, passphraseGenerator.getPassphrase())
            }

            //Encrypt temp decrypted file to temp encrypted with user password
            if(SQLCipherUtils.getDatabaseState(tempDec) == SQLCipherUtils.State.UNENCRYPTED) {
                Log.d("VVV", "copyTo: $backupPassword")
                SQLCipherUtils.encryptTo(context, tempDec, tempEnc, backupPassword.toByteArray())
            }

            //write DB to file
            tempEnc.inputStream().copyTo(stream)
            tempEnc.delete()
            tempDec.delete()
            stream.close()
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