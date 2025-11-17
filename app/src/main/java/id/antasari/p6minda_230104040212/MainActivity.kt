package id.antasari.p6minda_230104040212

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import id.antasari.p6minda_230104040212.ui.NoteDetailScreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.launch
import id.antasari.p6minda_230104040212.data.MindaDatabase
import id.antasari.p6minda_230104040212.data.DiaryRepository
import androidx.compose.ui.platform.LocalContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MindaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // TEST ZONE
                    // Kita butuh 1 id entry nyata dari DB.
                    // Strategi: ambil entry pertama dari DB dan pakai id-nya.
                    val context = LocalContext.current
                    val scope = rememberCoroutineScope()

                    var firstId by remember { mutableStateOf<Int?>(null) }

                    LaunchedEffect(true) {
                        withContext(Dispatchers.IO) {
                            val db = MindaDatabase.getInstance(context)
                            val repo = DiaryRepository(db.diaryDao())

                            val all = repo.allEntries()
                            if (all.isNotEmpty()) {
                                firstId = all.first().id // ambil entry terbaru
                            } else {
                                // Kalau kosong banget, seed satu agar bisa dites
                                repo.addEntry(
                                    title = "Test detail view",
                                    content = "This is a sample diary entry for detail screen.\nHow do I feel today?",
                                    mood = "😊 Calm"
                                )
                                val again = repo.allEntries()
                                firstId = again.first().id
                            }
                        }
                    }

                    if (firstId != null) {
                        NoteDetailScreen(
                            entryId = firstId!!,
                            onBack = {
                                // sementara: tidak ada nav, jadi no-op
                            },
                            onDeleted = {
                                // sementara: tidak ada nav, bisa kosong
                            },
                            onEdit = { id ->
                                // nanti akan buka mode edit
                            }
                        )
                    } else {
                        // fallback simple text
                        androidx.compose.material3.Text(
                            "Loading entry...",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}