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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.Scoreboard
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.realatiif.doodleai.game.model.GameState
import com.realatiif.doodleai.ui.components.StatCard
import com.realatiif.doodleai.ui.theme.AccentCyan
import com.realatiif.doodleai.ui.theme.AccentPurple
import com.realatiif.doodleai.ui.theme.AccentYellow
import com.realatiif.doodleai.ui.theme.NavyBg
import com.realatiif.doodleai.ui.theme.NavyBorder
import com.realatiif.doodleai.ui.theme.NavyCard
import com.realatiif.doodleai.ui.theme.SuccessGreen
import com.realatiif.doodleai.ui.theme.TextMuted
import com.realatiif.doodleai.ui.theme.TextWhite

@Composable
fun GameOverScreen(
    gameState: GameState,
    onRestartClicked: () -> Unit,
    onHomeClicked: () -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NavyBg)
            .verticalScroll(scrollState)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Trophy Icon
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(AccentYellow.copy(alpha = 0.15f))
                .border(2.dp, AccentYellow, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.WorkspacePremium,
                contentDescription = "Trophy",
                tint = AccentYellow,
                modifier = Modifier.size(44.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Game Complete!",
            fontSize = 28.sp,
            fontWeight = FontWeight.ExtraBold,
            color = TextWhite
        )

        Text(
            text = "${gameState.difficulty.displayName} Mode • ${gameState.totalRounds} Rounds",
            fontSize = 14.sp,
            color = TextMuted,
            modifier = Modifier.padding(top = 4.dp, bottom = 24.dp)
        )

        // Final Score Hero Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(NavyCard)
                .border(1.5.dp, AccentCyan.copy(alpha = 0.6f), RoundedCornerShape(20.dp))
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "FINAL SCORE",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextMuted,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "${gameState.totalScore}",
                    fontSize = 44.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = AccentCyan
                )
                Text(
                    text = "points earned",
                    fontSize = 13.sp,
                    color = TextMuted
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Session Performance Stats Grid
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatCard(
                title = "Accuracy",
                value = "${gameState.accuracyPercentage}%",
                icon = Icons.Default.CheckCircle,
                accentColor = SuccessGreen,
                modifier = Modifier.weight(1f)
            )

            StatCard(
                title = "Best Streak",
                value = "${gameState.bestStreak}",
                icon = Icons.Default.LocalFireDepartment,
                accentColor = AccentYellow,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatCard(
                title = "Correct Guesses",
                value = "${gameState.correctGuesses} / ${gameState.totalRounds}",
                icon = Icons.Default.Scoreboard,
                accentColor = AccentPurple,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Play Again Button
        Button(
            onClick = onRestartClicked,
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = AccentCyan)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Replay,
                    contentDescription = null,
                    tint = NavyBg,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "PLAY AGAIN",
                    color = NavyBg,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Return Home Button
        OutlinedButton(
            onClick = onHomeClicked,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(16.dp),
            border = ButtonDefaults.outlinedButtonBorder.copy(brush = androidx.compose.ui.graphics.SolidColor(NavyBorder))
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = null,
                    tint = TextWhite,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Return to Home",
                    color = TextWhite,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
