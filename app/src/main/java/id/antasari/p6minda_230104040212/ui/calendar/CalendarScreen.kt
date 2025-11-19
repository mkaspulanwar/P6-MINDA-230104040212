package id.antasari.p6minda_230104040212.ui.calendar

import android.app.Application
import android.content.res.Configuration
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import id.antasari.p6minda_230104040212.data.DiaryEntry
import java.time.*
import java.time.format.TextStyle
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    onEdit: (Int) -> Unit = {}
) {
    val app = LocalContext.current.applicationContext as Application
    val vm: CalendarViewModel = viewModel(factory = CalendarViewModel.provideFactory(app))
    val diaryByDate = vm.diaryByDate.collectAsStateWithLifecycle(emptyMap()).value
    val today = remember { LocalDate.now() }
    var visibleMonth by remember { mutableStateOf(YearMonth.of(today.year, today.month)) }
    var selectedDate by remember { mutableStateOf(today) }

    fun goPrevMonth() { visibleMonth = visibleMonth.minusMonths(1); selectedDate = visibleMonth.atDay(1) }
    fun goNextMonth() { visibleMonth = visibleMonth.plusMonths(1); selectedDate = visibleMonth.atDay(1) }

    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE
    val pagePadding = 16.dp
    val gridSpacing = 4.dp
    val cellHeight = 42.dp
    val portraitGridHeight = 240.dp

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = pagePadding)
    ) {
        MonthHeaderWithDayMonthYearPicker(
            visibleMonth = visibleMonth,
            selectedDate = selectedDate,
            onPick = { d -> selectedDate = d; visibleMonth = YearMonth.from(d) }
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(gridSpacing),
            verticalAlignment = Alignment.CenterVertically
        ) {
            DayOfWeek.entries.forEach { dow ->
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        dow.getDisplayName(TextStyle.SHORT, Locale.ENGLISH),
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        Spacer(Modifier.height(8.dp))

        val cells = remember(visibleMonth) { buildMonthCells(visibleMonth) }
        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            verticalArrangement = Arrangement.spacedBy(gridSpacing),
            horizontalArrangement = Arrangement.spacedBy(gridSpacing),
            modifier = Modifier
                .fillMaxWidth()
                .shadow(1.dp, RoundedCornerShape(12.dp))
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.surfaceContainer)
                .then(if (isLandscape) Modifier.weight(1f) else Modifier.height(portraitGridHeight))
                .pointerInput(visibleMonth) {
                    var acc = 0f
                    detectHorizontalDragGestures(
                        onHorizontalDrag = { _, drag -> acc += drag },
                        onDragEnd = {
                            val threshold = 60f
                            if (acc > threshold) goPrevMonth()
                            if (acc < -threshold) goNextMonth()
                            acc = 0f
                        }
                    )
                }
                .padding(8.dp)
        ) {
            items(cells) { c ->
                DayCell(
                    date = c.date,
                    inCurrentMonth = c.inCurrentMonth,
                    selected = c.date == selectedDate,
                    hasDiary = diaryByDate[c.date]?.isNotEmpty() == true,
                    cellHeight = cellHeight,
                    onClick = {
                        selectedDate = c.date
                        visibleMonth = YearMonth.from(c.date)
                    }
                )
            }
        }

        Spacer(Modifier.height(8.dp))
        Divider(thickness = 1.dp, color = MaterialTheme.colorScheme.outlineVariant)
        Spacer(Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .then(if (isLandscape) Modifier.weight(1f) else Modifier)
        ) {
            DiaryListForDate(
                date = selectedDate,
                entries = diaryByDate[selectedDate].orEmpty(),
                onEdit = onEdit,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
private fun MonthHeaderWithDayMonthYearPicker(
    visibleMonth: YearMonth,
    selectedDate: LocalDate,
    onPick: (LocalDate) -> Unit
) {
    var showPicker by remember { mutableStateOf(false) }
    val currentYear = LocalDate.now().year
    val monthName = visibleMonth.month.getDisplayName(TextStyle.FULL, Locale.ENGLISH)
        .replaceFirstChar { it.titlecase(Locale.ENGLISH) }
    val headerText = if (visibleMonth.year == currentYear) monthName else "$monthName ${visibleMonth.year}"

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .clickable { showPicker = true }
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(
                text = headerText,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(Modifier.width(6.dp))
            Text(
                text = "▼",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }
        Spacer(Modifier.weight(1f))
    }

    if (showPicker) {
        DayMonthYearPickerDialog(
            initial = selectedDate,
            onDismiss = { showPicker = false },
            onConfirm = { picked ->
                onPick(picked)
                showPicker = false
            }
        )
    }
}

@Composable
private fun DayCell(
    date: LocalDate,
    inCurrentMonth: Boolean,
    selected: Boolean,
    hasDiary: Boolean,
    cellHeight: Dp,
    onClick: () -> Unit
) {
    val primary = MaterialTheme.colorScheme.primary
    val onPrimary = MaterialTheme.colorScheme.onPrimary
    val textColor = if (inCurrentMonth) MaterialTheme.colorScheme.onSurface
    else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(cellHeight)
            .clip(RoundedCornerShape(10.dp))
            .clickable(onClick = onClick)
            .background(if (selected) primary.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surfaceContainerLowest),
        contentAlignment = Alignment.Center
    ) {
        if (selected) {
            Box(
                Modifier
                    .clip(CircleShape)
                    .background(primary)
                    .padding(horizontal = 10.dp, vertical = 5.dp)
            ) {
                Text(
                    date.dayOfMonth.toString(),
                    color = onPrimary,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold
                )
            }
        } else {
            Text(
                date.dayOfMonth.toString(),
                color = textColor,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Medium
            )
        }

        if (hasDiary) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .align(Alignment.BottomCenter)
                    .offset(y = (-6).dp)
                    .clip(CircleShape)
                    .background(if (selected) onPrimary else primary)
            )
        }
    }
}

