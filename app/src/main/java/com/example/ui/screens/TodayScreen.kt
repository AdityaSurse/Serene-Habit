package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.data.Habit
import com.example.data.HabitLog
import com.example.ui.HabitViewModel
import com.example.ui.Screen
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodayScreen(navController: NavController, viewModel: HabitViewModel) {
    val currentDate by viewModel.currentDate.collectAsState()
    val habits by viewModel.allHabits.collectAsState()
    val logs by viewModel.logsForToday.collectAsState()
    val streaks by viewModel.habitStreaks.collectAsState()
    val isLoggedIn by viewModel.isLoggedIn.collectAsState()
    val userName by viewModel.currentUserName.collectAsState()

    val dayOfWeek = currentDate.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
    val formattedDate = "$dayOfWeek, ${currentDate.format(DateTimeFormatter.ofPattern("MMMM dd, yyyy"))}"
    
    val completedCount = logs.count { it.completed }
    val totalCount = habits.size
    val progress = if (totalCount > 0) completedCount.toFloat() / totalCount else 0f

    val firstName = remember(userName) {
        val nameToSplit = if (userName.isBlank()) "Guest" else userName
        nameToSplit.trim().substringBefore(" ")
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Column {
                        Text("Good morning, $firstName", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Medium)
                        Text(formattedDate, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                },
                actions = {
                    Box(
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .size(40.dp)
                            .background(
                                if (isLoggedIn) MaterialTheme.colorScheme.primaryContainer 
                                else MaterialTheme.colorScheme.surfaceVariant, 
                                CircleShape
                            )
                            .clickable { navController.navigate(Screen.Settings.route) },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (userName.isNotBlank()) userName.take(1).uppercase() else "G", 
                            style = MaterialTheme.typography.titleMedium, 
                            color = if (isLoggedIn) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant, 
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(Screen.AddHabit.route) },
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.padding(bottom = 16.dp, end = 8.dp), // Fixed compounding bottom padding (parent NavHost already pads bottom nav bar height)
                shape = RoundedCornerShape(18.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Habit", modifier = Modifier.size(24.dp))
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                    elevation = CardDefaults.cardElevation(0.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp).fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Bottom
                        ) {
                            Column {
                                Text("Daily Goal", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSecondaryContainer, fontWeight = FontWeight.Medium)
                                Text("${(progress * 100).toInt()}%", fontSize = 32.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSecondaryContainer)
                            }
                            Text(
                                "${completedCount} of ${totalCount} Habits",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        LinearProgressIndicator(
                            progress = { progress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(12.dp)
                                .clip(CircleShape),
                            color = MaterialTheme.colorScheme.primary,
                            trackColor = MaterialTheme.colorScheme.background.copy(alpha = 0.5f)
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(32.dp))
            }

            items(habits) { habit ->
                val log = logs.find { it.habitId == habit.id }
                val isCompleted = log?.completed == true
                val streak = streaks[habit.id] ?: 0
                
                HabitCard(
                    habit = habit,
                    isCompleted = isCompleted,
                    streak = streak,
                    onClick = { navController.navigate(Screen.HabitDetail.createRoute(habit.id)) },
                    onToggle = { viewModel.toggleHabitCompletion(habit.id, currentDate.toEpochDay()) }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
            
            // Padding for FAB and Bottom Nav
            item { Spacer(modifier = Modifier.height(80.dp)) }
        }
    }
}

@Composable
fun HabitCard(
    habit: Habit,
    isCompleted: Boolean,
    streak: Int,
    onClick: () -> Unit,
    onToggle: () -> Unit
) {
    val cardColor = if (isCompleted) MaterialTheme.colorScheme.surfaceContainerHighest else MaterialTheme.colorScheme.surfaceVariant
    val contentAlpha = if (isCompleted) 0.6f else 1f
    val iconBg = if (isCompleted) MaterialTheme.colorScheme.outlineVariant else MaterialTheme.colorScheme.primaryContainer
    val iconTextColor = if (isCompleted) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onPrimaryContainer

    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .alpha(contentAlpha),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(iconBg),
                contentAlignment = Alignment.Center
            ) {
                Text(habit.name.take(1).uppercase(), color = iconTextColor, fontSize = 20.sp)
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = habit.name,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.SemiBold,
                        textDecoration = if (isCompleted) TextDecoration.LineThrough else null,
                        modifier = Modifier.weight(1f, fill = false)
                    )
                    
                    if (streak > 0) {
                        Surface(
                            color = if (isCompleted) MaterialTheme.colorScheme.outlineVariant else MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.8f),
                            contentColor = if (isCompleted) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onErrorContainer,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.height(20.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(2.dp)
                            ) {
                                Text(
                                    text = "🔥 $streak d",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, fontSize = 10.sp)
                                )
                            }
                        }
                    }
                }
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 4.dp)) {
                    if (isCompleted) {
                        Text("Done", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    } else {
                        val badgeBg = if (streak > 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline.copy(alpha = 0.15f)
                        val badgeText = if (streak > 0) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                        Box(
                            modifier = Modifier
                                .background(badgeBg, RoundedCornerShape(6.dp))
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = if (streak > 0) "$streak day streak" else "0 streak", 
                                style = MaterialTheme.typography.labelSmall, 
                                color = badgeText
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(habit.frequencyType, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
            
            IconButton(
                onClick = onToggle,
                modifier = Modifier
                    .size(32.dp)
                    .border(
                        width = 2.dp,
                        color = MaterialTheme.colorScheme.primary,
                        shape = CircleShape
                    )
                    .background(
                        color = if (isCompleted) MaterialTheme.colorScheme.primary else Color.Transparent,
                        shape = CircleShape
                    )
            ) {
                if (isCompleted) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Complete",
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}
