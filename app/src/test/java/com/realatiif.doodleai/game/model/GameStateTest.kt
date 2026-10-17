package com.realatiif.doodleai.game.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class GameStateTest {

    @Test
    fun difficultyPresets_matchRequirements() {
        // Easy: 60s, 5 rounds
        assertEquals(60, Difficulty.EASY.secondsPerRound)
        assertEquals(5, Difficulty.EASY.totalRounds)

        // Medium: 45s, 8 rounds
        assertEquals(45, Difficulty.MEDIUM.secondsPerRound)
        assertEquals(8, Difficulty.MEDIUM.totalRounds)

        // Hard: 30s, 10 rounds
        assertEquals(30, Difficulty.HARD.secondsPerRound)
        assertEquals(10, Difficulty.HARD.totalRounds)
    }

    @Test
    fun isLastRound_detectsEndOfSession() {
        val ongoingState = GameState(
            difficulty = Difficulty.EASY,
            currentRound = 3,
            totalRounds = 5
        )
        assertFalse(ongoingState.isLastRound)

        val lastRoundState = GameState(
            difficulty = Difficulty.EASY,
            currentRound = 5,
            totalRounds = 5
        )
        assertTrue(lastRoundState.isLastRound)
    }

    @Test
    fun accuracyPercentage_reflectsRounds() {
        val state = GameState(
            currentRound = 5,
            isRoundSubmitted = true,
            correctGuesses = 4
        )
        assertEquals(80, state.accuracyPercentage)
    }
}
