package com.example.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao {
    @Query("SELECT * FROM habits ORDER BY createdAt DESC")
    fun getAllHabits(): Flow<List<Habit>>

    @Query("SELECT * FROM habits WHERE userId = :userId ORDER BY createdAt DESC")
    fun getHabitsForUser(userId: String): Flow<List<Habit>>

    @Query("SELECT * FROM habits WHERE id = :id")
    suspend fun getHabitById(id: Int): Habit?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabit(habit: Habit): Long

    @Update
    suspend fun updateHabit(habit: Habit)

    @Delete
    suspend fun deleteHabit(habit: Habit)

    @Query("SELECT * FROM habit_logs WHERE habitId = :habitId ORDER BY dateEpochDay ASC")
    fun getLogsForHabit(habitId: Int): Flow<List<HabitLog>>

    @Query("SELECT * FROM habit_logs WHERE habitId IN (SELECT id FROM habits WHERE userId = :userId) ORDER BY dateEpochDay DESC")
    fun getAllLogsForUser(userId: String): Flow<List<HabitLog>>

    @Query("SELECT * FROM habit_logs WHERE habitId = :habitId AND dateEpochDay = :epochDay LIMIT 1")
    suspend fun getLogForHabitAndDate(habitId: Int, epochDay: Long): HabitLog?

    @Query("SELECT * FROM habit_logs WHERE dateEpochDay = :epochDay")
    fun getLogsForDate(epochDay: Long): Flow<List<HabitLog>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: HabitLog)
    
    @Query("DELETE FROM habit_logs WHERE habitId = :habitId")
    suspend fun deleteLogsForHabit(habitId: Int)

    @Query("DELETE FROM habits WHERE userId = :userId")
    suspend fun deleteHabitsForUser(userId: String)

    @Query("DELETE FROM habit_logs WHERE habitId IN (SELECT id FROM habits WHERE userId = :userId)")
    suspend fun deleteLogsForUser(userId: String)
}
