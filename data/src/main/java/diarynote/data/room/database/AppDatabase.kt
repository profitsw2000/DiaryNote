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
import diarynote.data.room.utils.SQLCipherUtils
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory
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

    val state: ByteArray = SQLiteDatabase.getBytes(charArrayOf('f', 'd'))
    val factory = SupportFactory(state)


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

        fun create(context: Context, passphrase: ByteArray) {
            if (instance == null) {
                instance = Room.databaseBuilder(context, AppDatabase::class.java, DB_NAME)
                    .addMigrations(MIGRATION_1_2)
                    .build()
            }
        }

        fun exists(context: Context) = context.getDatabasePath(DB_NAME).exists()

        fun copyTo(context: Context, stream: OutputStream) {
            context.getDatabasePath(DB_NAME).inputStream().copyTo(stream)
        }

        fun copyFrom(context: Context, stream: InputStream) {
            val dbFile = context.getDatabasePath(DB_NAME)

            dbFile.delete()
            stream.copyTo(dbFile.outputStream())
        }

        private fun addMigrationAndEncrypt(context: Context, passphrase: ByteArray, klass: Class<out RoomDatabase>, dbName: String) {
            val factory = SupportFactory(passphrase)
            val state = SQLCipherUtils.getDatabaseState(context, dbName)

            val installedVersion = try {
                SQLiteDatabase.openDatabase(
                    context.getDatabasePath(dbName).path,
                    userPassphrase,
                    null,
                    SQLiteDatabase.OPEN_READONLY
                ).version
            } catch () {
                MIGRATION_1_2.invoke().maxOf { migration -> migration.endVersion }
            }
        }
    }
}