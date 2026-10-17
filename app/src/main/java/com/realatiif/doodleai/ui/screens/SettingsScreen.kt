package com.realatiif.doodleai.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.realatiif.doodleai.ui.components.AppTopBar
import com.realatiif.doodleai.ui.theme.AccentCyan
import com.realatiif.doodleai.ui.theme.NavyBg
import com.realatiif.doodleai.ui.theme.NavyBorder
import com.realatiif.doodleai.ui.theme.NavyCard
import com.realatiif.doodleai.ui.theme.NavySurface
import com.realatiif.doodleai.ui.theme.TextMuted
import com.realatiif.doodleai.ui.theme.TextWhite

@Composable
fun SettingsScreen(
    soundEnabled: Boolean,
    hapticsEnabled: Boolean,
    onSoundToggled: (Boolean) -> Unit,
    onHapticsToggled: (Boolean) -> Unit,
    onBack: () -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NavyBg)
    ) {
        AppTopBar(title = "Settings", onBack = onBack)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(20.dp)
        ) {
            Text(
                text = "Audio & Sensory",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = TextWhite,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // Sound Toggle
            SettingToggleCard(
                title = "Sound Effects",
                description = "Chimes, alerts, and feedback tones",
                icon = Icons.Default.VolumeUp,
                checked = soundEnabled,
                onCheckedChange = onSoundToggled
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Haptics Toggle
            SettingToggleCard(
                title = "Haptic Feedback",
                description = "Vibrations for draws, submits, and alerts",
                icon = Icons.Default.Vibration,
                checked = hapticsEnabled,
                onCheckedChange = onHapticsToggled
            )

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = "About Doodle AI",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = TextWhite,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(NavyCard)
                    .border(1.dp, NavyBorder, RoundedCornerShape(16.dp))
                    .padding(18.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Info",
                            tint = AccentCyan,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = "  Application Info",
                            color = TextWhite,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text("App: Doodle AI", color = TextWhite, fontSize = 13.sp)
                    Text("Tagline: Draw It. Let AI Guess It.", color = TextMuted, fontSize = 12.sp)
                    Text("Developer: Muhammad Atif", color = TextWhite, fontSize = 13.sp)
                    Text("Package: com.realatiif.doodleai", color = TextMuted, fontSize = 12.sp)
                    Text("Version: 1.0.0 (Release Build)", color = TextMuted, fontSize = 12.sp)
                    Text("Platform: Native Android • Jetpack Compose • TFLite", color = AccentCyan, fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
private fun SettingToggleCard(
    title: String,
    description: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(NavyCard)
            .border(1.dp, NavyBorder, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(NavySurface),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = AccentCyan,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Column(modifier = Modifier.padding(start = 14.dp)) {
                    Text(text = title, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    Text(text = description, fontSize = 12.sp, color = TextMuted)
                }
            }

            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = NavyBg,
                    checkedTrackColor = AccentCyan,
                    uncheckedThumbColor = TextMuted,
                    uncheckedTrackColor = NavySurface
                )
            )
        }
    }
}
