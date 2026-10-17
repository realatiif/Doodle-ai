package com.realatiif.doodleai.game.model

/**
 * A single class prediction candidate with its confidence score.
 */
data class RankedPrediction(
    val label: String,
    val confidence: Float,
    val rank: Int
) {
    val formattedConfidence: String
        get() = "${(confidence * 100).toInt()}%"

    val displayLabel: String
        get() = label.replace('_', ' ').replaceFirstChar { it.uppercase() }
}

/**
 * Full prediction result from the ML model and associated scoring calculation.
 */
data class PredictionResult(
    val topPrediction: String,
    val topConfidence: Float,
    val rankedPredictions: List<RankedPrediction>,
    val isCorrect: Boolean,
    val basePoints: Int = 0,
    val confidenceBonus: Int = 0,
    val timeBonus: Int = 0,
    val totalPointsEarned: Int = 0,
    val timestamp: Long = System.currentTimeMillis()
) {
    val formattedTopConfidence: String
        get() = "${(topConfidence * 100).toInt()}%"

    val displayTopPrediction: String
        get() = topPrediction.replace('_', ' ').replaceFirstChar { it.uppercase() }
}
