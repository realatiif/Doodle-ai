package com.realatiif.doodleai.game.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ChallengeManagerTest {

    private val sampleLabels = listOf(
        "apple", "airplane", "bicycle", "cat", "car",
        "clock", "ice_cream", "tree", "star", "umbrella"
    )

    @Test
    fun nextChallenge_alwaysPicksFromSupportedList() {
        val manager = ChallengeManager(sampleLabels)

        for (i in 0 until 50) {
            val challenge = manager.nextChallenge()
            assertTrue("Challenge '$challenge' must belong to supported labels", sampleLabels.contains(challenge))
        }
    }

    @Test
    fun formatChallengeForDisplay_replacesSeparatorsAndCapitalizes() {
        assertEquals("Ice Cream", ChallengeManager.formatChallengeForDisplay("ice_cream"))
        assertEquals("Cell Phone", ChallengeManager.formatChallengeForDisplay("cell-phone"))
        assertEquals("Airplane", ChallengeManager.formatChallengeForDisplay("airplane"))
        assertEquals("Baseball Bat", ChallengeManager.formatChallengeForDisplay("baseball_bat"))
    }

    @Test
    fun totalSupportedClasses_matchesSize() {
        val manager = ChallengeManager(sampleLabels)
        assertEquals(sampleLabels.size, manager.totalSupportedClasses)
    }
}
