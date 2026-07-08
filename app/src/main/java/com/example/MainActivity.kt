package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.ui.HabitViewModel
import com.example.ui.Screen
import com.example.ui.screens.AddEditHabitScreen
import com.example.ui.screens.HabitDetailScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.StatsScreen
import com.example.ui.screens.TodayScreen
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val viewModel: HabitViewModel = viewModel()
    
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val bottomBarDestinations = listOf(
        Triple(Screen.Today, Icons.Default.CalendarToday, "Today"),
        Triple(Screen.Stats, Icons.Default.BarChart, "Stats"),
        Triple(Screen.Settings, Icons.Default.Settings, "Settings")
    )
    val showBottomBar = bottomBarDestinations.any { it.first.route == currentDestination?.route }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.outlineVariant,
                            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
                        ),
                    containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                    tonalElevation = 0.dp
                ) {
                    bottomBarDestinations.forEach { (screen, icon, label) ->
                        val selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true
                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = label,
                                    tint = if (selected) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                )
                            },
                            label = { 
                                Text(
                                    text = label, 
                                    color = if (selected) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                ) 
                            },
                            colors = NavigationBarItemDefaults.colors(
                                indicatorColor = MaterialTheme.colorScheme.secondaryContainer,
                                selectedIconColor = MaterialTheme.colorScheme.onSecondaryContainer,
                                unselectedIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                selectedTextColor = MaterialTheme.colorScheme.onSurface,
                                unselectedTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Today.route,
            modifier = Modifier.padding(innerPadding) // Actually, we might want to ignore bottom padding for a floating bar, but we'll stick to innerPadding to avoid overlap
        ) {
            composable(Screen.Today.route) { TodayScreen(navController, viewModel) }
            composable(Screen.Stats.route) { StatsScreen(navController, viewModel) }
            composable(Screen.Settings.route) { SettingsScreen(navController, viewModel) }
            composable(Screen.AddHabit.route) { AddEditHabitScreen(navController, viewModel, null) }
            composable(Screen.EditHabit.route) { backStackEntry ->
                val habitId = backStackEntry.arguments?.getString("habitId")?.toIntOrNull()
                AddEditHabitScreen(navController, viewModel, habitId)
            }
            composable(Screen.HabitDetail.route) { backStackEntry ->
                val habitId = backStackEntry.arguments?.getString("habitId")?.toIntOrNull()
                HabitDetailScreen(navController, viewModel, habitId)
            }
        }
    }
}
