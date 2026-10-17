package com.realatiif.doodleai.ml

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.math.max

/**
 * Preprocesses Android user drawings into the exact tensor format expected
 * by the Quick, Draw! CNN model.
 *
 * Requirements satisfied:
 * 1. Centering the drawing.
 * 2. Aspect-ratio preservation (square padding).
 * 3. Downsampling to 28x28 grayscale.
 * 4. Background polarity inversion (white drawing stroke on black background).
 * 5. Pixel normalization into [0.0f, 1.0f].
 * 6. Empty drawing detection.
 */
class EmptyDrawingException(message: String = "The drawing canvas is empty. Please draw something before submitting!") :
    IllegalArgumentException(message)

object DrawingPreprocessor {

    const val TARGET_SIZE = 28
    private const val MIN_STROKE_PIXELS = 25
    private const val PADDING_RATIO = 0.15f // 15% padding around bounding box

    /**
     * Checks whether a bitmap has enough drawing content.
     */
    fun isDrawingEmpty(bitmap: Bitmap, threshold: Int = MIN_STROKE_PIXELS): Boolean {
        var strokePixelCount = 0
        val width = bitmap.width
        val height = bitmap.height
        val pixels = IntArray(width * height)
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height)

        for (pixel in pixels) {
            val r = (pixel shr 16) and 0xFF
            val g = (pixel shr 8) and 0xFF
            val b = pixel and 0xFF
            // Check if pixel is significantly darker than pure white (#FFFFFF)
            val brightness = (r + g + b) / 3
            if (brightness < 240) {
                strokePixelCount++
                if (strokePixelCount >= threshold) return false
            }
        }
        return true
    }

    /**
     * Finds the minimum bounding box enclosing all non-background pixels.
     * Returns null if no drawing content is found.
     */
    fun findBoundingBox(bitmap: Bitmap): Rect? {
        val width = bitmap.width
        val height = bitmap.height
        val pixels = IntArray(width * height)
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height)

        var minX = width
        var maxX = 0
        var minY = height
        var maxY = 0
        var foundAny = false

        for (y in 0 until height) {
            for (x in 0 until width) {
                val pixel = pixels[y * width + x]
                val r = (pixel shr 16) and 0xFF
                val g = (pixel shr 8) and 0xFF
                val b = pixel and 0xFF
                val brightness = (r + g + b) / 3

                if (brightness < 240) {
                    foundAny = true
                    if (x < minX) minX = x
                    if (x > maxX) maxX = x
                    if (y < minY) minY = y
                    if (y > maxY) maxY = y
                }
            }
        }

        if (!foundAny || maxX < minX || maxY < minY) {
            return null
        }

        return Rect(minX, minY, maxX + 1, maxY + 1)
    }

    /**
     * Preprocesses the input bitmap:
     * 1. Validates non-empty drawing.
     * 2. Crops to bounding box with padding.
     * 3. Centers in a square bitmap to preserve aspect ratio.
     * 4. Resizes to 28x28.
     * 5. Converts to ByteBuffer with polarity inversion (stroke=1.0, bg=0.0).
     */
    fun preprocessToByteBuffer(bitmap: Bitmap): ByteBuffer {
        val bbox = findBoundingBox(bitmap) ?: throw EmptyDrawingException()

        val boxWidth = bbox.width()
        val boxHeight = bbox.height()

        if (boxWidth <= 2 || boxHeight <= 2) {
            throw EmptyDrawingException("Drawing is too small or faint to recognize.")
        }

        // Add padding
        val pad = (max(boxWidth, boxHeight) * PADDING_RATIO).toInt().coerceAtLeast(4)
        val maxDim = max(boxWidth, boxHeight) + (pad * 2)

        // Create square white bitmap to preserve aspect ratio
        val squareBitmap = Bitmap.createBitmap(maxDim, maxDim, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(squareBitmap)
        canvas.drawColor(Color.WHITE)

        // Draw cropped bounding box centered onto the square canvas
        val srcRect = bbox
        val offsetX = (maxDim - boxWidth) / 2
        val offsetY = (maxDim - boxHeight) / 2
        val dstRect = Rect(offsetX, offsetY, offsetX + boxWidth, offsetY + boxHeight)

        val paint = Paint(Paint.FILTER_BITMAP_FLAG)
        canvas.drawBitmap(bitmap, srcRect, dstRect, paint)

        // Downsample to 28x28 with bilinear filtering
        val scaled = Bitmap.createScaledBitmap(squareBitmap, TARGET_SIZE, TARGET_SIZE, true)

        // Allocate direct ByteBuffer: 1 * 28 * 28 * 1 * 4 bytes
        val buffer = ByteBuffer.allocateDirect(4 * TARGET_SIZE * TARGET_SIZE).apply {
            order(ByteOrder.nativeOrder())
        }

        val pixels = IntArray(TARGET_SIZE * TARGET_SIZE)
        scaled.getPixels(pixels, 0, TARGET_SIZE, 0, 0, TARGET_SIZE, TARGET_SIZE)

        for (pixel in pixels) {
            val r = (pixel shr 16) and 0xFF
            val g = (pixel shr 8) and 0xFF
            val b = pixel and 0xFF
            val brightness = (r + g + b) / (3f * 255f)

            // Polarity inversion: Quick Draw model expects background=0.0, stroke=1.0
            val normalizedStroke = (1f - brightness).coerceIn(0f, 1f)
            buffer.putFloat(normalizedStroke)
        }

        buffer.rewind()

        // Clean up intermediate bitmaps to avoid memory pressure
        if (squareBitmap != scaled) squareBitmap.recycle()
        scaled.recycle()

        return buffer
    }

    /**
     * Helper for testing/debugging: returns 28x28 float array.
     */
    fun preprocessToFloatArray(bitmap: Bitmap): FloatArray {
        val buffer = preprocessToByteBuffer(bitmap)
        val result = FloatArray(TARGET_SIZE * TARGET_SIZE)
        buffer.asFloatBuffer().get(result)
        return result
    }
}