@Composable
fun DiaryListForDate(
    date: LocalDate,
    entries: List<DiaryEntry>,
    onEdit: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier.padding(8.dp)) {
        Text(
            text = "Diary - ${date.toString()}",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(Modifier.height(8.dp))

        if (entries.isEmpty()) {
            Text(
                "Tidak ada catatan untuk tanggal ini.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(entries.size) { i ->
                    val entry = entries[i]
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onEdit(entry.id) },
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(Modifier.padding(12.dp)) {
                            Text(
                                entry.title,
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                entry.content,
                                style = MaterialTheme.typography.bodySmall,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DayMonthYearPickerDialog(
    initial: LocalDate,
    onDismiss: () -> Unit,
    onConfirm: (LocalDate) -> Unit
) {
    var selectedYear by remember { mutableStateOf(initial.year) }
    var selectedMonth by remember { mutableStateOf(initial.monthValue) }
    var selectedDay by remember { mutableStateOf(initial.dayOfMonth) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Pilih Tanggal") },
        text = {
            Column {
                Text("Tahun: $selectedYear")
                Slider(
                    value = selectedYear.toFloat(),
                    onValueChange = { selectedYear = it.toInt() },
                    valueRange = 2000f..2100f,
                    steps = 100
                )

                Text("Bulan: $selectedMonth")
                Slider(
                    value = selectedMonth.toFloat(),
                    onValueChange = { selectedMonth = it.toInt() },
                    valueRange = 1f..12f,
                    steps = 11
                )

                Text("Hari: $selectedDay")
                Slider(
                    value = selectedDay.toFloat(),
                    onValueChange = { selectedDay = it.toInt() },
                    valueRange = 1f..31f,
                    steps = 30
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                onConfirm(LocalDate.of(selectedYear, selectedMonth, selectedDay))
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Batal")
            }
        }
    )
}

private data class MonthCell(
    val date: LocalDate,
    val inCurrentMonth: Boolean
)

private fun buildMonthCells(month: YearMonth): List<MonthCell> {
    val firstDay = month.atDay(1)
    val lastDay = month.atEndOfMonth()
    val firstDayOfWeek = firstDay.dayOfWeek.value % 7
    val totalDays = lastDay.dayOfMonth

    val prevMonth = month.minusMonths(1)
    val prevMonthDays = prevMonth.atEndOfMonth().dayOfMonth
    val cells = mutableListOf<MonthCell>()

    for (i in firstDayOfWeek - 1 downTo 0) {
        cells.add(MonthCell(prevMonth.atDay(prevMonthDays - i), false))
    }
    for (day in 1..totalDays) {
        cells.add(MonthCell(month.atDay(day), true))
    }
    val nextMonth = month.plusMonths(1)
    val nextDaysNeeded = 42 - cells.size
    for (day in 1..nextDaysNeeded) {
        cells.add(MonthCell(nextMonth.atDay(day), false))
    }

    return cells
}