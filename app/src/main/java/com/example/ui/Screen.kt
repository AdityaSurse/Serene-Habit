package com.example.ui

sealed class Screen(val route: String) {
    object Today : Screen("today")
    object Stats : Screen("stats")
    object Settings : Screen("settings")
    object AddHabit : Screen("add_habit")
    object EditHabit : Screen("edit_habit/{habitId}") {
        fun createRoute(habitId: Int) = "edit_habit/$habitId"
    }
    object HabitDetail : Screen("habit_detail/{habitId}") {
        fun createRoute(habitId: Int) = "habit_detail/$habitId"
    }
}
