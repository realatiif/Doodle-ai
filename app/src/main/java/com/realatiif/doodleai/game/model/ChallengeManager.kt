package com.realatiif.doodleai.game.model

import java.util.LinkedList

/**
 * Generates drawing challenges strictly from the model's supported classes.
 * Ensures variety by preventing recent duplicates.
 */
class ChallengeManager(private val supportedLabels: List<String>) {

    private val recentHistory = LinkedList<String>()
    private val maxRecentHistorySize = 15

    init {
        require(supportedLabels.isNotEmpty()) { "Supported labels list must not be empty" }
    }

    /**
     * Picks a random supported challenge.
     */
    fun nextChallenge(): String {
        val candidates = if (supportedLabels.size > maxRecentHistorySize) {
            supportedLabels.filter { it !in recentHistory }
        } else {
            supportedLabels
        }

        val chosen = candidates.randomOrNull() ?: supportedLabels.random()

        recentHistory.add(chosen)
        if (recentHistory.size > maxRecentHistorySize) {
            recentHistory.removeFirst()
        }

        return chosen
    }

    /**
     * Resets recent challenge memory.
     */
    fun reset() {
        recentHistory.clear()
    }

    val totalSupportedClasses: Int
        get() = supportedLabels.size

    companion object {
        fun formatChallengeForDisplay(label: String): String {
            return label
                .replace('_', ' ')
                .replace('-', ' ')
                .split(' ')
                .joinToString(" ") { word ->
                    word.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
                }
        }
    }
}
