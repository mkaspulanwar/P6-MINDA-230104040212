package id.antasari.p6minda_230104040212

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import id.antasari.p6minda_230104040212.ui.TestRoomScreen

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Mengatur konten UI menggunakan Jetpack Compose
        setContent {
            // Memanggil tema kustom yang telah didefinisikan (MindaTheme)
            MindaTheme {
                // Surface adalah wadah yang menerapkan warna latar belakang dari tema
                Surface(
                    modifier = Modifier.fillMaxSize(), // Membuat Surface mengisi seluruh layar
                    color = MaterialTheme.colorScheme.background // Menggunakan warna latar belakang dari tema
                ) {
                    // Memanggil Composable Screen utama yang berisi logika pengujian Room Database
                    TestRoomScreen()
                }
            }
        }
    }
}