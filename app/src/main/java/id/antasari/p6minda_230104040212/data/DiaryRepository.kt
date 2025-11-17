package id.antasari.p6minda_230104040212.data

// Kelas Repository yang menerima DiaryDao sebagai dependency
class DiaryRepository(private val dao: DiaryDao) {

    // Fungsi untuk membuat objek DiaryEntry baru dan memasukkannya ke database
    suspend fun addEntry(
        title: String,
        content: String,
        mood: String
    ) {
        val entry = DiaryEntry(
            title = title,
            content = content,
            mood = mood,
            timestamp = System.currentTimeMillis()
        )
        dao.insert(entry)
    }

    // Fungsi untuk mengambil semua entri, langsung memanggil DAO
    suspend fun allEntries(): List<DiaryEntry> = dao.getAll()

    // Fungsi untuk menghapus entri, langsung memanggil DAO
    suspend fun remove(entry: DiaryEntry) = dao.delete(entry)

    // Fungsi untuk mengedit (update) entri, langsung memanggil DAO
    suspend fun edit(entry: DiaryEntry) = dao.update(entry)

    // READ satu entry by id (untuk Detail & Edit)
    suspend fun getById(id: Int): DiaryEntry? = dao.getById(id)
}