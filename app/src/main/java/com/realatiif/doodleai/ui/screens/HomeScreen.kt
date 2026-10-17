package com.realatiif.doodleai.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.realatiif.doodleai.data.local.UserGameStats
import com.realatiif.doodleai.ui.theme.AccentCyan
import com.realatiif.doodleai.ui.theme.AccentPurple
import com.realatiif.doodleai.ui.theme.AccentYellow
import com.realatiif.doodleai.ui.theme.NavyBg
import com.realatiif.doodleai.ui.theme.NavyBorder
import com.realatiif.doodleai.ui.theme.NavyCard
import com.realatiif.doodleai.ui.theme.NavySurface
import com.realatiif.doodleai.ui.theme.TextMuted
import com.realatiif.doodleai.ui.theme.TextWhite

@Composable
fun HomeScreen(
    userStats: UserGameStats,
    onPlayClicked: () -> Unit,
    onTutorialClicked: () -> Unit,
    onProgressClicked: () -> Unit,
    onModelInfoClicked: () -> Unit,
    onSettingsClicked: () -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NavyBg)
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Hero Branding Header
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(AccentCyan.copy(alpha = 0.3f), Color.Transparent)
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(NavyCard)
                    .border(1.5.dp, AccentCyan, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = "Logo",
                    tint = AccentCyan,
                    modifier = Modifier.size(28.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Doodle AI",
            fontSize = 32.sp,
            fontWeight = FontWeight.ExtraBold,
            color = TextWhite,
            letterSpacing = 0.5.sp
        )

        Text(
            text = "Draw It. Let AI Guess It.",
            fontSize = 15.sp,
            color = AccentCyan,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(28.dp))

        // High Score Banner
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(NavyCard)
                .border(1.dp, NavyBorder, RoundedCornerShape(20.dp))
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(AccentYellow.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.WorkspacePremium,
                            contentDescription = "Trophy",
                            tint = AccentYellow,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text("Personal Best", fontSize = 12.sp, color = TextMuted)
                        Text("${userStats.highScore} pts", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text("Accuracy", fontSize = 12.sp, color = TextMuted)
                    Text("${userStats.overallAccuracy}%", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = AccentCyan)
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Big Play Button
        Button(
            onClick = onPlayClicked,
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(containerColor = AccentCyan),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = null,
                    tint = NavyBg,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "PLAY GAME",
                    color = NavyBg,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Navigation Grid
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            HomeMenuButton(
                title = "How to Play",
                subtitle = "Rules, scoring, and drawing tips",
                icon = Icons.Default.HelpOutline,
                accentColor = AccentPurple,
                onClick = onTutorialClicked
            )

            HomeMenuButton(
                title = "My Progress",
                subtitle = "Scores, streaks, and drawing stats",
                icon = Icons.Default.BarChart,
                accentColor = AccentCyan,
                onClick = onProgressClicked
            )

            HomeMenuButton(
                title = "AI Model Info",
                subtitle = "Quick, Draw! CNN architecture & specs",
                icon = Icons.Default.Memory,
                accentColor = AccentYellow,
                onClick = onModelInfoClicked
            )

            HomeMenuButton(
                title = "Settings",
                subtitle = "Sound effects and haptics",
                icon = Icons.Default.Settings,
                accentColor = TextMuted,
                onClick = onSettingsClicked
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Developer: Muhammad Atif • Native Android & TFLite",
            fontSize = 11.sp,
            color = TextMuted
        )
    }
}

@Composable
private fun HomeMenuButton(
    title: String,
    subtitle: String,
    icon: ImageVector,
    accentColor: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(NavyCard)
            .border(1.dp, NavyBorder, RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(accentColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = accentColor,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite
                )
                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = TextMuted
                )
            }
        }
    }
}
