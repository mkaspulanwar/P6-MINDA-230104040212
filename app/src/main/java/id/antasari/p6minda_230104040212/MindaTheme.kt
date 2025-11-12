package id.antasari.p6minda_230104040212

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

// Mendefinisikan skema warna (color scheme) dasar untuk tema
private val MindaColorScheme = lightColorScheme(
    /* * Skema warna Material 3 standar (lightColorScheme) digunakan.
     * Warna dapat dikustomisasi di sini dengan parameter
     * primary, secondary, tertiary, background, surface, dll.
     * Contoh:
     * primary = Color(0xFF6200EE),
     * secondary = Color(0xFF03DAC5)
     */
)

@Composable
fun MindaTheme(content: @Composable () -> Unit) {
    // Mengaplikasikan tema Material 3 ke konten aplikasi
    MaterialTheme(
        colorScheme = MindaColorScheme, // Menggunakan skema warna yang didefinisikan di atas
        typography = MaterialTheme.typography, // Menggunakan tipografi default dari MaterialTheme
        content = content // Konten Composable yang akan menggunakan tema ini
    )
}