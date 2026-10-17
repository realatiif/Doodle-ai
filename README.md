# 🎨 Doodle AI — Draw It. Let AI Guess It.

[![Platform](https://img.shields.io/badge/Platform-Native%20Android-green.svg)](https://developer.android.com/)
[![Kotlin](https://img.shields.io/badge/Language-Kotlin%202.2-purple.svg)](https://kotlinlang.org/)
[![Compose](https://img.shields.io/badge/UI-Jetpack%20Compose%20%2F%20Material%203-blue.svg)](https://developer.android.com/jetpack/compose)
[![ML Engine](https://img.shields.io/badge/ML-TensorFlow%20Lite%202.16-orange.svg)](https://www.tensorflow.org/lite)
[![License](https://img.shields.io/badge/Dataset%20License-CC%20BY%204.0-lightgrey.svg)](https://creativecommons.org/licenses/by/4.0/)

> **Internship Project for WebEra Solutions PK**  
> **Developer:** Muhammad Atif  
> **Package:** `com.realatiif.doodleai`  
> **Platform:** Native Android (Min SDK 26, Target SDK 35, Compile SDK 36)  

---

## 📌 1. Project Overview

**Doodle AI** is an AI-powered native Android drawing game where players are challenged to sketch everyday objects against a ticking countdown timer. A genuine, on-device deep learning model (Convolutional Neural Network) analyzes the user's touch strokes in real-time and predicts what was drawn.

### 🌟 Key Highlights:
* 🤖 **Genuine On-Device AI:** Runs a trained CNN model via TensorFlow Lite. No mocked predictions, no random outputs, and no network latency.
* 🔒 **100% Offline & Private:** Zero cloud APIs, paid keys, or external tracking. All inference occurs locally on the device CPU.
* ✍️ **Fluid Drawing Engine:** Smooth touch/stylus drawing with quadratic Bézier curve interpolation, customizable brush sizes, eraser, undo, and canvas clear.
* 📐 **Mathematical Preprocessing:** Automatic drawing localization, empty canvas rejection, aspect-ratio preservation, 28×28 downsampling, and polarity inversion.
* 🎯 **Dynamic Scoring:** 100 base points for top-1 match + up to 25 confidence bonus + up to 25 time bonus points.
* 📱 **Full Material 3 Experience:** Sleek dark navy/purple gaming theme with vibrant neon cyan accents, responsive layouts, procedural audio effects, and haptic feedback.

---

## 🏗️ 2. Technology Stack & Architecture

```
com.realatiif.doodleai/
  ├── data/
  │    ├── local/          # Jetpack DataStore Preferences for persistent scores & settings
  │    └── repository/     # GameRepository contract and implementation
  ├── ml/
  │    ├── DrawingClassifier.kt   # TFLite interpreter, off-main-thread inference (4 CPU threads)
  │    ├── DrawingPreprocessor.kt # Bounding box, centering, aspect-ratio padding, polarity inversion
  │    └── ModelInfo.kt           # Metadata, dataset licensing, and tensor specifications
  ├── drawing/
  │    ├── DrawingStroke.kt       # Stroke data model (points, brush width, eraser mode)
  │    ├── DrawingCanvasState.kt  # State holder and offscreen high-res bitmap rasterizer
  │    └── DrawingCanvas.kt       # Compose Canvas with sub-pixel touch gesture detection
  ├── game/
  │    ├── model/                 # Difficulty, GameState, PredictionResult, ChallengeManager
  │    └── logic/                 # ScoringEngine (pure calculation of points, bonuses & accuracy)
  ├── sound/
  │    └── SoundManager.kt        # Procedural audio tone synthesis (ToneGenerator) & Haptics
  ├── ui/
  │    ├── theme/                 # Dark gaming theme, custom colors (Color.kt), Material 3 Typography
  │    ├── components/            # AppTopBar, StatCard, TimerIndicator, BrushControlBar, ResultDialog
  │    └── screens/               # 10 Full Android Screens (Splash, Home, Difficulty, Game, etc.)
  ├── viewmodel/
  │    └── DoodleGameViewModel.kt # StateFlow, coroutine timer, inference orchestration, persistence
  └── MainActivity.kt             # Single-Activity architecture with Compose navigation & lifecycle observers
```

---

## 🧠 3. Real AI / Machine Learning Integration

The app bundles a verified **TensorFlow Lite model** (`quickdraw.tflite`, 442 KB) trained on Google's Quick, Draw! dataset:

| Attribute | Specification |
| :--- | :--- |
| **Model Architecture** | 3× Conv2D (16, 32, 64 filters) + 3× MaxPool2D + Dense(128) + Dense(100) Softmax |
| **Input Tensor** | `[1, 28, 28, 1]` (FLOAT32, normalized `[0.0, 1.0]`) |
| **Output Tensor** | `[1, 100]` (FLOAT32, class probability distribution summing to 1.0) |
| **Supported Classes** | 100 common objects (`apple`, `airplane`, `bicycle`, `cat`, `car`, `clock`, `tree`, `star`, etc.) |
| **Accuracy** | 93% top-5 accuracy on the Quick, Draw! test dataset |
| **Inference Time** | ~15–35 ms on mobile CPU |

### Preprocessing Pipeline:
1. **Empty Check:** Validates that stroke pixel count exceeds threshold ($\ge 25$ pixels) to prevent empty submissions.
2. **Bounding Box Detection:** Calculates `[minX, minY, maxX, maxY]` around the drawing content.
3. **Aspect Ratio Preservation:** Centers the cropped drawing inside a square canvas with 15% padding.
4. **Resizing:** Bilinearly downsamples to $28 \times 28$ pixels.
5. **Polarity Inversion:** Quick, Draw! uses white strokes on black backgrounds; the preprocessor converts user ink via $\text{pixel} = 1.0 - (\text{brightness} / 255.0)$.

For comprehensive ML details, see [docs/AI_ML_EXPLANATION.md](docs/AI_ML_EXPLANATION.md).

---

## 🎮 4. Gameplay & Scoring Rules

### Difficulty Presets:
* **Easy:** 60 seconds per challenge • 5 rounds per session
* **Medium:** 45 seconds per challenge • 8 rounds per session
* **Hard:** 30 seconds per challenge • 10 rounds per session

### Scoring Engine:
$$\text{Total Points} = \text{Base Points} + \text{Confidence Bonus} + \text{Time Bonus}$$

* **Base Points:** **100 pts** if the AI's top prediction matches the target challenge (**0 pts** if incorrect).
* **Confidence Bonus:** Up to **25 pts** based on actual model probability: $\text{round}(\text{confidence} \times 25)$.
* **Time Bonus:** Up to **25 pts** based on remaining time: $\text{round}\left(\frac{\text{Remaining Time}}{\text{Total Time}} \times 25\right)$.
* **Streak Tracking:** Consecutive correct guesses build your active streak and contribute to your lifetime best streak.

---

## 📱 5. Implemented Screens

1. **Splash Screen:** Animated brand launch with glowing logo, tagline, and developer credits.
2. **Home Screen:** High score display, accuracy metric, big "PLAY" button, and quick navigation.
3. **Difficulty Screen:** Selection between Easy, Medium, and Hard modes with descriptions.
4. **Game Screen:** Challenge prompt, countdown timer progress bar, interactive drawing canvas, brush controls, submit button, and live inference loader.
5. **Prediction Result Dialog:** Modal displaying AI's top guess, confidence percentage, points breakdown, and top-3 ranked alternatives.
6. **Game Over Screen:** Final session score, accuracy rate, correct guesses count, best streak, and replay action.
7. **Progress Screen:** Career statistics (games played, total drawings, accuracy, high score) and reset option.
8. **Tutorial Screen:** Illustrated step-by-step instructions on gameplay, scoring, and drawing tips.
9. **AI Model Info Screen:** Technical neural network specs, layer layout, dataset attribution, and 100% offline privacy notice.
10. **Settings Screen:** Sound effects toggle, haptic vibration toggle, and application metadata.

---

## 🛠️ 6. Build & Setup Instructions

### Prerequisites:
* Android Studio (Koala / Ladybug / Meerkat or newer)
* JDK 17, 21, or 25
* Android SDK (API 36 / 35)

### Windows PowerShell Commands:

```powershell
# Clone the repository
git clone https://github.com/<your-username>/doodle-ai.git
cd doodle-ai

# Run all unit tests
.\gradlew.bat testDebugUnitTest --no-daemon

# Build the Debug APK
.\gradlew.bat assembleDebug --no-daemon
```

### Install APK onto Device or Emulator:
```powershell
# The compiled APK is located in the obvious release folder:
adb install -r apk/doodle-ai-debug.apk

# Launch the game:
adb shell am start -n com.realatiif.doodleai/.MainActivity
```

---

## 🧪 7. Testing Summary

* **Unit Tests Passed:** 14 / 14 (100% pass rate)
  * `ScoringEngineTest`: Scoring accuracy, bonuses, penalty, label matching.
  * `ChallengeManagerTest`: Supported label generation, history uniqueness, formatting.
  * `GameStateTest`: Round boundaries, timer limits, difficulty transitions.
  * `PreprocessingMathTest`: Polarity inversion and tensor dimensions.
* **APK Output:** `apk/doodle-ai-debug.apk` (32.4 MB)
* Full testing report: [docs/TESTING.md](docs/TESTING.md)

---

## 📄 8. Submission Documents

* [docs/AI_ML_EXPLANATION.md](docs/AI_ML_EXPLANATION.md) — Comprehensive Machine Learning breakdown.
* [docs/TESTING.md](docs/TESTING.md) — QA verification report and test command evidence.
* [docs/DEMO_SCRIPT.md](docs/DEMO_SCRIPT.md) — 75-second video recording walkthrough & narration.
* [docs/LINKEDIN_POST.md](docs/LINKEDIN_POST.md) — Professional LinkedIn post draft mentioning WebEra Solutions PK.
* [docs/SUBMISSION_CHECKLIST.md](docs/SUBMISSION_CHECKLIST.md) — Master requirement verification checklist.

---

## 📜 9. License & Attribution

* Google Quick, Draw! Dataset: Licensed under [Creative Commons Attribution 4.0 International (CC BY 4.0)](https://creativecommons.org/licenses/by/4.0/).
* Project codebase developed by **Muhammad Atif** as an internship project for **WebEra Solutions PK**.
