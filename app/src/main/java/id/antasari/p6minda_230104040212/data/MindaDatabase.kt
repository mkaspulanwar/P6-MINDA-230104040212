package id.antasari.p6minda_230104040212.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [DiaryEntry::class],
    version = 1,
    exportSchema = false
)
abstract class MindaDatabase : RoomDatabase() {

    // Fungsi abstrak untuk mendapatkan DAO (Data Access Object)
    abstract fun diaryDao(): DiaryDao

    companion object {
        @Volatile
        private var INSTANCE: MindaDatabase? = null

        // Fungsi untuk mendapatkan instance database (Singleton)
        fun getInstance(context: Context): MindaDatabase {
            return INSTANCE ?: synchronized(this) {
                // Buat instance database jika INSTANCE masih null
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    MindaDatabase::class.java,
                    "minda.db"
                )
                    .build()
                    .also { db -> INSTANCE = db } // Tetapkan instance yang baru dibuat ke INSTANCE
            }
        }
    }
}