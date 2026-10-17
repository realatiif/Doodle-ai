package com.realatiif.doodleai.game.logic

import kotlin.math.roundToInt

/**
 * Pure scoring engine for Doodle AI.
 * Implements the strictly specified scoring rules:
 * - Correct top-1 prediction: 100 base points.
 * - Confidence bonus: 0-25 points based on model confidence.
 * - Time bonus: 0-25 points based on remaining time / total time.
 * - Incorrect top-1 prediction: 0 points total.
 * - No duplicate score awards.
 */
object ScoringEngine {

    const val BASE_CORRECT_POINTS = 100
    const val MAX_CONFIDENCE_BONUS = 25
    const val MAX_TIME_BONUS = 25

    /**
     * Calculates the breakdown and total score for a round.
     */
    fun calculateScore(
        targetChallenge: String,
        predictedLabel: String,
        confidence: Float,
        remainingSeconds: Int,
        totalRoundSeconds: Int
    ): ScoreBreakdown {
        val isCorrect = isLabelMatch(targetChallenge, predictedLabel)

        if (!isCorrect) {
            return ScoreBreakdown(
                isCorrect = false,
                basePoints = 0,
                confidenceBonus = 0,
                timeBonus = 0,
                totalPoints = 0
            )
        }

        // Base points
        val basePoints = BASE_CORRECT_POINTS

        // Confidence bonus: 0 to 25
        val clampedConfidence = confidence.coerceIn(0f, 1f)
        val confidenceBonus = (clampedConfidence * MAX_CONFIDENCE_BONUS).roundToInt().coerceIn(0, MAX_CONFIDENCE_BONUS)

        // Time bonus: 0 to 25 based on remaining time ratio
        val timeRatio = if (totalRoundSeconds > 0) {
            (remainingSeconds.toFloat() / totalRoundSeconds.toFloat()).coerceIn(0f, 1f)
        } else {
            0f
        }
        val timeBonus = (timeRatio * MAX_TIME_BONUS).roundToInt().coerceIn(0, MAX_TIME_BONUS)

        val totalPoints = basePoints + confidenceBonus + timeBonus

        return ScoreBreakdown(
            isCorrect = true,
            basePoints = basePoints,
            confidenceBonus = confidenceBonus,
            timeBonus = timeBonus,
            totalPoints = totalPoints
        )
    }

    /**
     * Calculates accuracy percentage (0 to 100) from correct count and completed rounds.
     */
    fun calculateAccuracy(correctCount: Int, totalRounds: Int): Int {
        if (totalRounds <= 0) return 0
        return ((correctCount.toFloat() / totalRounds.toFloat()) * 100f).roundToInt().coerceIn(0, 100)
    }

    /**
     * Checks if the predicted label matches the target challenge.
     * Handles case insensitivity, spaces vs underscores, and minor formatting differences.
     */
    fun isLabelMatch(target: String, prediction: String): Boolean {
        val normalizedTarget = target.trim().lowercase().replace('-', '_').replace(' ', '_')
        val normalizedPred = prediction.trim().lowercase().replace('-', '_').replace(' ', '_')
        return normalizedTarget == normalizedPred
    }
}

data class ScoreBreakdown(
    val isCorrect: Boolean,
    val basePoints: Int,
    val confidenceBonus: Int,
    val timeBonus: Int,
    val totalPoints: Int
)
