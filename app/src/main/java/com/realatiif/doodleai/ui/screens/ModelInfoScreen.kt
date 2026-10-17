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
import androidx.compose.material.icons.filled.DataObject
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.realatiif.doodleai.ml.ModelInfo
import com.realatiif.doodleai.ui.components.AppTopBar
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
fun ModelInfoScreen(
    supportedLabels: List<String>,
    onBack: () -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NavyBg)
    ) {
        AppTopBar(title = "AI Model Architecture", onBack = onBack)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(20.dp)
        ) {
            // Neural Engine Hero
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(NavyCard)
                    .border(1.5.dp, AccentPurple.copy(alpha = 0.6f), RoundedCornerShape(20.dp))
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(AccentPurple.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Memory,
                            contentDescription = "AI Chip",
                            tint = AccentPurple,
                            modifier = Modifier.size(30.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {
                        Text(
                            text = "Quick, Draw! CNN",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                        Text(
                            text = "TensorFlow Lite On-Device Classifier",
                            fontSize = 12.sp,
                            color = AccentCyan
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Technical Specifications",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextWhite
            )

            Spacer(modifier = Modifier.height(12.dp))

            SpecItem(label = "Model File", value = ModelInfo.MODEL_FILENAME)
            SpecItem(label = "Architecture", value = ModelInfo.MODEL_ARCHITECTURE)
            SpecItem(label = "Input Shape", value = "[1, 28, 28, 1] (Float32)")
            SpecItem(label = "Output Shape", value = "[1, 100] (Softmax Probabilities)")
            SpecItem(label = "Pre-processing", value = "Centering + Aspect Ratio Padding + Polarity Inversion (0.0 to 1.0)")
            SpecItem(label = "Dataset", value = ModelInfo.DATASET_SOURCE)
            SpecItem(label = "Engine", value = ModelInfo.INFERENCE_ENGINE)
            SpecItem(label = "Trained Classes", value = "${supportedLabels.size} Categories")

            Spacer(modifier = Modifier.height(24.dp))

            // Offline Privacy Guarantee
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(SuccessGreen.copy(alpha = 0.1f))
                    .border(1.dp, SuccessGreen.copy(alpha = 0.4f), RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Row(verticalAlignment = Alignment.Top) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Privacy",
                        tint = SuccessGreen,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "100% On-Device & Private",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = SuccessGreen
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = ModelInfo.PRIVACY_STATEMENT,
                            fontSize = 12.sp,
                            color = TextWhite,
                            lineHeight = 18.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Supported Categories Preview
            Text(
                text = "Supported Drawing Categories (${supportedLabels.size})",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextWhite
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = supportedLabels.joinToString(", ") { it.replace('_', ' ') },
                fontSize = 12.sp,
                color = TextMuted,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun SpecItem(label: String, value: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(NavyCard)
            .border(1.dp, NavyBorder, RoundedCornerShape(12.dp))
            .padding(horizontal = 14.dp, vertical = 10.dp)
    ) {
        Column {
            Text(text = label, fontSize = 11.sp, color = TextMuted, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = value, fontSize = 13.sp, color = TextWhite, fontWeight = FontWeight.Medium)
        }
    }
}
