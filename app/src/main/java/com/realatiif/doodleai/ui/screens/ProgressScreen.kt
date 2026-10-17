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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Gesture
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.realatiif.doodleai.data.local.UserGameStats
import com.realatiif.doodleai.ui.components.AppTopBar
import com.realatiif.doodleai.ui.components.StatCard
import com.realatiif.doodleai.ui.theme.AccentCyan
import com.realatiif.doodleai.ui.theme.AccentPurple
import com.realatiif.doodleai.ui.theme.AccentYellow
import com.realatiif.doodleai.ui.theme.ErrorRed
import com.realatiif.doodleai.ui.theme.NavyBg
import com.realatiif.doodleai.ui.theme.NavyBorder
import com.realatiif.doodleai.ui.theme.NavyCard
import com.realatiif.doodleai.ui.theme.SuccessGreen
import com.realatiif.doodleai.ui.theme.TextMuted
import com.realatiif.doodleai.ui.theme.TextWhite

@Composable
fun ProgressScreen(
    userStats: UserGameStats,
    onClearHistory: () -> Unit,
    onBack: () -> Unit
) {
    var showResetDialog by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NavyBg)
    ) {
        AppTopBar(title = "My Progress", onBack = onBack)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(20.dp)
        ) {
            // Lifetime High Score Hero
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(NavyCard)
                    .border(1.5.dp, AccentYellow.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(AccentYellow.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.WorkspacePremium,
                            contentDescription = "Trophy",
                            tint = AccentYellow,
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    Column(modifier = Modifier.padding(start = 16.dp)) {
                        Text("All-Time High Score", fontSize = 13.sp, color = TextMuted)
                        Text(
                            "${userStats.highScore} pts",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = AccentYellow
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Career Statistics",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextWhite
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    title = "Games Played",
                    value = "${userStats.completedGames}",
                    icon = Icons.Default.SportsEsports,
                    accentColor = AccentPurple,
                    modifier = Modifier.weight(1f)
                )

                StatCard(
                    title = "Total Drawings",
                    value = "${userStats.totalDrawings}",
                    icon = Icons.Default.Gesture,
                    accentColor = AccentCyan,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    title = "AI Guessed Correct",
                    value = "${userStats.totalCorrect}",
                    icon = Icons.Default.CheckCircle,
                    accentColor = SuccessGreen,
                    modifier = Modifier.weight(1f)
                )

                StatCard(
                    title = "Overall Accuracy",
                    value = "${userStats.overallAccuracy}%",
                    icon = Icons.Default.AutoAwesome,
                    accentColor = AccentCyan,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    title = "Best Winning Streak",
                    value = "${userStats.bestStreak} in a row",
                    icon = Icons.Default.LocalFireDepartment,
                    accentColor = AccentYellow,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Clear Progress Button
            OutlinedButton(
                onClick = { showResetDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(14.dp),
                border = ButtonDefaults.outlinedButtonBorder.copy(
                    brush = androidx.compose.ui.graphics.SolidColor(ErrorRed.copy(alpha = 0.5f))
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null,
                    tint = ErrorRed,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = "  Reset All Progress",
                    color = ErrorRed,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )
            }
        }
    }

    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = { Text("Reset Progress Data?", color = TextWhite, fontWeight = FontWeight.Bold) },
            text = { Text("This will permanently clear your high score, game history, and accuracy statistics.", color = TextMuted) },
            confirmButton = {
                TextButton(
                    onClick = {
                        showResetDialog = false
                        onClearHistory()
                    }
                ) {
                    Text("Clear All", color = ErrorRed, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetDialog = false }) {
                    Text("Cancel", color = AccentCyan)
                }
            },
            containerColor = NavyCard
        )
    }
}
