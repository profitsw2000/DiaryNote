package diarynote.data.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import diarynote.data.room.mappers.Converter
import diarynote.data.room.dao.CategoryDao
import diarynote.data.room.dao.NoteDao
import diarynote.data.room.dao.UserDao
import diarynote.data.room.entity.CategoryEntity
import diarynote.data.room.entity.NoteEntity
import diarynote.data.room.entity.UserEntity

@Database(
    entities = [CategoryEntity::class, NoteEntity::class, UserEntity::class],
    version = 1,
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
        fun getInstance() = instance
            ?: throw java.lang.RuntimeException("Database has not been created. Please call create(context)")

        fun create(context: Context) {
            if (instance == null) {
                instance = Room.databaseBuilder(context, AppDatabase::class.java, DB_NAME)
                    .build()
            }
        }
    }
}