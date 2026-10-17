package com.realatiif.doodleai.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.realatiif.doodleai.ui.components.AppTopBar
import com.realatiif.doodleai.ui.theme.AccentCyan
import com.realatiif.doodleai.ui.theme.AccentPurple
import com.realatiif.doodleai.ui.theme.AccentYellow
import com.realatiif.doodleai.ui.theme.NavyBg
import com.realatiif.doodleai.ui.theme.NavyBorder
import com.realatiif.doodleai.ui.theme.NavyCard
import com.realatiif.doodleai.ui.theme.NavySurface
import com.realatiif.doodleai.ui.theme.SuccessGreen
import com.realatiif.doodleai.ui.theme.TextMuted
import com.realatiif.doodleai.ui.theme.TextWhite

@Composable
fun TutorialScreen(onBack: () -> Unit) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NavyBg)
    ) {
        AppTopBar(title = "How to Play", onBack = onBack)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(20.dp)
        ) {
            Text(
                text = "Welcome to Doodle AI!",
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextWhite
            )

            Text(
                text = "Test your sketching skills against a genuine deep learning model.",
                fontSize = 14.sp,
                color = AccentCyan,
                modifier = Modifier.padding(top = 4.dp, bottom = 20.dp)
            )

            // Step 1
            TutorialStepCard(
                stepNumber = "1",
                title = "Receive a Random Target",
                description = "Each round gives you a secret object to sketch (e.g., Airplane, Apple, Bicycle, Cat).",
                icon = Icons.Default.Create,
                accentColor = AccentCyan
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Step 2
            TutorialStepCard(
                stepNumber = "2",
                title = "Draw Before the Timer Expires",
                description = "Use your finger or stylus on the canvas. Adjust brush size, switch to eraser, or undo mistakes.",
                icon = Icons.Default.Timer,
                accentColor = AccentYellow
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Step 3
            TutorialStepCard(
                stepNumber = "3",
                title = "Real On-Device AI Prediction",
                description = "When you press Submit, our trained TensorFlow Lite neural network classifies your sketch instantly offline.",
                icon = Icons.Default.Psychology,
                accentColor = AccentPurple
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Step 4
            TutorialStepCard(
                stepNumber = "4",
                title = "Earn Base Points & Bonuses",
                description = "Score 100 base points for matching the target, plus up to 25 confidence bonus and 25 time bonus points!",
                icon = Icons.Default.WorkspacePremium,
                accentColor = SuccessGreen
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Tips Section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(NavyCard)
                    .border(1.dp, NavyBorder, RoundedCornerShape(18.dp))
                    .padding(18.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Lightbulb,
                            contentDescription = "Tips",
                            tint = AccentYellow,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Pro Tips for High Accuracy",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "• Keep strokes bold and clear. Don't worry about tiny fine details.\n" +
                                "• Quick, Draw! was trained on simple, iconic doodles drawn by millions of players worldwide.\n" +
                                "• Submit as soon as your sketch is recognizable to maximize your Time Bonus!",
                        fontSize = 13.sp,
                        color = TextMuted,
                        lineHeight = 20.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun TutorialStepCard(
    stepNumber: String,
    title: String,
    description: String,
    icon: ImageVector,
    accentColor: Color
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
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(accentColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stepNumber,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = accentColor
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
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    fontSize = 13.sp,
                    color = TextMuted,
                    lineHeight = 18.sp
                )
            }
        }
    }
}
