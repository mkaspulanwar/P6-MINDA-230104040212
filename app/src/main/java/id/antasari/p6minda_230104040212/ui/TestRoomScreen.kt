package id.antasari.p6minda_230104040212.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext // <- IMPORT PENTING
import androidx.compose.ui.unit.dp
import id.antasari.p6minda_230104040212.data.DiaryEntry
import id.antasari.p6minda_230104040212.data.DiaryRepository
import id.antasari.p6minda_230104040212.data.MindaDatabase
import id.antasari.p6minda_230104040212.util.formatTimestamp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun TestRoomScreen() {
    // 1. Inisialisasi Database dan Repository
    val context = LocalContext.current
    // Mendapatkan instance database (Singleton)
    val db = remember { MindaDatabase.getInstance(context) }
    // Membuat instance Repository dengan DAO
    val repo = remember { DiaryRepository(db.diaryDao()) }

    // Variabel state untuk menyimpan daftar entri yang diambil dari database
    var entries by remember { mutableStateOf(listOf<DiaryEntry>()) }
    // Scope untuk menjalankan coroutines
    val scope = rememberCoroutineScope()

    // 2. Seed Data (Mengisi data awal) dan Fetch Data
    LaunchedEffect(true) {
        withContext(Dispatchers.IO) { // Pindah ke thread I/O untuk operasi database
            // Jika database kosong, tambahkan data awal (Seed Data)
            if (repo.allEntries().isEmpty()) {
                // Entri 1
                repo.addEntry(
                    title = "My Day 1",
                    content = "🌟 What was the best thing about today and why?\n" +
                            "💪 What were the challenges today and how did I overcome them?\n" +
                            "💙 What was my emotional state today?",
                    mood = "Happy"
                )
                // Entri 2
                repo.addEntry(
                    title = "Gratitude journal",
                    content = "✨ What am I thankful for today?\n" +
                            "🥺 Who made my day better?",
                    mood = "Sad"
                )
            }
            // Setelah seeding atau jika sudah ada data, ambil semua entri
            entries = repo.allEntries()
        }
    }

    // 3. UI
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Minda - Your mind, in one place.",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(Modifier.height(12.dp))

        Button(
            onClick = {
                // Luncurkan coroutine untuk operasi I/O (Database)
                scope.launch(Dispatchers.IO) {
                    // 1. Tambahkan entri baru ke database
                    repo.addEntry(
                        title = "New note from button",
                        content = "This is a new offline diary entry.",
                        mood = "Calm"
                    )

                    // 2. Ambil semua entri yang sudah diperbarui
                    val updated = repo.allEntries()

                    // 3. Pindah kembali ke Main thread (UI thread) untuk memperbarui state
                    withContext(Dispatchers.Main) {
                        entries = updated
                    }
                }
            }
        ) {
            Text("Add dummy entry")
        }

        Spacer(Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            // Membuat item untuk setiap entri dalam daftar 'entries'
            items(entries) { entry ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp) // Spasi di bawah setiap kartu
                ) {
                    Column(Modifier.padding(12.dp)) {

                        // Judul (Title)
                        Text(
                            text = entry.title,
                            style = MaterialTheme.typography.titleMedium
                        )

                        // Mood (dengan contoh emoji)
                        Text(
                            // Mood kecil, misal: "😊 Happy"
                            text = "Mood: ${entry.mood}",
                            style = MaterialTheme.typography.labelSmall
                        )

                        Spacer(Modifier.height(4.dp))

                        // Konten (Content)
                        Text(
                            text = entry.content,
                            style = MaterialTheme.typography.bodyMedium
                        )

                        Spacer(Modifier.height(8.dp))

                        // Timestamp (Difomat)
                        Text(
                            text = formatTimestamp(entry.timestamp),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}