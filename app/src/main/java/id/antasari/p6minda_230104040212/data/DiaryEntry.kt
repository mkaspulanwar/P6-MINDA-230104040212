package id.antasari.p6minda_230104040212.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "diary_entries")
data class DiaryEntry(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val title: String, // ex: "Gratitude journal"
    val content: String, // ex: "What am I thankful for today? ..."
    val mood: String, // ex: "Happy", "Sad"
    val timestamp: Long // System.currentTimeMillis()
)