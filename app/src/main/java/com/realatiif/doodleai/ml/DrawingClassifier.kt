package com.realatiif.doodleai.ml

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.realatiif.doodleai.game.logic.ScoringEngine
import com.realatiif.doodleai.game.model.PredictionResult
import com.realatiif.doodleai.game.model.RankedPrediction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.ByteBuffer
import java.nio.channels.FileChannel

/**
 * On-device drawing classifier using TensorFlow Lite and the Quick, Draw! CNN model.
 * Executes all inference off the main thread.
 */
class DrawingClassifier(context: Context) : AutoCloseable {

    companion object {
        private const val TAG = "DrawingClassifier"
        private const val TOP_K_CANDIDATES = 5
    }

    private val interpreter: Interpreter
    val labels: List<String>

    init {
        // Load model buffer from assets
        val modelBuffer = loadModelFile(context, ModelInfo.MODEL_FILENAME)
        val options = Interpreter.Options().apply {
            setNumThreads(4)
            setUseNNAPI(false) // Safe portable CPU execution
        }
        interpreter = Interpreter(modelBuffer, options)

        // Load labels
        labels = loadLabels(context, ModelInfo.LABELS_FILENAME)
        Log.i(TAG, "TFLite model loaded successfully with ${labels.size} classes.")
    }

    private fun loadModelFile(context: Context, filename: String): ByteBuffer {
        val assetFileDescriptor = context.assets.openFd(filename)
        val inputStream = FileInputStream(assetFileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = assetFileDescriptor.startOffset
        val declaredLength = assetFileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }

    private fun loadLabels(context: Context, filename: String): List<String> {
        return context.assets.open(filename).bufferedReader().useLines { lines ->
            lines.map { it.trim() }.filter { it.isNotEmpty() }.toList()
        }
    }

    /**
     * Runs drawing classification asynchronously off the main thread.
     */
    suspend fun classifyDrawing(
        bitmap: Bitmap,
        targetChallenge: String,
        remainingSeconds: Int,
        totalRoundSeconds: Int
    ): PredictionResult = withContext(Dispatchers.Default) {
        // Preprocess user drawing
        val inputBuffer = DrawingPreprocessor.preprocessToByteBuffer(bitmap)

        // Output buffer matches number of classes in labels
        val outputBuffer = Array(1) { FloatArray(labels.size) }

        // Run TFLite inference
        interpreter.run(inputBuffer, outputBuffer)

        val probabilities = outputBuffer[0]

        // Rank top predictions by confidence
        val sortedIndices = probabilities.indices
            .sortedByDescending { probabilities[it] }

        val topIndex = sortedIndices.firstOrNull() ?: 0
        val topLabel = labels.getOrElse(topIndex) { "unknown" }
        val topConfidence = probabilities.getOrElse(topIndex) { 0f }

        val rankedList = sortedIndices.take(TOP_K_CANDIDATES).mapIndexed { rank, idx ->
            RankedPrediction(
                label = labels.getOrElse(idx) { "unknown" },
                confidence = probabilities.getOrElse(idx) { 0f },
                rank = rank + 1
            )
        }

        // Calculate score breakdown
        val score = ScoringEngine.calculateScore(
            targetChallenge = targetChallenge,
            predictedLabel = topLabel,
            confidence = topConfidence,
            remainingSeconds = remainingSeconds,
            totalRoundSeconds = totalRoundSeconds
        )

        Log.d(TAG, "Prediction: $topLabel (${(topConfidence * 100).toInt()}%), Target: $targetChallenge, Correct: ${score.isCorrect}")

        PredictionResult(
            topPrediction = topLabel,
            topConfidence = topConfidence,
            rankedPredictions = rankedList,
            isCorrect = score.isCorrect,
            basePoints = score.basePoints,
            confidenceBonus = score.confidenceBonus,
            timeBonus = score.timeBonus,
            totalPointsEarned = score.totalPoints
        )
    }

    override fun close() {
        try {
            interpreter.close()
        } catch (e: Exception) {
            Log.e(TAG, "Error closing interpreter: ${e.message}")
        }
    }
}
