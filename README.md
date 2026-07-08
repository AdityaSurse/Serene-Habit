# 🌸 Serene Habit

**Serene Habit** is a polished, minimalist offline-first habit tracking application built with modern Android development practices using **Kotlin** and **Jetpack Compose**. It is designed to cultivate positive routines through simple tracking, visual motivation, and dynamic progress metrics.

---

## 🌟 Key Features

### 📅 Today Screen
*   **Intelligent Greeting**: Welcomes the user by name with a responsive header and localized dates.
*   **Habit Checklist**: Quick-toggle checkmarks for marking completions with smooth ripple indicators.
*   **Dynamic Streak Badges**: Real-time calculated streak indicators styled with a fire theme (`🔥 5 d`) for instant motivation.
*   **Progress Dashboard**: High-contrast, top-level progress bar demonstrating today's overall completion percentage.
*   **Floating Habit Creator**: An ergonomic Floating Action Button (FAB) designed to add new habits in one tap.

### 📊 Rich Analytics & Stats
*   **Consistency Score Card**: A premium custom header displaying a mathematically calculated consistency index alongside dynamic motivational coaching insights.
*   **Weekly Activity Bar Chart**: A custom-drawn, beautifully animated visual bar chart depicting completions per day over the last seven days.
*   **Monthly Progress Heatmap**: A contribution-style grid tracking the last 31 days of habit completions, color-coded by performance intensity.
*   **Personalized Smart Insights**: Dynamic lightbulb suggestions customized according to logging habits and daily tracking data.

### 🔍 Habit Deep Dive & Details
*   **Streak Overview**: Dedicated circular progress meters displaying current and longest completion streaks.
*   **Historic Calendar Log**: Interactive visual grid mapping previous completions.
*   **Modal Sheet Editor**: A clean Material 3 Bottom Sheet allowing users to edit, mark completion, or safely delete habits in place.

---

## 🛠️ Architecture & Tech Stack

This application is built in accordance with modern Android development standards, utilizing an **offline-first MVVM (Model-View-ViewModel)** architecture:

*   **UI Framework**: [Jetpack Compose](https://developer.android.com/jetpack/compose) — Built entirely with modern declarative UI APIs.
*   **Design Language**: [Material Design 3 (M3)](https://m3.material.io/) — Generous spacing, accessible typography, adaptive touch targets (minimum 48dp), and a customized primary color system.
*   **Database & Persistence**: [Room Database](https://developer.android.com/training/data-storage/room) — Highly efficient local SQLite storage with modern Kotlin coroutine flows to ensure data persistence works flawlessly offline.
*   **State Management**: `ViewModel` + `MutableStateFlow` paired with `collectAsStateWithLifecycle` to prevent resource leaks.
*   **Navigation**: [Jetpack Navigation Compose](https://developer.android.com/guide/navigation/navigation-compose) — Secure type-safe routes managing full multi-screen backstack transactions.
*   **Language**: Kotlin (100%) utilizing asynchronous Coroutines and Flow APIs.

---

## 📦 Directory Structure

```text
app/src/main/java/com/example/
├── data/                       # Local database entity configurations
│   ├── Habit.kt                # Habit and HabitLog entities (Room Schema)
│   ├── HabitDao.kt             # SQLite Queries & custom user logic
│   ├── HabitDatabase.kt        # Room database configuration
│   └── HabitRepository.kt      # Unifies local state across components
├── ui/                         # Presentation Layer
│   ├── screens/                # Jetpack Compose Screens
│   │   ├── TodayScreen.kt      # Main tracking and check-off center
│   │   ├── StatsScreen.kt      # Rich graphs, bar charts, and heatmaps
│   │   ├── HabitDetailScreen.kt# Individual history and bottom sheet
│   │   ├── AddEditHabitScreen.kt# Schedule & customization options
│   │   └── SettingsScreen.kt   # Authentication and user profiles
│   ├── theme/                  # Material 3 Color Schemes & Typography
│   └── HabitViewModel.kt       # Streamlined ViewModel handling all logic
└── MainActivity.kt             # Single-activity container and NavHost
```

---

## 🚀 Getting Started

### Prerequisites
*   Android Studio Ladybug (or newer)
*   JDK 17+
*   Android SDK Platform 34 (Android 14)

### Local Setup & Compilation

1.  **Clone the Repository**:
    ```bash
    git clone https://github.com/yourusername/serene-habit.git
    cd serene-habit
    ```

2.  **Open in Android Studio**:
    Launch Android Studio, select **Open**, and navigate to the project root directory.

3.  **Build the Project**:
    Allow Gradle to sync dependencies automatically. Run the clean build to compile the applet:
    ```bash
    ./gradlew assembleDebug
    ```

4.  **Run the App**:
    Deploy the build to an Android Emulator or physical device by clicking the **Run** button (green play icon) in Android Studio.

---

## 🎨 Visual Identity

The interface of **Serene Habit** leverages visual harmony to foster relaxation and task clarity:
*   **Negative Space**: Built with expansive edge padding to avoid visual clutter.
*   **Typography**: Clean sans-serif pairings coupled with custom monospaced elements for data metrics and streaks.
*   **Theme Integration**: Supports systemic Dark/Light scheduling seamlessly matching Material Design color scheme resources.
