package com.realatiif.doodleai.viewmodel

import android.app.Application
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.realatiif.doodleai.data.local.UserGameStats
import com.realatiif.doodleai.data.local.UserPreferencesManager
import com.realatiif.doodleai.drawing.DrawingCanvasState
import com.realatiif.doodleai.game.model.ChallengeManager
import com.realatiif.doodleai.game.model.Difficulty
import com.realatiif.doodleai.game.model.GameState
import com.realatiif.doodleai.game.model.PredictionResult
import com.realatiif.doodleai.ml.DrawingClassifier
import com.realatiif.doodleai.ml.DrawingPreprocessor
import com.realatiif.doodleai.ml.EmptyDrawingException
import com.realatiif.doodleai.sound.SoundManager
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.max

class DoodleGameViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        private const val TAG = "DoodleGameViewModel"
    }

    private val preferencesManager = UserPreferencesManager(application)
    val soundManager = SoundManager(application)
    private val classifier: DrawingClassifier = DrawingClassifier(application)
    val supportedLabels: List<String> get() = classifier.labels
    val challengeManager = ChallengeManager(classifier.labels)

    private val _gameState = MutableStateFlow(GameState())
    val gameState: StateFlow<GameState> = _gameState.asStateFlow()

    val userStats: StateFlow<UserGameStats> = preferencesManager.userStatsFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UserGameStats()
        )

    private var timerJob: Job? = null

    /**
     * Starts a new game session with the chosen difficulty.
     */
    fun startGame(difficulty: Difficulty) {
        stopTimer()
        val firstChallenge = challengeManager.nextChallenge()

        _gameState.value = GameState(
            difficulty = difficulty,
            currentRound = 1,
            totalRounds = difficulty.totalRounds,
            currentChallenge = firstChallenge,
            timerRemainingSeconds = difficulty.secondsPerRound,
            isTimerRunning = true,
            totalScore = 0,
            currentStreak = 0,
            bestStreak = 0,
            correctGuesses = 0,
            isRoundSubmitted = false,
            isInferenceLoading = false,
            lastPredictionResult = null,
            isGameOver = false,
            errorMessage = null
        )

        startTimer()
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            _gameState.update { it.copy(isTimerRunning = true) }
            while (isActive) {
                delay(1000L)
                val currentRemaining = _gameState.value.timerRemainingSeconds
                if (currentRemaining > 1) {
                    _gameState.update { it.copy(timerRemainingSeconds = currentRemaining - 1) }
                } else {
                    // Time is up!
                    _gameState.update {
                        it.copy(
                            timerRemainingSeconds = 0,
                            isTimerRunning = false
                        )
                    }
                    handleTimeOut()
                    break
                }
            }
        }
    }

    fun stopTimer() {
        timerJob?.cancel()
        timerJob = null
        _gameState.update { it.copy(isTimerRunning = false) }
    }

    fun resumeTimer() {
        val state = _gameState.value
        if (!state.isRoundSubmitted && !state.isGameOver && state.timerRemainingSeconds > 0 && !state.isTimerRunning) {
            startTimer()
        }
    }

    /**
     * Handles timeout when the player runs out of time without submitting.
     */
    private fun handleTimeOut() {
        val state = _gameState.value
        if (state.isRoundSubmitted || state.isGameOver) return

        soundManager.playIncorrectSound(userStats.value.soundEnabled)
        soundManager.vibrateError(userStats.value.hapticsEnabled)

        val timeoutResult = PredictionResult(
            topPrediction = "Time Out",
            topConfidence = 0.0f,
            rankedPredictions = emptyList(),
            isCorrect = false,
            basePoints = 0,
            confidenceBonus = 0,
            timeBonus = 0,
            totalPointsEarned = 0
        )

        _gameState.update {
            it.copy(
                isRoundSubmitted = true,
                currentStreak = 0,
                lastPredictionResult = timeoutResult,
                errorMessage = "Time's up! No points earned this round."
            )
        }
    }

    /**
     * Submits player drawing for real ML inference.
     */
    fun submitDrawing(bitmap: Bitmap) {
        val state = _gameState.value
        if (state.isRoundSubmitted || state.isInferenceLoading || state.isGameOver) return

        // Stop timer immediately to capture remaining time bonus
        stopTimer()

        // Validate non-empty drawing
        if (DrawingPreprocessor.isDrawingEmpty(bitmap)) {
            soundManager.vibrateError(userStats.value.hapticsEnabled)
            _gameState.update {
                it.copy(
                    errorMessage = "The canvas is empty! Please draw something before submitting."
                )
            }
            resumeTimer()
            return
        }

        soundManager.playSubmitSound(userStats.value.soundEnabled)
        soundManager.vibrateClick(userStats.value.hapticsEnabled)

        _gameState.update {
            it.copy(
                isInferenceLoading = true,
                errorMessage = null
            )
        }

        viewModelScope.launch {
            try {
                val prediction = classifier.classifyDrawing(
                    bitmap = bitmap,
                    targetChallenge = state.currentChallenge,
                    remainingSeconds = state.timerRemainingSeconds,
                    totalRoundSeconds = state.totalRoundSeconds
                )

                val isCorrect = prediction.isCorrect
                val soundOn = userStats.value.soundEnabled
                val hapticsOn = userStats.value.hapticsEnabled

                if (isCorrect) {
                    soundManager.playCorrectSound(soundOn)
                    soundManager.vibrateSuccess(hapticsOn)
                } else {
                    soundManager.playIncorrectSound(soundOn)
                    soundManager.vibrateError(hapticsOn)
                }

                _gameState.update { current ->
                    val newStreak = if (isCorrect) current.currentStreak + 1 else 0
                    val newBestStreak = max(current.bestStreak, newStreak)
                    val newCorrect = if (isCorrect) current.correctGuesses + 1 else current.correctGuesses
                    val newScore = current.totalScore + prediction.totalPointsEarned

                    current.copy(
                        totalScore = newScore,
                        currentStreak = newStreak,
                        bestStreak = newBestStreak,
                        correctGuesses = newCorrect,
                        isRoundSubmitted = true,
                        isInferenceLoading = false,
                        lastPredictionResult = prediction
                    )
                }
            } catch (e: EmptyDrawingException) {
                soundManager.vibrateError(userStats.value.hapticsEnabled)
                _gameState.update {
                    it.copy(
                        isInferenceLoading = false,
                        errorMessage = e.message ?: "Drawing is empty."
                    )
                }
                resumeTimer()
            } catch (e: Exception) {
                Log.e(TAG, "Inference failure: ${e.message}", e)
                soundManager.vibrateError(userStats.value.hapticsEnabled)
                _gameState.update {
                    it.copy(
                        isInferenceLoading = false,
                        errorMessage = "AI recognition error: ${e.localizedMessage ?: "Unknown error"}"
                    )
                }
                resumeTimer()
            }
        }
    }

    /**
     * Proceeds to next round or transitions to Game Over.
     */
    fun nextRound(canvasState: DrawingCanvasState) {
        val state = _gameState.value
        canvasState.clear()

        if (state.isLastRound) {
            // End of game session
            stopTimer()
            soundManager.playGameOverSound(userStats.value.soundEnabled)
            _gameState.update { it.copy(isGameOver = true) }

            // Persist session to DataStore
            viewModelScope.launch {
                preferencesManager.recordGameSession(
                    sessionScore = state.totalScore,
                    roundsPlayed = state.totalRounds,
                    correctCount = state.correctGuesses,
                    sessionBestStreak = state.bestStreak
                )
            }
        } else {
            // Advance to next round
            val nextChallenge = challengeManager.nextChallenge()
            _gameState.update {
                it.copy(
                    currentRound = it.currentRound + 1,
                    currentChallenge = nextChallenge,
                    timerRemainingSeconds = it.totalRoundSeconds,
                    isRoundSubmitted = false,
                    lastPredictionResult = null,
                    errorMessage = null
                )
            }
            startTimer()
        }
    }

    fun restartGame() {
        startGame(_gameState.value.difficulty)
    }

    fun dismissError() {
        _gameState.update { it.copy(errorMessage = null) }
    }

    fun setSoundEnabled(enabled: Boolean) {
        viewModelScope.launch {
            preferencesManager.setSoundEnabled(enabled)
        }
    }

    fun setHapticsEnabled(enabled: Boolean) {
        viewModelScope.launch {
            preferencesManager.setHapticsEnabled(enabled)
        }
    }

    fun clearHistory() {
        viewModelScope.launch {
            preferencesManager.clearHistory()
        }
    }

    override fun onCleared() {
        super.onCleared()
        stopTimer()
        classifier.close()
        soundManager.close()
    }
}
