package com.realatiif.doodleai.drawing

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp

/**
 * Interactive touch and stylus drawing canvas.
 * Renders smooth quadratic bezier curves with sub-pixel precision.
 */
@Composable
fun DrawingCanvas(
    state: DrawingCanvasState,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color.White
) {
    Box(
        modifier = modifier
            .shadow(elevation = 8.dp, shape = RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor)
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { offset ->
                        state.startStroke(offset)
                    },
                    onDrag = { change, _ ->
                        change.consume()
                        state.addPoint(change.position)
                    },
                    onDragEnd = {
                        state.endStroke()
                    },
                    onDragCancel = {
                        state.endStroke()
                    }
                )
            }
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val allStrokes = if (state.currentStroke != null) {
                state.strokes + state.currentStroke!!
            } else {
                state.strokes
            }

            for (stroke in allStrokes) {
                val strokeColor = if (stroke.isEraser) backgroundColor else stroke.color

                if (stroke.points.size == 1) {
                    val pt = stroke.points[0]
                    drawCircle(
                        color = strokeColor,
                        radius = stroke.strokeWidth / 2f,
                        center = pt
                    )
                } else if (stroke.points.size > 1) {
                    val path = Path()
                    val pts = stroke.points
                    path.moveTo(pts[0].x, pts[0].y)

                    for (i in 1 until pts.size) {
                        val prev = pts[i - 1]
                        val curr = pts[i]
                        val midX = (prev.x + curr.x) / 2f
                        val midY = (prev.y + curr.y) / 2f
                        path.quadraticTo(prev.x, prev.y, midX, midY)
                    }
                    path.lineTo(pts.last().x, pts.last().y)

                    drawPath(
                        path = path,
                        color = strokeColor,
                        style = Stroke(
                            width = stroke.strokeWidth,
                            cap = StrokeCap.Round,
                            join = StrokeJoin.Round
                        )
                    )
                }
            }
        }
    }
}
