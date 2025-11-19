package id.antasari.p6minda_230104040212.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import id.antasari.p6minda_230104040212.data.DiaryEntry
import id.antasari.p6minda_230104040212.data.DiaryRepository
import id.antasari.p6minda_230104040212.data.MindaDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.*
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewEntryScreen(
    onBack: () -> Unit,
    onSaved: (Int) -> Unit
) {
    val context = LocalContext.current
    val db = remember { MindaDatabase.getInstance(context) }
    val repo = remember { DiaryRepository(db.diaryDao()) }
    val scope = rememberCoroutineScope()

    var titleText by remember { mutableStateOf("") }
    var contentText by remember { mutableStateOf("") }

    val moodOptions = remember {
        listOf(
            "😊" to "Happy",
            "😌" to "Calm",
            "😔" to "Sad",
            "😠" to "Angry",
            "😩" to "Tired",
            "😎" to "Cool"
        )
    }
    var selectedMood by remember { mutableStateOf("😊") }

    val now = remember { ZonedDateTime.now() }
    var selectedDate by remember { mutableStateOf(now.toLocalDate()) }
    var selectedTime by remember { mutableStateOf(now.toLocalTime().withSecond(0).withNano(0)) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    val dateFormatter = remember { DateTimeFormatter.ofPattern("d MMM, yyyy", Locale.getDefault()) }
    val timeFormatter12h = remember { DateTimeFormatter.ofPattern("hh:mm a", Locale.getDefault()) }

    fun formattedDate(): String = selectedDate.format(dateFormatter)
    fun formattedTime(): String = selectedTime.format(timeFormatter12h)
    fun combineMillis(): Long {
        val ldt = LocalDateTime.of(selectedDate, selectedTime)
        return ldt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
    }

    fun doSave() {
        if (titleText.isBlank() || contentText.isBlank()) return
        scope.launch(Dispatchers.IO) {
            val newEntry = DiaryEntry(
                id = 0,
                title = titleText,
                content = contentText,
                mood = selectedMood,
                timestamp = combineMillis()
            )
            val newId = repo.add(newEntry).toInt()
            withContext(Dispatchers.Main) { onSaved(newId) }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "New Entry",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                actions = {
                    TextButton(
                        onClick = { doSave() },
                        enabled = titleText.isNotBlank() && contentText.isNotBlank()
                    ) {
                        Text(
                            "Done",
                            color = if (titleText.isNotBlank() && contentText.isNotBlank())
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                // ===== Baris tanggal & jam =====
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    Text(
                        text = formattedDate(),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.clickable { showDatePicker = true }
                    )
                    Text(text = ", ", style = MaterialTheme.typography.titleMedium)
                    Text(
                        text = formattedTime(),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.clickable { showTimePicker = true }
                    )
                }

                // ===== Judul & Konten =====
                OutlinedTextField(
                    value = titleText,
                    onValueChange = { titleText = it },
                    label = { Text("Title") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                        cursorColor = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(16.dp))
                OutlinedTextField(
                    value = contentText,
                    onValueChange = { contentText = it },
                    label = { Text("What's on your mind?") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 160.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                        cursorColor = MaterialTheme.colorScheme.primary
                    ),
                    singleLine = false,
                    maxLines = 8
                )
                Spacer(Modifier.height(16.dp))

                // ===== Mood Picker =====
                Text(
                    text = "Mood",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.height(8.dp))
                val row1 = remember { moodOptions.take(3) }
                val row2 = remember { moodOptions.drop(3).take(3) }

                MoodRow(
                    options = row1,
                    selected = selectedMood,
                    onSelect = { selectedMood = it }
                )
                Spacer(Modifier.height(8.dp))
                MoodRow(
                    options = row2,
                    selected = selectedMood,
                    onSelect = { selectedMood = it }
                )
            }

            // ===== Tombol bawah =====
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onBack,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.primary
                    )
                ) { Text("Cancel") }

                Button(
                    onClick = { doSave() },
                    enabled = titleText.isNotBlank() && contentText.isNotBlank(),
                    modifier = Modifier.weight(1f)
                ) { Text("Save") }
            }
        }

        // ===== DatePicker =====
        if (showDatePicker) {
            val datePickerState = rememberDatePickerState(
                initialSelectedDateMillis = selectedDate
                    .atStartOfDay(ZoneId.systemDefault())
                    .toInstant()
                    .toEpochMilli()
            )
            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    Button(onClick = {
                        val ms = datePickerState.selectedDateMillis
                        if (ms != null) {
                            selectedDate = Instant.ofEpochMilli(ms)
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate()
                        }
                        showDatePicker = false
                    }) { Text("Save") }
                },
                dismissButton = {
                    OutlinedButton(onClick = { showDatePicker = false }) { Text("Cancel") }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }

        // ===== TimePicker =====
        if (showTimePicker) {
            val timeState = rememberTimePickerState(
                initialHour = selectedTime.hour,
                initialMinute = selectedTime.minute,
                is24Hour = false
            )
            AlertDialog(
                onDismissRequest = { showTimePicker = false },
                confirmButton = {
                    Button(onClick = {
                        val h = timeState.hour
                        val m = timeState.minute
                        selectedTime = selectedTime.withHour(h).withMinute(m)
                        showTimePicker = false
                    }) { Text("Save") }
                },
                dismissButton = {
                    OutlinedButton(onClick = { showTimePicker = false }) { Text("Cancel") }
                },
                text = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        TimePicker(state = timeState)
                    }
                }
            )
        }
    }
}

/* ===== MoodRow & util warna (pakai MindaTheme) ===== */
@Composable
private fun MoodRow(
    options: List<Pair<String, String>>,
    selected: String,
    onSelect: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        options.forEach { (emoji, label) ->
            val color = moodColor(emoji)
            FilterChip(
                selected = selected == emoji,
                onClick = { onSelect(emoji) },
                label = { Text("$emoji $label") },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = color.copy(alpha = 0.20f),
                    selectedLabelColor = color,
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    labelColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    }
}

@Composable
private fun moodColor(mood: String): Color {
    val scheme = MaterialTheme.colorScheme
    return when (mood) {
        "😊" -> scheme.primary
        "😌" -> scheme.tertiary
        "😔" -> Color(0xFFE57373)
        "😠" -> Color(0xFFFF8A65)
        "😩" -> Color(0xFFBA68C8)
        "😎" -> Color(0xFF26A69A)
        else -> scheme.primary
    }
}