package id.antasari.p6minda_230104040212

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val MindaColorScheme = lightColorScheme(
    primary = Color(0xFF42A5F5),      // biru muda
    secondary = Color(0xFF90CAF9),    // biru lembut
    tertiary = Color(0xFFBBDEFB),     // biru sangat muda
    background = Color(0xFFE3F2FD),   // biru langit pucat
    surface = Color(0xFFFFFFFF),      // putih bersih
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
)

@Composable
fun MindaTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = MindaColorScheme,
        typography = MaterialTheme.typography,
        content = content
    )
}