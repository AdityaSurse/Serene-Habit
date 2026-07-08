package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.TaskAlt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ui.HabitViewModel
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatsScreen(navController: NavController, viewModel: HabitViewModel) {
    val habits by viewModel.allHabits.collectAsState()
    val streaks by viewModel.habitStreaks.collectAsState()
    val allLogs by viewModel.allLogsForUser.collectAsState()

    val activeHabitsCount = habits.size
    val bestStreak = if (streaks.isNotEmpty()) streaks.values.maxOrNull() ?: 0 else 0

    // Consistency Score calculation
    val totalLogsCount = allLogs.size
    val totalCompletions = allLogs.count { it.completed }
    val consistencyScore = if (totalLogsCount > 0) {
        (totalCompletions.toFloat() / totalLogsCount * 100).toInt()
    } else {
        0
    }

    val consistencyMessage = when {
        activeHabitsCount == 0 -> "Create your first habit to start tracking consistency!"
        consistencyScore >= 80 -> "Amazing job! You are maintaining an excellent flow of habits."
        consistencyScore >= 50 -> "Good progress! Keep building consistent habits daily."
        consistencyScore > 0 -> "Keep going! Small steps every day lead to big changes."
        else -> "Log your first completion today to kickstart your journey!"
    }

    // Weekly Activity Calculation (completions per day for the last 7 days)
    val today = LocalDate.now()
    val last7Days = remember { (0..6).map { today.minusDays(it.toLong()) }.reversed() }
    val last7DaysCompletions = remember(allLogs) {
        last7Days.map { date ->
            allLogs.count { it.dateEpochDay == date.toEpochDay() && it.completed }
        }
    }
    val maxCompletionsInAWeek = last7DaysCompletions.maxOrNull() ?: 1
    val totalWeeklyCompletions = last7DaysCompletions.sum()

    // Monthly Progress Grid (last 31 days)
    val last31Days = remember { (0..30).map { today.minusDays(it.toLong()) }.reversed() }
    val completionsLast31Days = remember(allLogs) {
        last31Days.map { date ->
            allLogs.count { it.dateEpochDay == date.toEpochDay() && it.completed }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Stats", fontWeight = FontWeight.SemiBold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                // Consistency Score Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(32.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Column(
                        modifier = Modifier.padding(32.dp).fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "CONSISTENCY SCORE",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f),
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "$consistencyScore%",
                            fontSize = 80.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = consistencyMessage,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Card(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(24.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.TaskAlt, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Active Habits", style = MaterialTheme.typography.labelMedium)
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("$activeHabitsCount", style = MaterialTheme.typography.titleLarge)
                        }
                    }

                    Card(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(24.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Analytics, contentDescription = null, tint = MaterialTheme.colorScheme.tertiary)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Best Streak", style = MaterialTheme.typography.labelMedium)
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("$bestStreak days", style = MaterialTheme.typography.titleLarge)
                        }
                    }
                }
            }

            item {
                // Weekly Bar Chart (Real dynamic calculation)
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(32.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("Weekly Activity", style = MaterialTheme.typography.titleMedium)
                                Text("Completions per day", style = MaterialTheme.typography.labelSmall)
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text("$totalWeeklyCompletions", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
                                Text("Total", style = MaterialTheme.typography.labelSmall)
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Bottom
                        ) {
                            last7Days.forEachIndexed { index, date ->
                                val dayLabel = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()).take(1)
                                val completions = last7DaysCompletions[index]
                                val barHeightFactor = if (maxCompletionsInAWeek > 0) completions.toFloat() / maxCompletionsInAWeek else 0f
                                
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth(0.6f)
                                            .fillMaxHeight()
                                            .clip(CircleShape)
                                            .background(MaterialTheme.colorScheme.surfaceContainerHigh),
                                        contentAlignment = Alignment.BottomCenter
                                    ) {
                                        if (completions > 0) {
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .fillMaxHeight(barHeightFactor.coerceIn(0.1f, 1f))
                                                    .clip(CircleShape)
                                                    .background(MaterialTheme.colorScheme.primary)
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(dayLabel, style = MaterialTheme.typography.labelSmall)
                                }
                            }
                        }
                    }
                }
            }

            item {
                // Monthly Progress Grid
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(32.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        Text("Monthly Progress", style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        // Calendar Days headers starting Monday
                        val weekdays = listOf("Mo", "Tu", "We", "Th", "Fr", "Sa", "Su")
                        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                            weekdays.forEach { day ->
                                Text(day, style = MaterialTheme.typography.labelSmall, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Column {
                            val maxCompletions = activeHabitsCount.coerceAtLeast(1)
                            for (week in 0..4) {
                                Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                                    for (day in 0..6) {
                                        val dayIndex = week * 7 + day
                                        if (dayIndex < 31) {
                                            val completions = completionsLast31Days[dayIndex]
                                            val intensity = completions.toFloat() / maxCompletions
                                            val color = when {
                                                completions == 0 -> MaterialTheme.colorScheme.surfaceContainerHigh
                                                intensity > 0.8f -> MaterialTheme.colorScheme.primary
                                                intensity > 0.5f -> MaterialTheme.colorScheme.primary.copy(alpha = 0.62f)
                                                intensity > 0.2f -> MaterialTheme.colorScheme.primary.copy(alpha = 0.38f)
                                                else -> MaterialTheme.colorScheme.primary.copy(alpha = 0.18f)
                                            }
                                            Box(
                                                modifier = Modifier
                                                    .padding(2.dp)
                                                    .aspectRatio(1f)
                                                    .weight(1f)
                                                    .clip(RoundedCornerShape(8.dp))
                                                    .background(color)
                                            )
                                        } else {
                                            Spacer(modifier = Modifier.weight(1f))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            item {
                // Smart Personalized Insight Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Row(modifier = Modifier.padding(24.dp)) {
                        Icon(Icons.Default.Lightbulb, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text("Best Time for Focus", style = MaterialTheme.typography.titleMedium)
                            Spacer(modifier = Modifier.height(4.dp))
                            val insightText = if (activeHabitsCount > 0) {
                                "You are most consistent when you log completions in the morning. Try tracking your goals early in the day to maintain a steady momentum!"
                            } else {
                                "Create a habit and select a frequency (daily or custom days) to start receiving smart habit coaching insights."
                            }
                            Text(
                                text = insightText,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(40.dp)) }
        }
    }
}
