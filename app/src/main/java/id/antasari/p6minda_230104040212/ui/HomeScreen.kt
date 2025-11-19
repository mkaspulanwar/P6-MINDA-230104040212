// Package sudah disesuaikan dengan ID Anda
package id.antasari.p6minda_230104040212.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import id.antasari.p6minda_230104040212.R
import id.antasari.p6minda_230104040212.data.DiaryEntry
import id.antasari.p6minda_230104040212.data.DiaryRepository
import id.antasari.p6minda_230104040212.data.MindaDatabase
import id.antasari.p6minda_230104040212.util.formatTimestamp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    userName: String?,
    onOpenEntry: (Int) -> Unit
) {
    val context = LocalContext.current
    val db = remember { MindaDatabase.getInstance(context) }
    val repo = remember { DiaryRepository(db.diaryDao()) }
    var entries by remember { mutableStateOf<List<DiaryEntry>>(emptyList()) }
    var isSearching by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            val current = repo.allEntries()
            if (current.isEmpty()) {
                val sample = DiaryEntry(
                    id = 0,
                    title = "Gratitude journal",
                    content = "What am I thankful for today?\nWho made my day better?",
                    mood = "😊",
                    timestamp = System.currentTimeMillis()
                )
                repo.add(sample)
            }
            entries = repo.allEntries().sortedByDescending { it.timestamp }
        }
    }

    val filteredEntries = remember(entries, searchQuery) {
        val q = searchQuery.trim()
        if (q.isBlank()) entries
        else entries.filter { e ->
            e.title.contains(q, ignoreCase = true) ||
                    e.content.contains(q, ignoreCase = true)
        }
    }

    val gradientBackground = Brush.verticalGradient(
        colors = listOf(Color(0xFFB3E5FC), Color.White)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Minda",
                        color = Color.Black,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                },
                actions = {
                    IconButton(
                        onClick = {
                            isSearching = !isSearching
                            if (!isSearching) searchQuery = ""
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = Color.Black
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White, // 🔹 Top bar putih
                )
            )
        },
        containerColor = Color.Transparent
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradientBackground)
                .padding(innerPadding)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            ) {
                if (isSearching) {
                    item {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            label = { Text("Search your entries") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color.White.copy(alpha = 0.6f)),
                            singleLine = true
                        )
                    }
                }

                item {
                    Image(
                        painter = painterResource(id = R.drawable.banner),
                        contentDescription = "Diary banner",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .padding(12.dp)
                            .clip(RoundedCornerShape(24.dp))
                            .blur(0.5.dp),
                        contentScale = ContentScale.Crop
                    )
                }

                items(filteredEntries) { entry ->
                    DiaryListItem(
                        entry = entry,
                        onClick = { onOpenEntry(entry.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun DiaryListItem(
    entry: DiaryEntry,
    onClick: () -> Unit
) {
    val cardColor = Color.White // 🔹 Sekarang putih solid (tanpa transparansi)
    val localDateTime = Instant.ofEpochMilli(entry.timestamp)
        .atZone(ZoneId.systemDefault())
        .toLocalDateTime()
    val formatterDay = DateTimeFormatter.ofPattern("dd")
    val formatterMonth = DateTimeFormatter.ofPattern("MMM")
    val formatterYear = DateTimeFormatter.ofPattern("yyyy")

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(cardColor)
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.width(60.dp)
            ) {
                Text(
                    text = localDateTime.format(formatterDay),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0288D1)
                )
                Text(
                    text = localDateTime.format(formatterMonth),
                    fontSize = 14.sp,
                    color = Color(0xFF4FC3F7)
                )
                Text(
                    text = localDateTime.format(formatterYear),
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 12.dp)
            ) {
                Text(
                    text = formatTimestamp(entry.timestamp),
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray
                )
                Text(
                    text = entry.title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = Color(0xFF01579B)
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = entry.content.take(80),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF0277BD)
                )
            }
        }
    }
}