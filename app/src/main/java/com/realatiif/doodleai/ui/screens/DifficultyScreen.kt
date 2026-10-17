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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Speed
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
import com.realatiif.doodleai.game.model.Difficulty
import com.realatiif.doodleai.ui.components.AppTopBar
import com.realatiif.doodleai.ui.theme.AccentCyan
import com.realatiif.doodleai.ui.theme.AccentPurple
import com.realatiif.doodleai.ui.theme.AccentYellow
import com.realatiif.doodleai.ui.theme.ErrorRed
import com.realatiif.doodleai.ui.theme.NavyBg
import com.realatiif.doodleai.ui.theme.NavyBorder
import com.realatiif.doodleai.ui.theme.NavyCard
import com.realatiif.doodleai.ui.theme.TextMuted
import com.realatiif.doodleai.ui.theme.TextWhite

@Composable
fun DifficultyScreen(
    onDifficultySelected: (Difficulty) -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NavyBg)
    ) {
        AppTopBar(title = "Select Difficulty", onBack = onBack)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Choose Your Challenge",
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextWhite
            )

            Text(
                text = "Timer speed and total rounds vary by difficulty",
                fontSize = 14.sp,
                color = TextMuted,
                modifier = Modifier.padding(top = 4.dp, bottom = 32.dp)
            )

            DifficultyCard(
                difficulty = Difficulty.EASY,
                icon = Icons.Default.Speed,
                accentColor = AccentCyan,
                badgeText = "Recommended for Beginners",
                onClick = { onDifficultySelected(Difficulty.EASY) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            DifficultyCard(
                difficulty = Difficulty.MEDIUM,
                icon = Icons.Default.Bolt,
                accentColor = AccentYellow,
                badgeText = "Balanced Gameplay",
                onClick = { onDifficultySelected(Difficulty.MEDIUM) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            DifficultyCard(
                difficulty = Difficulty.HARD,
                icon = Icons.Default.LocalFireDepartment,
                accentColor = ErrorRed,
                badgeText = "Quick Sketch Masters",
                onClick = { onDifficultySelected(Difficulty.HARD) }
            )
        }
    }
}

@Composable
private fun DifficultyCard(
    difficulty: Difficulty,
    icon: ImageVector,
    accentColor: Color,
    badgeText: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(NavyCard)
            .border(1.5.dp, NavyBorder, RoundedCornerShape(20.dp))
            .clickable(onClick = onClick)
            .padding(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(accentColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = difficulty.displayName,
                    tint = accentColor,
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = difficulty.displayName,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )
                    Text(
                        text = "${difficulty.secondsPerRound}s / round",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = accentColor
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "${difficulty.totalRounds} Challenges • $badgeText",
                    fontSize = 12.sp,
                    color = TextMuted
                )
            }
        }
    }
}
