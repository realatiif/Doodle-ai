package com.realatiif.doodleai.game.model

/**
 * Game difficulty presets according to the project specifications.
 */
enum class Difficulty(
    val secondsPerRound: Int,
    val totalRounds: Int,
    val displayName: String,
    val description: String
) {
    EASY(
        secondsPerRound = 60,
        totalRounds = 5,
        displayName = "Easy",
        description = "60 seconds • 5 rounds"
    ),
    MEDIUM(
        secondsPerRound = 45,
        totalRounds = 8,
        displayName = "Medium",
        description = "45 seconds • 8 rounds"
    ),
    HARD(
        secondsPerRound = 30,
        totalRounds = 10,
        displayName = "Hard",
        description = "30 seconds • 10 rounds"
    )
}
