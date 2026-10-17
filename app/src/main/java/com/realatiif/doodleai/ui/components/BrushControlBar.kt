package com.realatiif.doodleai.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.material.icons.filled.AutoFixNormal
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.realatiif.doodleai.drawing.DrawingCanvasState
import com.realatiif.doodleai.ui.theme.AccentCyan
import com.realatiif.doodleai.ui.theme.ErrorRed
import com.realatiif.doodleai.ui.theme.NavyBorder
import com.realatiif.doodleai.ui.theme.NavyCard
import com.realatiif.doodleai.ui.theme.NavySurface
import com.realatiif.doodleai.ui.theme.TextMuted
import com.realatiif.doodleai.ui.theme.TextWhite

@Composable
fun BrushControlBar(
    canvasState: DrawingCanvasState,
    modifier: Modifier = Modifier,
    onClearClicked: () -> Unit = { canvasState.clear() },
    onUndoClicked: () -> Unit = { canvasState.undo() }
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(NavyCard)
            .border(1.dp, NavyBorder, RoundedCornerShape(20.dp))
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Pen Mode Toggle
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(if (!canvasState.isEraser) AccentCyan.copy(alpha = 0.2f) else NavySurface)
                .border(
                    width = if (!canvasState.isEraser) 2.dp else 1.dp,
                    color = if (!canvasState.isEraser) AccentCyan else NavyBorder,
                    shape = CircleShape
                )
                .clickable { canvasState.isEraser = false },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Pen",
                tint = if (!canvasState.isEraser) AccentCyan else TextMuted,
                modifier = Modifier.size(22.dp)
            )
        }

        // Eraser Mode Toggle
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(if (canvasState.isEraser) AccentCyan.copy(alpha = 0.2f) else NavySurface)
                .border(
                    width = if (canvasState.isEraser) 2.dp else 1.dp,
                    color = if (canvasState.isEraser) AccentCyan else NavyBorder,
                    shape = CircleShape
                )
                .clickable { canvasState.isEraser = true },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.AutoFixNormal,
                contentDescription = "Eraser",
                tint = if (canvasState.isEraser) AccentCyan else TextMuted,
                modifier = Modifier.size(22.dp)
            )
        }

        // Divider
        Box(
            modifier = Modifier
                .size(width = 1.dp, height = 24.dp)
                .background(NavyBorder)
        )

        // Brush Sizes: Small (6f), Medium (14f), Large (24f)
        val sizes = listOf(6f to 8.dp, 14f to 14.dp, 24f to 20.dp)
        sizes.forEach { (width, dotDp) ->
            val isSelected = canvasState.strokeWidth == width
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(if (isSelected) AccentCyan.copy(alpha = 0.2f) else Color.Transparent)
                    .clickable { canvasState.strokeWidth = width },
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(dotDp)
                        .clip(CircleShape)
                        .background(if (isSelected) AccentCyan else TextMuted)
                )
            }
        }

        // Divider
        Box(
            modifier = Modifier
                .size(width = 1.dp, height = 24.dp)
                .background(NavyBorder)
        )

        // Undo Button
        IconButton(
            onClick = onUndoClicked,
            enabled = canvasState.strokes.isNotEmpty(),
            modifier = Modifier.size(44.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Undo,
                contentDescription = "Undo",
                tint = if (canvasState.strokes.isNotEmpty()) TextWhite else TextMuted.copy(alpha = 0.4f),
                modifier = Modifier.size(22.dp)
            )
        }

        // Clear Canvas Button
        IconButton(
            onClick = onClearClicked,
            enabled = !canvasState.isEmpty,
            modifier = Modifier.size(44.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Clear",
                tint = if (!canvasState.isEmpty) ErrorRed else TextMuted.copy(alpha = 0.4f),
                modifier = Modifier.size(22.dp)
            )
        }
    }
}
