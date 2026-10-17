package com.realatiif.doodleai.game.logic

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ScoringEngineTest {

    @Test
    fun calculateScore_correctPrediction_awardsBasePointsAndBonuses() {
        // Target: apple, Prediction: apple, Confidence: 1.0 (100%), Time: 30s / 30s (100%)
        val result = ScoringEngine.calculateScore(
            targetChallenge = "apple",
            predictedLabel = "apple",
            confidence = 1.0f,
            remainingSeconds = 30,
            totalRoundSeconds = 30
        )

        assertTrue(result.isCorrect)
        assertEquals(100, result.basePoints)
        assertEquals(25, result.confidenceBonus)
        assertEquals(25, result.timeBonus)
        assertEquals(150, result.totalPoints)
    }

    @Test
    fun calculateScore_correctPrediction_partialBonuses() {
        // Target: cat, Prediction: cat, Confidence: 0.8 (80% -> 20 pts), Time: 15s / 30s (50% -> 13 pts)
        val result = ScoringEngine.calculateScore(
            targetChallenge = "cat",
            predictedLabel = "cat",
            confidence = 0.8f,
            remainingSeconds = 15,
            totalRoundSeconds = 30
        )

        assertTrue(result.isCorrect)
        assertEquals(100, result.basePoints)
        assertEquals(20, result.confidenceBonus)
        assertEquals(13, result.timeBonus)
        assertEquals(133, result.totalPoints)
    }

    @Test
    fun calculateScore_incorrectPrediction_awardsZeroPoints() {
        // Target: airplane, Prediction: bird, Confidence: 0.95
        val result = ScoringEngine.calculateScore(
            targetChallenge = "airplane",
            predictedLabel = "bird",
            confidence = 0.95f,
            remainingSeconds = 25,
            totalRoundSeconds = 30
        )

        assertFalse(result.isCorrect)
        assertEquals(0, result.basePoints)
        assertEquals(0, result.confidenceBonus)
        assertEquals(0, result.timeBonus)
        assertEquals(0, result.totalPoints)
    }

    @Test
    fun calculateScore_timeoutOrZeroRemainingTime() {
        val result = ScoringEngine.calculateScore(
            targetChallenge = "tree",
            predictedLabel = "tree",
            confidence = 0.6f,
            remainingSeconds = 0,
            totalRoundSeconds = 60
        )

        assertTrue(result.isCorrect)
        assertEquals(100, result.basePoints)
        assertEquals(15, result.confidenceBonus) // 0.6 * 25 = 15
        assertEquals(0, result.timeBonus)
        assertEquals(115, result.totalPoints)
    }

    @Test
    fun calculateAccuracy_variousRounds() {
        assertEquals(0, ScoringEngine.calculateAccuracy(0, 0))
        assertEquals(0, ScoringEngine.calculateAccuracy(0, 5))
        assertEquals(100, ScoringEngine.calculateAccuracy(5, 5))
        assertEquals(60, ScoringEngine.calculateAccuracy(3, 5))
        assertEquals(50, ScoringEngine.calculateAccuracy(4, 8))
        assertEquals(70, ScoringEngine.calculateAccuracy(7, 10))
    }

    @Test
    fun isLabelMatch_handlesFormattingDifferences() {
        assertTrue(ScoringEngine.isLabelMatch("ice_cream", "Ice Cream"))
        assertTrue(ScoringEngine.isLabelMatch("cell_phone", "cell-phone"))
        assertTrue(ScoringEngine.isLabelMatch("T-SHIRT", "t_shirt"))
        assertTrue(ScoringEngine.isLabelMatch("  baseball_bat  ", "Baseball Bat"))
        assertFalse(ScoringEngine.isLabelMatch("apple", "banana"))
    }
}
