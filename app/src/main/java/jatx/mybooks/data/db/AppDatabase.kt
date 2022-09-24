package jatx.mybooks.data.db

import android.content.Context
import android.util.Log
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import jatx.mybooks.data.db.dao.BookDao
import jatx.mybooks.data.db.entity.BookEntity

@Database(
    entities = [
        BookEntity::class
    ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun wordDao(): BookDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context): AppDatabase = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also {
                Log.e("db", "building")
                instance = it
            }
        }

        private fun buildDatabase(context: Context) = Room
            .databaseBuilder(
                context,
                AppDatabase::class.java, "books.db"
            )
            .allowMainThreadQueries()
            .build()
    }
}