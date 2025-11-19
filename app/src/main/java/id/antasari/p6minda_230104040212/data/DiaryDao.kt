// Package sudah disesuaikan dengan ID Anda
package id.antasari.p6minda_230104040212.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow // <-- TAMBAHKAN IMPORT INI

@Dao
interface DiaryDao {

    // --- TAMBAHKAN FUNGSI BARU DI BAWAH INI ---
    @Query("SELECT * FROM diary_entries ORDER BY timestamp DESC")
    fun observeAll(): Flow<List<DiaryEntry>> //

    @Query("SELECT * FROM diary_entries ORDER BY timestamp DESC")
    suspend fun getAll(): List<DiaryEntry>

    @Query("SELECT * FROM diary_entries WHERE id = :entryId LIMIT 1")
    suspend fun getById(entryId: Int): DiaryEntry?

    @Insert
    suspend fun insert(entry: DiaryEntry): Long

    @Update
    suspend fun update(entry: DiaryEntry)

    @Delete
    suspend fun delete(entry: DiaryEntry)
}