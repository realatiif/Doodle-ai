package com.realatiif.doodleai.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
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
import com.realatiif.doodleai.drawing.DrawingCanvas
import com.realatiif.doodleai.drawing.DrawingCanvasState
import com.realatiif.doodleai.drawing.rememberDrawingCanvasState
import com.realatiif.doodleai.game.model.GameState
import com.realatiif.doodleai.ui.components.BrushControlBar
import com.realatiif.doodleai.ui.components.ResultDialog
import com.realatiif.doodleai.ui.components.TimerIndicator
import com.realatiif.doodleai.ui.theme.AccentCyan
import com.realatiif.doodleai.ui.theme.AccentPurple
import com.realatiif.doodleai.ui.theme.AccentYellow
import com.realatiif.doodleai.ui.theme.ErrorRed
import com.realatiif.doodleai.ui.theme.NavyBg
import com.realatiif.doodleai.ui.theme.NavyBorder
import com.realatiif.doodleai.ui.theme.NavyCard
import com.realatiif.doodleai.ui.theme.NavySurface
import com.realatiif.doodleai.ui.theme.TextMuted
import com.realatiif.doodleai.ui.theme.TextWhite

@Composable
fun GameScreen(
    gameState: GameState,
    onSubmitDrawing: (DrawingCanvasState) -> Unit,
    onNextRound: (DrawingCanvasState) -> Unit,
    onQuitGame: () -> Unit,
    onDismissError: () -> Unit,
    canvasState: DrawingCanvasState = rememberDrawingCanvasState()
) {
    var showQuitDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NavyBg)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top Game Bar: Round, Score, Streak, and Quit Button
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                onClick = { showQuitDialog = true },
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Quit",
                    tint = TextMuted
                )
            }

            // Round Tracker Badge
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(NavyCard)
                    .border(1.dp, NavyBorder, RoundedCornerShape(12.dp))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "Round ${gameState.currentRound} / ${gameState.totalRounds}",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = AccentCyan
                )
            }

            // Streak Indicator
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(NavyCard)
                    .border(1.dp, NavyBorder, RoundedCornerShape(12.dp))
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.LocalFireDepartment,
                    contentDescription = "Streak",
                    tint = if (gameState.currentStreak > 0) AccentYellow else TextMuted,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${gameState.currentStreak}",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (gameState.currentStreak > 0) AccentYellow else TextMuted
                )
            }

            // Score Badge
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(AccentPurple.copy(alpha = 0.15f))
                    .border(1.dp, AccentPurple.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "${gameState.totalScore} pts",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = AccentPurple
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Target Challenge Header Banner
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(NavyCard)
                .border(1.5.dp, AccentCyan.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                .padding(vertical = 10.dp, horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "DRAW THIS OBJECT",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp,
                    color = TextMuted
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = gameState.displayChallenge,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = AccentCyan
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Countdown Timer Indicator
        TimerIndicator(
            remainingSeconds = gameState.timerRemainingSeconds,
            totalSeconds = gameState.totalRoundSeconds
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Interactive Drawing Canvas (Flexible responsive size)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            DrawingCanvas(
                state = canvasState,
                modifier = Modifier.fillMaxSize()
            )

            // Canvas empty or active helper hints
            if (canvasState.isEmpty) {
                Text(
                    text = "Draw ${gameState.displayChallenge} here...",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Normal,
                    color = TextMuted.copy(alpha = 0.5f),
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Brush & Tool Controls
        BrushControlBar(
            canvasState = canvasState,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Error message banner
        if (gameState.errorMessage != null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(ErrorRed.copy(alpha = 0.15f))
                    .border(1.dp, ErrorRed.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = gameState.errorMessage,
                    color = ErrorRed,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
        }

        // Submit Button
        Button(
            onClick = { onSubmitDrawing(canvasState) },
            enabled = !gameState.isInferenceLoading && !gameState.isRoundSubmitted,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = AccentCyan,
                disabledContainerColor = NavyCard
            )
        ) {
            if (gameState.isInferenceLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = NavyBg,
                    strokeWidth = 2.5.dp
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "AI IS ANALYZING...",
                    color = NavyBg,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            } else {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = null,
                        tint = if (!gameState.isRoundSubmitted) NavyBg else TextMuted,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "SUBMIT DRAWING",
                        color = if (!gameState.isRoundSubmitted) NavyBg else TextMuted,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }

    // AI Prediction Result Dialog
    if (gameState.isRoundSubmitted && gameState.lastPredictionResult != null) {
        ResultDialog(
            result = gameState.lastPredictionResult,
            targetChallenge = gameState.displayChallenge,
            isLastRound = gameState.isLastRound,
            onNextClicked = { onNextRound(canvasState) }
        )
    }

    // Quit Game Confirmation Dialog
    if (showQuitDialog) {
        AlertDialog(
            onDismissRequest = { showQuitDialog = false },
            title = { Text("Quit Game Session?", color = TextWhite, fontWeight = FontWeight.Bold) },
            text = { Text("Your current session progress will be lost.", color = TextMuted) },
            confirmButton = {
                TextButton(
                    onClick = {
                        showQuitDialog = false
                        onQuitGame()
                    }
                ) {
                    Text("Quit", color = ErrorRed, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showQuitDialog = false }) {
                    Text("Keep Playing", color = AccentCyan)
                }
            },
            containerColor = NavyCard
        )
    }
}
