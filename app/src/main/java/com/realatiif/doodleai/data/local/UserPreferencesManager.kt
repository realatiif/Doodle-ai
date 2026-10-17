package com.realatiif.doodleai.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "doodle_ai_preferences")

data class UserGameStats(
    val highScore: Int = 0,
    val completedGames: Int = 0,
    val totalDrawings: Int = 0,
    val totalCorrect: Int = 0,
    val bestStreak: Int = 0,
    val soundEnabled: Boolean = true,
    val hapticsEnabled: Boolean = true,
    val recentHistoryJson: String = ""
) {
    val overallAccuracy: Int
        get() = if (totalDrawings > 0) {
            ((totalCorrect.toFloat() / totalDrawings.toFloat()) * 100).toInt().coerceIn(0, 100)
        } else {
            0
        }
}

class UserPreferencesManager(private val context: Context) {

    private object PreferencesKeys {
        val HIGH_SCORE = intPreferencesKey("high_score")
        val COMPLETED_GAMES = intPreferencesKey("completed_games")
        val TOTAL_DRAWINGS = intPreferencesKey("total_drawings")
        val TOTAL_CORRECT = intPreferencesKey("total_correct")
        val BEST_STREAK = intPreferencesKey("best_streak")
        val SOUND_ENABLED = booleanPreferencesKey("sound_enabled")
        val HAPTICS_ENABLED = booleanPreferencesKey("haptics_enabled")
        val RECENT_HISTORY = stringPreferencesKey("recent_history_json")
    }

    val userStatsFlow: Flow<UserGameStats> = context.dataStore.data.map { prefs ->
        UserGameStats(
            highScore = prefs[PreferencesKeys.HIGH_SCORE] ?: 0,
            completedGames = prefs[PreferencesKeys.COMPLETED_GAMES] ?: 0,
            totalDrawings = prefs[PreferencesKeys.TOTAL_DRAWINGS] ?: 0,
            totalCorrect = prefs[PreferencesKeys.TOTAL_CORRECT] ?: 0,
            bestStreak = prefs[PreferencesKeys.BEST_STREAK] ?: 0,
            soundEnabled = prefs[PreferencesKeys.SOUND_ENABLED] ?: true,
            hapticsEnabled = prefs[PreferencesKeys.HAPTICS_ENABLED] ?: true,
            recentHistoryJson = prefs[PreferencesKeys.RECENT_HISTORY] ?: ""
        )
    }

    suspend fun recordGameSession(
        sessionScore: Int,
        roundsPlayed: Int,
        correctCount: Int,
        sessionBestStreak: Int
    ) {
        context.dataStore.edit { prefs ->
            val currentHigh = prefs[PreferencesKeys.HIGH_SCORE] ?: 0
            if (sessionScore > currentHigh) {
                prefs[PreferencesKeys.HIGH_SCORE] = sessionScore
            }

            val games = prefs[PreferencesKeys.COMPLETED_GAMES] ?: 0
            prefs[PreferencesKeys.COMPLETED_GAMES] = games + 1

            val drawings = prefs[PreferencesKeys.TOTAL_DRAWINGS] ?: 0
            prefs[PreferencesKeys.TOTAL_DRAWINGS] = drawings + roundsPlayed

            val correct = prefs[PreferencesKeys.TOTAL_CORRECT] ?: 0
            prefs[PreferencesKeys.TOTAL_CORRECT] = correct + correctCount

            val currentBestStreak = prefs[PreferencesKeys.BEST_STREAK] ?: 0
            if (sessionBestStreak > currentBestStreak) {
                prefs[PreferencesKeys.BEST_STREAK] = sessionBestStreak
            }
        }
    }

    suspend fun setSoundEnabled(enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[PreferencesKeys.SOUND_ENABLED] = enabled
        }
    }

    suspend fun setHapticsEnabled(enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[PreferencesKeys.HAPTICS_ENABLED] = enabled
        }
    }

    suspend fun clearHistory() {
        context.dataStore.edit { prefs ->
            prefs[PreferencesKeys.COMPLETED_GAMES] = 0
            prefs[PreferencesKeys.TOTAL_DRAWINGS] = 0
            prefs[PreferencesKeys.TOTAL_CORRECT] = 0
            prefs[PreferencesKeys.RECENT_HISTORY] = ""
        }
    }
}
