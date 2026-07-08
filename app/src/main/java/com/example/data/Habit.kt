package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "habits")
data class Habit(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val iconName: String, // e.g. "spa", "water_drop"
    val colorString: String, // Hex color
    val frequencyType: String, // "DAILY", "WEEKDAYS", "CUSTOM"
    val customDays: String, // comma-separated days "1,2,3"
    val dailyGoal: Int,
    val reminderTime: String?, // "HH:MM"
    val createdAt: Long = System.currentTimeMillis(),
    val userId: String = "guest"
)

@Entity(tableName = "habit_logs")
data class HabitLog(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val habitId: Int,
    val dateEpochDay: Long,
    val completed: Boolean
)
