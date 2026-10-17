package com.realatiif.doodleai.game.model

/**
 * Immutable representation of the game loop state.
 */
data class GameState(
    val difficulty: Difficulty = Difficulty.EASY,
    val currentRound: Int = 1,
    val totalRounds: Int = Difficulty.EASY.totalRounds,
    val currentChallenge: String = "",
    val timerRemainingSeconds: Int = Difficulty.EASY.secondsPerRound,
    val isTimerRunning: Boolean = false,
    val totalScore: Int = 0,
    val currentStreak: Int = 0,
    val bestStreak: Int = 0,
    val correctGuesses: Int = 0,
    val isRoundSubmitted: Boolean = false,
    val isInferenceLoading: Boolean = false,
    val lastPredictionResult: PredictionResult? = null,
    val isGameOver: Boolean = false,
    val errorMessage: String? = null
) {
    val totalRoundSeconds: Int
        get() = difficulty.secondsPerRound

    val displayChallenge: String
        get() = ChallengeManager.formatChallengeForDisplay(currentChallenge)

    val accuracyPercentage: Int
        get() = if (currentRound > 1 || isRoundSubmitted) {
            val completed = if (isRoundSubmitted) currentRound else currentRound - 1
            if (completed > 0) ((correctGuesses.toFloat() / completed.toFloat()) * 100).toInt().coerceIn(0, 100) else 0
        } else {
            0
        }

    val isLastRound: Boolean
        get() = currentRound >= totalRounds
}
