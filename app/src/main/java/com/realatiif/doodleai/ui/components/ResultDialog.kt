package com.realatiif.doodleai.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.realatiif.doodleai.game.model.PredictionResult
import com.realatiif.doodleai.ui.theme.AccentCyan
import com.realatiif.doodleai.ui.theme.AccentPurple
import com.realatiif.doodleai.ui.theme.AccentYellow
import com.realatiif.doodleai.ui.theme.ErrorRed
import com.realatiif.doodleai.ui.theme.NavyBg
import com.realatiif.doodleai.ui.theme.NavyBorder
import com.realatiif.doodleai.ui.theme.NavyCard
import com.realatiif.doodleai.ui.theme.NavySurface
import com.realatiif.doodleai.ui.theme.SuccessGreen
import com.realatiif.doodleai.ui.theme.TextMuted
import com.realatiif.doodleai.ui.theme.TextWhite

@Composable
fun ResultDialog(
    result: PredictionResult,
    targetChallenge: String,
    isLastRound: Boolean,
    onNextClicked: () -> Unit
) {
    Dialog(onDismissRequest = { /* Modal: require button click to proceed */ }) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = NavyCard),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.5.dp, if (result.isCorrect) SuccessGreen else ErrorRed.copy(alpha = 0.6f), RoundedCornerShape(24.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Success / Failure Icon Header
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(if (result.isCorrect) SuccessGreen.copy(alpha = 0.15f) else ErrorRed.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (result.isCorrect) Icons.Default.Check else Icons.Default.Close,
                        contentDescription = if (result.isCorrect) "Correct" else "Incorrect",
                        tint = if (result.isCorrect) SuccessGreen else ErrorRed,
                        modifier = Modifier.size(36.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = if (result.isCorrect) "AI Guessed Correctly!" else "AI Couldn't Match It",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = if (result.isCorrect) SuccessGreen else ErrorRed
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Target vs Prediction Box
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(NavySurface)
                        .border(1.dp, NavyBorder, RoundedCornerShape(16.dp))
                        .padding(16.dp)
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Target Word:", color = TextMuted, fontSize = 13.sp)
                            Text(
                                targetChallenge.replaceFirstChar { it.uppercase() },
                                color = TextWhite,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("AI Top Prediction:", color = TextMuted, fontSize = 13.sp)
                            Text(
                                result.displayTopPrediction,
                                color = if (result.isCorrect) SuccessGreen else AccentYellow,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Confidence:", color = TextMuted, fontSize = 13.sp)
                            Text(
                                result.formattedTopConfidence,
                                color = AccentCyan,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }
                }

                // Points Breakdown (if correct)
                if (result.isCorrect) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(SuccessGreen.copy(alpha = 0.1f))
                            .border(1.dp, SuccessGreen.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                            .padding(12.dp)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Base Points", color = TextWhite, fontSize = 12.sp)
                                Text("+${result.basePoints}", color = SuccessGreen, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Confidence Bonus", color = TextWhite, fontSize = 12.sp)
                                Text("+${result.confidenceBonus}", color = AccentCyan, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Time Bonus", color = TextWhite, fontSize = 12.sp)
                                Text("+${result.timeBonus}", color = AccentYellow, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Total Round Earned", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Text("+${result.totalPointsEarned} pts", color = SuccessGreen, fontWeight = FontWeight.ExtraBold, fontSize = 14.sp)
                            }
                        }
                    }
                }

                // Ranked Alternative Guesses
                if (result.rankedPredictions.size > 1) {
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = "AI Ranked Predictions",
                        fontSize = 12.sp,
                        color = TextMuted,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.align(Alignment.Start)
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    result.rankedPredictions.take(3).forEach { ranked ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "#${ranked.rank}",
                                color = TextMuted,
                                fontSize = 11.sp,
                                modifier = Modifier.width(24.dp)
                            )
                            Text(
                                text = ranked.displayLabel,
                                color = TextWhite,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = ranked.formattedConfidence,
                                color = AccentCyan,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Next Button
                Button(
                    onClick = onNextClicked,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AccentCyan)
                ) {
                    Text(
                        text = if (isLastRound) "View Final Score" else "Next Challenge",
                        color = NavyBg,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
