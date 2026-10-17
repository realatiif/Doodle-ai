# Doodle AI — 60–90 Second Video Demonstration Script

**Project:** Doodle AI  
**Developer:** Muhammad Atif  
**Target Duration:** 75 seconds  
**Objective:** Present a professional video walk-through of the native Android application, showcasing real on-device AI drawing recognition, gameplay mechanics, and UI/UX polish.

---

## Screen Recording Instructions

### Option A: Using Physical Android Phone
1. Connect your Android phone with **USB Debugging** enabled.
2. Install the APK:
   ```powershell
   adb install -r apk/doodle-ai-debug.apk
   ```
3. Record the screen directly using Android's native screen recorder:
   * Swipe down quick settings -> tap **Screen Record**.
   * Or via adb:
     ```powershell
     adb shell screenrecord /sdcard/doodle_ai_demo.mp4
     # (Press Ctrl+C when done, then pull the file)
     adb pull /sdcard/doodle_ai_demo.mp4 ./
     ```

### Option B: Using Android Studio Emulator
1. Open Android Studio -> Device Manager -> Start any phone emulator (e.g., Pixel 8 API 34/35).
2. Drag and drop `apk/doodle-ai-debug.apk` onto the emulator window to install.
3. Click the **Screen Record** (camera) button in the emulator toolbar to record video directly.

---

## Video Timeline & Voiceover Script

### [0:00 - 0:10] Intro & Branding
* **Visual:** Launch the app from the Android home screen. The splash screen animates with the glowing cyan/purple logo, tagline *"Draw It. Let AI Guess It."*, and developer name.
* **Narration:**
  > *"Hello! Welcome to Doodle AI, an AI-powered drawing game built natively for Android using Jetpack Compose and TensorFlow Lite. I'm Muhammad Atif, and today I'll walk you through how our on-device deep learning model guesses drawings in real-time."*

### [0:10 - 0:20] Home Screen & Difficulty Selection
* **Visual:** The Home screen loads showing personal best points and career accuracy. Tap the **PLAY GAME** button. The Difficulty Selection screen opens with Easy (60s), Medium (45s), and Hard (30s). Tap **Easy**.
* **Narration:**
  > *"From the home screen, players can inspect their stats, review the neural network specs, or start playing. We offer three difficulty presets that adjust the timer and round limits. Let's select Easy mode with 60 seconds per challenge."*

### [0:20 - 0:45] Live Drawing Canvas & Real AI Inference
* **Visual:** The game screen loads with challenge `Target: Apple`. The countdown timer starts ticking.
* Use finger or stylus to sketch an apple with a stem and leaf on the white canvas. Show the brush size toggle (thin/medium/thick) and eraser.
* Tap **SUBMIT DRAWING**. The button turns to *"AI IS ANALYZING..."* for ~40ms.
* **Narration:**
  > *"Our challenge is to draw an 'Apple'. The canvas is powered by smooth quadratic bezier strokes with complete brush sizing, eraser, and undo support. Now let's hit Submit to pass our drawing to the neural network."*

### [0:45 - 0:60] Prediction Results & Scoring Breakdown
* **Visual:** The Result Dialog pops up with green checkmark:
  * Top Guess: `Apple`
  * Confidence: `94%`
  * Score Breakdown: Base Points +100, Confidence Bonus +23, Time Bonus +20. Total: `+143 pts`.
  * Top Ranked Alternatives: `#2: Peach (4%)`, `#3: Pear (1%)`.
* Tap **Next Challenge**.
* **Narration:**
  > *"The TensorFlow Lite classifier instantly preprocessed our sketch, centered it, and predicted 'Apple' with 94% confidence. Because it matched the target challenge, the game awarded 100 base points, plus bonuses for high confidence and fast completion time."*

### [0:60 - 0:75] Progress & AI Model Specs
* **Visual:** Briefly show the next challenge, then tap close to view the **My Progress** screen showing updated games played, accuracy rate, and streak. Then tap **AI Model Info** to show the Quick, Draw! CNN specs.
* **Narration:**
  > *"All scores and career statistics persist locally via Jetpack DataStore. Most importantly, all inference runs 100% on-device with zero cloud dependencies or external API keys, ensuring complete user privacy. Thank you!"*
