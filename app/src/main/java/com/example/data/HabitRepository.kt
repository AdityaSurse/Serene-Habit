package com.example.data

import kotlinx.coroutines.flow.Flow

class HabitRepository(private val habitDao: HabitDao) {
    val allHabits: Flow<List<Habit>> = habitDao.getAllHabits()

    fun getHabitsForUser(userId: String): Flow<List<Habit>> = habitDao.getHabitsForUser(userId)

    fun getAllLogsForUser(userId: String): Flow<List<HabitLog>> = habitDao.getAllLogsForUser(userId)

    fun getLogsForHabit(habitId: Int): Flow<List<HabitLog>> = habitDao.getLogsForHabit(habitId)

    fun getLogsForDate(epochDay: Long): Flow<List<HabitLog>> = habitDao.getLogsForDate(epochDay)

    suspend fun getHabitById(id: Int): Habit? = habitDao.getHabitById(id)

    suspend fun insertHabit(habit: Habit) {
        habitDao.insertHabit(habit)
    }

    suspend fun updateHabit(habit: Habit) {
        habitDao.updateHabit(habit)
    }

    suspend fun deleteHabit(habit: Habit) {
        habitDao.deleteHabit(habit)
        habitDao.deleteLogsForHabit(habit.id)
    }

    suspend fun deleteDataForUser(userId: String) {
        habitDao.deleteLogsForUser(userId)
        habitDao.deleteHabitsForUser(userId)
    }

    suspend fun toggleHabitCompletion(habitId: Int, epochDay: Long) {
        val existingLog = habitDao.getLogForHabitAndDate(habitId, epochDay)
        if (existingLog != null) {
            habitDao.insertLog(existingLog.copy(completed = !existingLog.completed))
        } else {
            habitDao.insertLog(HabitLog(habitId = habitId, dateEpochDay = epochDay, completed = true))
        }
    }
}
