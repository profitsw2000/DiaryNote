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
            if (instance == null) {
                addMigrationAndEncrypt(context, passphraseGenerator.getPassphrase(), AppDatabase::class.java, DB_NAME)
                instance = Room.databaseBuilder(context, AppDatabase::class.java, DB_NAME)
                    .openHelperFactory(factory)
                    .build()
            }
        }

        fun exists(context: Context) = context.getDatabasePath(DB_NAME).exists()

        fun copyTo(context: Context, stream: OutputStream) {
            context.getDatabasePath(DB_NAME).inputStream().copyTo(stream)
        }

        fun copyTo(context: Context, stream: OutputStream, backupPassword: String, defaultPassword: ByteArray) {
            //check, if DB is encrypted and decrypt it
            if(SQLCipherUtils.getDatabaseState(context, DB_NAME) == SQLCipherUtils.State.ENCRYPTED) {
                //throw IOException()
                SQLCipherUtils.decrypt(context, context.getDatabasePath(DB_NAME), defaultPassword)
            }
            //encrypt DB with user password
            if (SQLCipherUtils.getDatabaseState(context, DB_NAME) == SQLCipherUtils.State.UNENCRYPTED) {
                SQLCipherUtils.encrypt(context, context.getDatabasePath(DB_NAME), backupPassword.toByteArray())
            }
            //write DB to file
            context.getDatabasePath(DB_NAME).inputStream().copyTo(stream)
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