package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.ui.HabitViewModel

@Composable
fun GoogleLogo(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.size(18.dp)) {
        val width = size.width
        val height = size.height
        val strokeWidth = width * 0.16f

        val red = Color(0xFFEA4335)
        val blue = Color(0xFF4285F4)
        val green = Color(0xFF34A853)
        val yellow = Color(0xFFFBBC05)

        // Draw top red arc
        drawArc(
            color = red,
            startAngle = 140f,
            sweepAngle = 100f,
            useCenter = false,
            style = Stroke(width = strokeWidth)
        )
        // Draw left yellow arc
        drawArc(
            color = yellow,
            startAngle = 240f,
            sweepAngle = 80f,
            useCenter = false,
            style = Stroke(width = strokeWidth)
        )
        // Draw bottom green arc
        drawArc(
            color = green,
            startAngle = 320f,
            sweepAngle = 100f,
            useCenter = false,
            style = Stroke(width = strokeWidth)
        )
        // Draw right blue arc
        drawArc(
            color = blue,
            startAngle = 60f,
            sweepAngle = 80f,
            useCenter = false,
            style = Stroke(width = strokeWidth)
        )
        // Draw blue horizontal bar
        drawLine(
            color = blue,
            start = Offset(width / 2f, height / 2f),
            end = Offset(width - strokeWidth / 2f, height / 2f),
            strokeWidth = strokeWidth
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavController, viewModel: HabitViewModel) {
    val isLoggedIn by viewModel.isLoggedIn.collectAsStateWithLifecycle()
    val userName by viewModel.currentUserName.collectAsStateWithLifecycle()
    val userEmail by viewModel.currentUserEmail.collectAsStateWithLifecycle()

    var showGooglePicker by remember { mutableStateOf(false) }
    var showSignOutConfirm by remember { mutableStateOf(false) }
    var showDeleteConfirm by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings", fontWeight = FontWeight.SemiBold) },
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
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Profile Card
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(
                                if (isLoggedIn) MaterialTheme.colorScheme.primaryContainer 
                                else MaterialTheme.colorScheme.surfaceContainerHigh
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isLoggedIn) {
                            Text(
                                text = userName.take(1).uppercase(),
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                fontWeight = FontWeight.Bold
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(userName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            if (isLoggedIn) {
                                Spacer(modifier = Modifier.width(8.dp))
                                Card(
                                    colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                                    ),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        "Google",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                        Text(userEmail, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }

            // Google Login Promo Card (Visible when Guest)
            if (!isLoggedIn) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f)
                        ),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.CloudSync,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(28.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    "Save Your Progress",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                "Sign in with Google to securely back up your habits, streaks, and completed targets, ensuring you never lose your routine.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(20.dp))

                            // Custom Interactive Google Button
                            Button(
                                onClick = { showGooglePicker = true },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.surface,
                                    contentColor = MaterialTheme.colorScheme.onSurface
                                ),
                                shape = RoundedCornerShape(24.dp),
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                                elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    GoogleLogo()
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(
                                        "Sign in with Google",
                                        style = MaterialTheme.typography.labelLarge,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }
                }
            }

            item {
                Text("PREFERENCES", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(bottom = 8.dp))
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    SettingItem(
                        icon = Icons.Default.DarkMode,
                        title = "Dark Mode",
                        subtitle = "Adjust interface brightness",
                        trailing = { Switch(checked = false, onCheckedChange = {}) }
                    )
                    SettingItem(
                        icon = Icons.Default.Notifications,
                        title = "Notifications",
                        subtitle = "Reminders for daily habits",
                        trailing = { Switch(checked = true, onCheckedChange = {}) }
                    )
                }
            }

            item {
                Text("YOUR CONTENT", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(bottom = 8.dp))
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    SettingItem(
                        icon = Icons.Default.Archive,
                        title = "Habit Archive",
                        subtitle = "View completed or paused journeys",
                        trailing = { /* Chevron */ }
                    )
                    SettingItem(
                        icon = Icons.Default.CloudSync,
                        title = "Cloud Backup",
                        subtitle = if (isLoggedIn) "Last synced: 2 minutes ago" else "Sign in to enable sync",
                        trailing = {
                            Button(
                                onClick = {}, 
                                enabled = isLoggedIn,
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f), contentColor = MaterialTheme.colorScheme.primary)
                            ) {
                                Text("Sync Now")
                            }
                        }
                    )
                    SettingItem(
                        icon = Icons.Default.Download,
                        title = "Export Data",
                        subtitle = "Download your history as JSON or CSV",
                        trailing = { /* MoreVert */ }
                    )
                }
            }

            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.15f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Warning, contentDescription = null, tint = MaterialTheme.colorScheme.error)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Account Security", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.error)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Permanent actions regarding your personal data and account access.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(start = 32.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            "Delete All Data",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier
                                .padding(start = 32.dp)
                                .clickable { showDeleteConfirm = true }
                        )
                        if (isLoggedIn) {
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                "Sign Out",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier
                                    .padding(start = 32.dp)
                                    .clickable { showSignOutConfirm = true }
                            )
                        }
                    }
                }
            }

            item {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Mindful Habits v2.4.0", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("Made with intention in California", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
                }
            }

            item { Spacer(modifier = Modifier.height(120.dp)) }
        }
    }

    // Google Account Picker Dialog Flow
    if (showGooglePicker) {
        var isRegisteringNew by remember { mutableStateOf(false) }
        var newName by remember { mutableStateOf("") }
        var newEmail by remember { mutableStateOf("") }
        var isLoggingIn by remember { mutableStateOf(false) }
        var selectedName by remember { mutableStateOf("") }
        var selectedEmail by remember { mutableStateOf("") }

        if (isLoggingIn) {
            AlertDialog(
                onDismissRequest = {},
                confirmButton = {},
                title = {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                    }
                },
                text = {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Signing in with Google...", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Setting up secure connection for $selectedEmail", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                    }
                }
            )
            
            LaunchedEffect(selectedEmail) {
                kotlinx.coroutines.delay(1500) // Simulate validation delay
                val shortId = selectedEmail.substringBefore("@")
                viewModel.loginWithGoogle(
                    id = shortId,
                    name = selectedName,
                    email = selectedEmail,
                    photoUrl = null,
                    mergeExisting = true
                )
                isLoggingIn = false
                showGooglePicker = false
            }
        } else if (isRegisteringNew) {
            AlertDialog(
                onDismissRequest = { isRegisteringNew = false },
                title = { Text("Add Google Account", fontWeight = FontWeight.Bold) },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        OutlinedTextField(
                            value = newName,
                            onValueChange = { newName = it },
                            label = { Text("Full Name") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = newEmail,
                            onValueChange = { newEmail = it },
                            label = { Text("Google Email") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            if (newName.isNotBlank() && newEmail.isNotBlank()) {
                                selectedName = newName
                                selectedEmail = newEmail
                                isLoggingIn = true
                            }
                        },
                        enabled = newName.isNotBlank() && newEmail.isNotBlank()
                    ) {
                        Text("Add Account")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { isRegisteringNew = false }) {
                        Text("Cancel")
                    }
                }
            )
        } else {
            AlertDialog(
                onDismissRequest = { showGooglePicker = false },
                title = {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(MaterialTheme.colorScheme.surface, CircleShape)
                                .border(1.dp, MaterialTheme.colorScheme.outlineVariant, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            GoogleLogo()
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("Choose an account", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        Text("to continue to Mindful Habits", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        val accounts = listOf(
                            "Aditya Surse" to "adityasurse42@gmail.com",
                            "Alex Rivers" to "alex.rivers@mindful.io"
                        )
                        
                        accounts.forEach { (name, email) ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        selectedName = name
                                        selectedEmail = email
                                        isLoggingIn = true
                                    },
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                            ) {
                                Row(
                                    modifier = Modifier.padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(40.dp)
                                            .background(MaterialTheme.colorScheme.primaryContainer, CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            name.take(1).uppercase(),
                                            style = MaterialTheme.typography.titleMedium,
                                            color = MaterialTheme.colorScheme.onPrimaryContainer
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(16.dp))
                                    Column {
                                        Text(name, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                                        Text(email, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                }
                            }
                        }

                        // Add custom account option
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { isRegisteringNew = true },
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh)
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.1f), CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Person,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.outline
                                    )
                                }
                                Spacer(modifier = Modifier.width(16.dp))
                                Text("Use another account", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                },
                confirmButton = {}
            )
        }
    }

    // Sign Out Confirmation Dialog
    if (showSignOutConfirm) {
        AlertDialog(
            onDismissRequest = { showSignOutConfirm = false },
            title = { Text("Sign Out", fontWeight = FontWeight.Bold) },
            text = { Text("Are you sure you want to sign out? Your habits and progress will remain saved on this account.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.logout()
                        showSignOutConfirm = false
                    }
                ) {
                    Text("Sign Out")
                }
            },
            dismissButton = {
                TextButton(onClick = { showSignOutConfirm = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Delete All Data Confirmation Dialog
    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = { Text("Delete All Data?", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.error) },
            text = { Text("This will permanently delete all your habits, completed logs, and streaks from this profile. This action cannot be undone.") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteCurrentUserAccountData()
                        showDeleteConfirm = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Permanently Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirm = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun SettingItem(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String, subtitle: String, trailing: @Composable () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(title, style = MaterialTheme.typography.bodyMedium)
                    Text(subtitle, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            trailing()
        }
    }
}
