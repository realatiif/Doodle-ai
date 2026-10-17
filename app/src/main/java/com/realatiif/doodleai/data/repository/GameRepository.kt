package com.realatiif.doodleai.data.repository

import com.realatiif.doodleai.data.local.UserGameStats
import com.realatiif.doodleai.data.local.UserPreferencesManager
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface defining data access contracts for Doodle AI.
 */
interface GameRepository {
    val userStats: Flow<UserGameStats>
    suspend fun recordGameSession(score: Int, rounds: Int, correct: Int, streak: Int)
    suspend fun setSoundEnabled(enabled: Boolean)
    suspend fun setHapticsEnabled(enabled: Boolean)
    suspend fun clearHistory()
}

/**
 * Default implementation backed by UserPreferencesManager (DataStore).
 */
class DefaultGameRepository(
    private val preferencesManager: UserPreferencesManager
) : GameRepository {

    override val userStats: Flow<UserGameStats> = preferencesManager.userStatsFlow

    override suspend fun recordGameSession(score: Int, rounds: Int, correct: Int, streak: Int) {
        preferencesManager.recordGameSession(score, rounds, correct, streak)
    }

    override suspend fun setSoundEnabled(enabled: Boolean) {
        preferencesManager.setSoundEnabled(enabled)
    }

    override suspend fun setHapticsEnabled(enabled: Boolean) {
        preferencesManager.setHapticsEnabled(enabled)
    }

    override suspend fun clearHistory() {
        preferencesManager.clearHistory()
    }
}
