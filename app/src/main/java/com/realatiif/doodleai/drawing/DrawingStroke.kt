package com.realatiif.doodleai.drawing

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color

/**
 * Represents a drawing stroke consisting of ordered points, stroke width, and eraser mode.
 */
data class DrawingStroke(
    val points: List<Offset>,
    val strokeWidth: Float,
    val isEraser: Boolean = false,
    val color: Color = Color.Black
)
