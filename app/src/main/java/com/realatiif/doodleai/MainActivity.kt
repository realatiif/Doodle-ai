package com.realatiif.doodleai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.realatiif.doodleai.drawing.rememberDrawingCanvasState
import com.realatiif.doodleai.ui.screens.DifficultyScreen
import com.realatiif.doodleai.ui.screens.GameOverScreen
import com.realatiif.doodleai.ui.screens.GameScreen
import com.realatiif.doodleai.ui.screens.HomeScreen
import com.realatiif.doodleai.ui.screens.ModelInfoScreen
import com.realatiif.doodleai.ui.screens.ProgressScreen
import com.realatiif.doodleai.ui.screens.SettingsScreen
import com.realatiif.doodleai.ui.screens.SplashScreen
import com.realatiif.doodleai.ui.screens.TutorialScreen
import com.realatiif.doodleai.ui.theme.DoodleAITheme
import com.realatiif.doodleai.ui.theme.NavyBg
import com.realatiif.doodleai.viewmodel.DoodleGameViewModel

enum class AppScreen {
    SPLASH,
    HOME,
    DIFFICULTY,
    GAME,
    GAME_OVER,
    PROGRESS,
    TUTORIAL,
    MODEL_INFO,
    SETTINGS
}

class MainActivity : ComponentActivity() {

    private val viewModel: DoodleGameViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            DoodleAITheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = NavyBg
                ) {
                    DoodleAIApp(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun DoodleAIApp(viewModel: DoodleGameViewModel) {
    var currentScreen by remember { mutableStateOf(AppScreen.SPLASH) }
    val gameState by viewModel.gameState.collectAsState()
    val userStats by viewModel.userStats.collectAsState()
    val canvasState = rememberDrawingCanvasState()

    // Handle lifecycle pause/resume for timer
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_PAUSE -> {
                    viewModel.stopTimer()
                }
                Lifecycle.Event.ON_RESUME -> {
                    if (currentScreen == AppScreen.GAME) {
                        viewModel.resumeTimer()
                    }
                }
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    // React to game over transition
    if (gameState.isGameOver && currentScreen == AppScreen.GAME) {
        currentScreen = AppScreen.GAME_OVER
    }

    // System back button handling
    BackHandler(enabled = currentScreen != AppScreen.HOME && currentScreen != AppScreen.SPLASH) {
        when (currentScreen) {
            AppScreen.GAME -> {
                // Game handles quit confirmation dialog internally
            }
            AppScreen.GAME_OVER -> {
                currentScreen = AppScreen.HOME
            }
            else -> {
                currentScreen = AppScreen.HOME
            }
        }
    }

    Crossfade(targetState = currentScreen, label = "ScreenTransition") { screen ->
        when (screen) {
            AppScreen.SPLASH -> {
                SplashScreen(
                    onAnimationFinished = {
                        currentScreen = AppScreen.HOME
                    }
                )
            }

            AppScreen.HOME -> {
                HomeScreen(
                    userStats = userStats,
                    onPlayClicked = { currentScreen = AppScreen.DIFFICULTY },
                    onTutorialClicked = { currentScreen = AppScreen.TUTORIAL },
                    onProgressClicked = { currentScreen = AppScreen.PROGRESS },
                    onModelInfoClicked = { currentScreen = AppScreen.MODEL_INFO },
                    onSettingsClicked = { currentScreen = AppScreen.SETTINGS }
                )
            }

            AppScreen.DIFFICULTY -> {
                DifficultyScreen(
                    onDifficultySelected = { difficulty ->
                        canvasState.clear()
                        viewModel.startGame(difficulty)
                        currentScreen = AppScreen.GAME
                    },
                    onBack = { currentScreen = AppScreen.HOME }
                )
            }

            AppScreen.GAME -> {
                GameScreen(
                    gameState = gameState,
                    canvasState = canvasState,
                    onSubmitDrawing = { state ->
                        val bitmap = state.exportToBitmap(512f, 512f)
                        viewModel.submitDrawing(bitmap)
                    },
                    onNextRound = { state ->
                        viewModel.nextRound(state)
                    },
                    onQuitGame = {
                        viewModel.stopTimer()
                        canvasState.clear()
                        currentScreen = AppScreen.HOME
                    },
                    onDismissError = {
                        viewModel.dismissError()
                    }
                )
            }

            AppScreen.GAME_OVER -> {
                GameOverScreen(
                    gameState = gameState,
                    onRestartClicked = {
                        canvasState.clear()
                        viewModel.restartGame()
                        currentScreen = AppScreen.GAME
                    },
                    onHomeClicked = {
                        canvasState.clear()
                        currentScreen = AppScreen.HOME
                    }
                )
            }

            AppScreen.PROGRESS -> {
                ProgressScreen(
                    userStats = userStats,
                    onClearHistory = { viewModel.clearHistory() },
                    onBack = { currentScreen = AppScreen.HOME }
                )
            }

            AppScreen.TUTORIAL -> {
                TutorialScreen(
                    onBack = { currentScreen = AppScreen.HOME }
                )
            }

            AppScreen.MODEL_INFO -> {
                ModelInfoScreen(
                    supportedLabels = viewModel.supportedLabels,
                    onBack = { currentScreen = AppScreen.HOME }
                )
            }

            AppScreen.SETTINGS -> {
                SettingsScreen(
                    soundEnabled = userStats.soundEnabled,
                    hapticsEnabled = userStats.hapticsEnabled,
                    onSoundToggled = { viewModel.setSoundEnabled(it) },
                    onHapticsToggled = { viewModel.setHapticsEnabled(it) },
                    onBack = { currentScreen = AppScreen.HOME }
                )
            }
        }
    }
}
