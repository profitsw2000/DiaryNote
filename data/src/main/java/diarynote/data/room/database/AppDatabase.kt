package diarynote.data.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
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
import java.lang.IllegalStateException


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
                SQLCipherUtils.decryptTo(context, context.getDatabasePath(DB_NAME), temp, passphraseGenerator.getPassphrase())
            }

            //write DB to file
            temp.inputStream().copyTo(stream)
            temp.delete()
            stream.close()
        }

        fun copyTo(context: Context, stream: OutputStream, backupPassword: String, passphraseGenerator: PassphraseGenerator) {

            val tempEnc = File(context.cacheDir, "exportenc.db")
            val tempDec = File(context.cacheDir, "exportdec.db")
            tempEnc.delete()
            tempDec.delete()

            //Decrypt DB if it is to temp decrypted file
            if(SQLCipherUtils.getDatabaseState(context, DB_NAME) == SQLCipherUtils.State.ENCRYPTED) {
                SQLCipherUtils.decryptTo(context, context.getDatabasePath(DB_NAME), tempDec, passphraseGenerator.getPassphrase())
            }

            //Encrypt temp decrypted file to temp encrypted with user password
            if(SQLCipherUtils.getDatabaseState(tempDec) == SQLCipherUtils.State.UNENCRYPTED) {
                SQLCipherUtils.encryptTo(context, tempDec, tempEnc, backupPassword.toByteArray())
            }

            //write DB to file
            tempEnc.inputStream().copyTo(stream)
            tempEnc.delete()
            tempDec.delete()
            stream.close()
        }

        fun copyFromDecrypted(context: Context, stream: InputStream, passphraseGenerator: PassphraseGenerator) {

            //create temp file and copy stream to it
            val temp = File(context.cacheDir, "temp.db")
            temp.delete()
            stream.copyTo(temp.outputStream())
            stream.close()

            //encrypt temp file with app passphrase
            try {
                if (SQLCipherUtils.getDatabaseState(temp) == SQLCipherUtils.State.UNENCRYPTED) {
                    SQLCipherUtils.encrypt(context, temp, passphraseGenerator.getPassphrase())
                } else {
                    temp.delete()
                    throw IllegalStateException("Unencrypted database expected, but appears to be encrypted or doesn't exist!")
                }
            } catch (e: Exception) {
                temp.delete()
            }

            //rename temp db file to original app db file, use backup file to prevent data lost
            if (SQLCipherUtils.getDatabaseState(temp) == SQLCipherUtils.State.ENCRYPTED) {
                val dbFile = context.getDatabasePath(DB_NAME)
                val dbBackUp = context.getDatabasePath("backup.db")

                if (dbFile.renameTo(dbBackUp)) {
                    if (temp.renameTo(dbFile)) {
                        dbBackUp.delete()
                    } else {
                        dbBackUp.renameTo(dbFile)
                        throw IOException("Could not rename $temp to $dbFile")
                    }
                } else {
                    temp.delete()
                    throw IOException("Could not rename $dbFile to $dbBackUp")
                }
            }
        }

        fun copyFromEncrypted(context: Context, stream: InputStream, passphraseGenerator: PassphraseGenerator, backupPassword: String) {

            //create encrypted temp file and copy encrypted backup file to it
            val tempEnc = File(context.cacheDir, "tempenc.db")
            val temp = File(context.cacheDir, "tempdec.db")
            tempEnc.delete()
            temp.delete()
            stream.copyTo(tempEnc.outputStream())
            stream.close()

            //decrypt encrypted temp file via user password to newly created temp decrypted file
            try {
                if (SQLCipherUtils.getDatabaseState(tempEnc) == SQLCipherUtils.State.ENCRYPTED) {
                    SQLCipherUtils.decryptTo(context, tempEnc, temp, backupPassword.toByteArray())
                } else throw IllegalStateException("Encrypted database expected, but appears to be unencrypted or doesn't exist!!")
            } finally {
                tempEnc.delete()
            }

            //encrypt temp file with app passphrase
            try {
                if (SQLCipherUtils.getDatabaseState(temp) == SQLCipherUtils.State.UNENCRYPTED) {
                    SQLCipherUtils.encrypt(context, temp, passphraseGenerator.getPassphrase())
                } else {
                    temp.delete()
                    throw IllegalStateException("Unencrypted database expected, but appears to be encrypted or doesn't exist!")
                }
            } catch (e: Exception) {
                temp.delete()
            }

            //rename temp db file to original app db file, use backup file to prevent data lost
            if (SQLCipherUtils.getDatabaseState(temp) == SQLCipherUtils.State.ENCRYPTED) {
                val dbFile = context.getDatabasePath(DB_NAME)
                val dbBackUp = context.getDatabasePath("backup.db")

                if (dbFile.renameTo(dbBackUp)) {
                    if (temp.renameTo(dbFile)) {
                        dbBackUp.delete()
                    } else {
                        dbBackUp.renameTo(dbFile)
                        throw IOException("Could not rename $temp to $dbFile")
                    }
                } else {
                    temp.delete()
                    throw IOException("Could not rename $dbFile to $dbBackUp")
                }
            }
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