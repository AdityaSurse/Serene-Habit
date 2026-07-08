package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.Habit
import com.example.data.HabitLog
import com.example.data.HabitRepository
import com.example.data.UserPreferences
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

@OptIn(ExperimentalCoroutinesApi::class)
class HabitViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: HabitRepository
    private val userPreferences = UserPreferences(application)
    
    init {
        val database = AppDatabase.getDatabase(application)
        repository = HabitRepository(database.habitDao())
    }

    private val _currentDate = MutableStateFlow(LocalDate.now())
    val currentDate: StateFlow<LocalDate> = _currentDate.asStateFlow()

    // Expose DataStore states
    val isLoggedIn: StateFlow<Boolean> = userPreferences.isLoggedIn.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )

    val currentUserId: StateFlow<String> = userPreferences.userId.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = "guest"
    )

    val currentUserName: StateFlow<String> = userPreferences.userName.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = "Guest User"
    )

    val currentUserEmail: StateFlow<String> = userPreferences.userEmail.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = "guest@mindful.io"
    )

    val currentUserPhotoUrl: StateFlow<String?> = userPreferences.userPhotoUrl.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    // Automatically load habits belonging to the current logged-in user
    val allHabits = currentUserId.flatMapLatest { userId ->
        repository.getHabitsForUser(userId)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val allLogsForUser = currentUserId.flatMapLatest { userId ->
        repository.getAllLogsForUser(userId)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val habitStreaks = kotlinx.coroutines.flow.combine(allHabits, allLogsForUser) { habits, logs ->
        val streaks = mutableMapOf<Int, Int>()
        val logsByHabit = logs.groupBy { it.habitId }
        val todayEpoch = LocalDate.now().toEpochDay()
        
        habits.forEach { habit ->
            val habitLogs = logsByHabit[habit.id] ?: emptyList()
            streaks[habit.id] = calculateStreak(habitLogs, todayEpoch)
        }
        streaks
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyMap()
    )

    private fun calculateStreak(logs: List<HabitLog>, todayEpoch: Long): Int {
        val completedDates = logs.filter { it.completed }.map { it.dateEpochDay }.toSet()
        if (completedDates.isEmpty()) return 0
        
        var streak = 0
        var checkDate = todayEpoch
        
        if (completedDates.contains(checkDate)) {
            while (completedDates.contains(checkDate)) {
                streak++
                checkDate--
            }
        } else {
            checkDate = todayEpoch - 1
            while (completedDates.contains(checkDate)) {
                streak++
                checkDate--
            }
        }
        return streak
    }

    val logsForToday = repository.getLogsForDate(LocalDate.now().toEpochDay()).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun getLogsForHabit(habitId: Int): StateFlow<List<HabitLog>> {
        return repository.getLogsForHabit(habitId).stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    }

    suspend fun getHabitById(id: Int): Habit? = repository.getHabitById(id)

    fun addHabit(habit: Habit) {
        viewModelScope.launch {
            // Associate new habit with active user
            repository.insertHabit(habit.copy(userId = currentUserId.value))
        }
    }

    fun updateHabit(habit: Habit) {
        viewModelScope.launch {
            repository.updateHabit(habit)
        }
    }

    fun deleteHabit(habit: Habit) {
        viewModelScope.launch {
            repository.deleteHabit(habit)
        }
    }

    fun toggleHabitCompletion(habitId: Int, epochDay: Long = LocalDate.now().toEpochDay()) {
        viewModelScope.launch {
            repository.toggleHabitCompletion(habitId, epochDay)
        }
    }

    // Google Login actions
    fun loginWithGoogle(id: String, name: String, email: String, photoUrl: String?, mergeExisting: Boolean = true) {
        viewModelScope.launch {
            if (mergeExisting) {
                try {
                    // Merge guest habits to the logged-in Google account
                    val guestHabits = repository.getHabitsForUser("guest").first()
                    guestHabits.forEach { habit ->
                        repository.updateHabit(habit.copy(userId = id))
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            userPreferences.saveUser(id, name, email, photoUrl)
        }
    }

    fun logout() {
        viewModelScope.launch {
            userPreferences.clearUser()
        }
    }

    fun deleteCurrentUserAccountData() {
        viewModelScope.launch {
            val userId = currentUserId.value
            repository.deleteDataForUser(userId)
            if (userId != "guest") {
                userPreferences.clearUser()
            }
        }
    }
}
