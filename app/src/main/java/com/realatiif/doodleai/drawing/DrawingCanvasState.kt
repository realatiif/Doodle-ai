package com.realatiif.doodleai.drawing

import android.graphics.Bitmap
import android.graphics.Canvas as AndroidCanvas
import android.graphics.Color as AndroidColor
import android.graphics.Paint as AndroidPaint
import android.graphics.Path as AndroidPath
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color

class DrawingCanvasState {
    val strokes = mutableStateListOf<DrawingStroke>()
    var currentStroke by mutableStateOf<DrawingStroke?>(null)
    var strokeWidth by mutableFloatStateOf(12f)
    var isEraser by mutableStateOf(false)

    val isEmpty: Boolean
        get() = strokes.isEmpty() && currentStroke == null

    fun startStroke(offset: Offset) {
        currentStroke = DrawingStroke(
            points = listOf(offset),
            strokeWidth = strokeWidth,
            isEraser = isEraser,
            color = if (isEraser) Color.White else Color.Black
        )
    }

    fun addPoint(offset: Offset) {
        val current = currentStroke ?: return
        // Avoid duplicate adjacent points to optimize performance
        val lastPoint = current.points.lastOrNull()
        if (lastPoint != null) {
            val dx = offset.x - lastPoint.x
            val dy = offset.y - lastPoint.y
            if (dx * dx + dy * dy < 4f) return
        }
        currentStroke = current.copy(points = current.points + offset)
    }

    fun endStroke() {
        currentStroke?.let {
            if (it.points.isNotEmpty()) {
                strokes.add(it)
            }
        }
        currentStroke = null
    }

    fun undo() {
        if (strokes.isNotEmpty()) {
            strokes.removeAt(strokes.lastIndex)
        }
    }

    fun clear() {
        strokes.clear()
        currentStroke = null
    }

    /**
     * Renders all strokes onto an offscreen Bitmap suitable for ML model input.
     * Uses White canvas background and Black ink strokes.
     */
    fun exportToBitmap(canvasWidth: Float, canvasHeight: Float, exportSize: Int = 512): Bitmap {
        val targetWidth = if (canvasWidth > 0) canvasWidth.toInt() else exportSize
        val targetHeight = if (canvasHeight > 0) canvasHeight.toInt() else exportSize

        val bitmap = Bitmap.createBitmap(targetWidth, targetHeight, Bitmap.Config.ARGB_8888)
        val canvas = AndroidCanvas(bitmap)
        canvas.drawColor(AndroidColor.WHITE)

        val penPaint = AndroidPaint().apply {
            color = AndroidColor.BLACK
            style = AndroidPaint.Style.STROKE
            strokeCap = AndroidPaint.Cap.ROUND
            strokeJoin = AndroidPaint.Join.ROUND
            isAntiAlias = true
        }

        val eraserPaint = AndroidPaint().apply {
            color = AndroidColor.WHITE
            style = AndroidPaint.Style.STROKE
            strokeCap = AndroidPaint.Cap.ROUND
            strokeJoin = AndroidPaint.Join.ROUND
            isAntiAlias = true
        }

        val allStrokes = if (currentStroke != null) strokes + currentStroke!! else strokes

        for (stroke in allStrokes) {
            val paint = if (stroke.isEraser) eraserPaint else penPaint
            paint.strokeWidth = stroke.strokeWidth

            if (stroke.points.size == 1) {
                val pt = stroke.points[0]
                canvas.drawCircle(pt.x, pt.y, stroke.strokeWidth / 2, paint)
            } else if (stroke.points.size > 1) {
                val path = AndroidPath()
                val pts = stroke.points
                path.moveTo(pts[0].x, pts[0].y)

                for (i in 1 until pts.size) {
                    val prev = pts[i - 1]
                    val curr = pts[i]
                    // Smooth quad bezier between midpoints
                    val midX = (prev.x + curr.x) / 2f
                    val midY = (prev.y + curr.y) / 2f
                    path.quadTo(prev.x, prev.y, midX, midY)
                }
                val last = pts.last()
                path.lineTo(last.x, last.y)
                canvas.drawPath(path, paint)
            }
        }

        return bitmap
    }
}

@Composable
fun rememberDrawingCanvasState(): DrawingCanvasState {
    return remember { DrawingCanvasState() }
}
