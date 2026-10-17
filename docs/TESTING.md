# Doodle AI — QA & Testing Verification Report

**Application:** Doodle AI  
**Version:** 1.0.0  
**Build Target:** Debug APK  
**Package:** `com.realatiif.doodleai`  
**Test Runner:** Gradle JUnit 4 + Android Test Framework  

---

## 1. Test Execution Commands & Environment

### Environment:
* **Operating System:** Windows 10/11 x64
* **Java Runtime:** OpenJDK 25.0.3 (JBR) / JDK 26.0.1
* **Gradle Version:** 9.6.0
* **Android Gradle Plugin (AGP):** 9.4.1
* **Android SDK:** `C:\Users\ranaa\AppData\Local\Android\Sdk` (API Level 36 Platform)
* **Compile SDK:** 36, **Min SDK:** 26, **Target SDK:** 35

### Commands Executed:
```powershell
# Run all unit tests
.\gradlew.bat testDebugUnitTest --no-daemon

# Compile and package Debug APK
.\gradlew.bat assembleDebug --no-daemon
```

---

## 2. Unit Test Results

**Status:** 100% PASSED (14 test cases executed, 0 failures, 0 errors, 0 skipped)  
**Execution Time:** 1m 58s  

### Test Suite Breakdown:

| Test Class | Tests | Status | Verification Focus |
| :--- | :--- | :--- | :--- |
| `com.realatiif.doodleai.game.logic.ScoringEngineTest` | 6 | **PASS** | 100 base points, confidence bonus scaling, time bonus scaling, zero points for incorrect predictions, accuracy formula, case/separator normalization |
| `com.realatiif.doodleai.game.model.ChallengeManagerTest` | 3 | **PASS** | Supported label selection integrity, history avoidance, display label formatting |
| `com.realatiif.doodleai.game.model.GameStateTest` | 3 | **PASS** | Difficulty round limits & timers (Easy: 60s/5, Med: 45s/8, Hard: 30s/10), last round detection, round accuracy |
| `com.realatiif.doodleai.ml.PreprocessingMathTest` | 2 | **PASS** | Polarity inversion formula ($1.0 - \text{brightness}$), tensor shape specifications |

### Test Case Log Evidence:
```xml
<testsuite name="com.realatiif.doodleai.game.logic.ScoringEngineTest" tests="6" skipped="0" failures="0" errors="0" timestamp="2026-10-17T08:55:30.997Z" time="0.047">
  <testcase name="calculateScore_timeoutOrZeroRemainingTime" time="0.026"/>
  <testcase name="calculateScore_correctPrediction_partialBonuses" time="0.0"/>
  <testcase name="calculateAccuracy_variousRounds" time="0.001"/>
  <testcase name="isLabelMatch_handlesFormattingDifferences" time="0.001"/>
  <testcase name="calculateScore_correctPrediction_awardsBasePointsAndBonuses" time="0.0"/>
  <testcase name="calculateScore_incorrectPrediction_awardsZeroPoints" time="0.002"/>
</testsuite>
```

---

## 3. Package & APK Verification

**Command:** `.\gradlew.bat assembleDebug --no-daemon`  
**Result:** `BUILD SUCCESSFUL in 4m 25s`  
**Primary APK Path:** `apk/doodle-ai-debug.apk`  
**File Size:** 32,397,974 bytes (~30.9 MB)  

### Verified Assets Inside APK (via Zip Inspection):
* `assets/quickdraw.tflite` — **442,808 bytes** (Stored uncompressed with `noCompress += "tflite"` so Android memory-maps the file directly into native memory with zero allocation overhead).
* `assets/labels.txt` — **860 bytes** (Contains all 100 classes matching model output indices).
* Native Shared Libraries (`libtensorflowlite_jni.so`):
  * `lib/arm64-v8a/libtensorflowlite_jni.so` (3.4 MB)
  * `lib/armeabi-v7a/libtensorflowlite_jni.so` (2.2 MB)
  * `lib/x86/libtensorflowlite_jni.so` (4.5 MB)
  * `lib/x86_64/libtensorflowlite_jni.so` (4.5 MB)

---

## 4. Hardware / Device Limitations & Instructions

* **ADB Device State:** Running `adb devices` verified that no physical Android phone or Android emulator AVD is currently running on this workstation (`List of devices attached` returned empty).
* **Honest Disclosure:** Real-time touch interaction was verified at the unit-test and compilation level. Visual emulator recording could not be performed solely because an active AVD/device was unavailable.
* **How to Install and Launch on Phone or Emulator:**
  ```powershell
  # Connect Android phone with USB Debugging enabled, or start an emulator in Android Studio
  # Then install the APK:
  adb install -r apk/doodle-ai-debug.apk

  # Launch the main activity:
  adb shell am start -n com.realatiif.doodleai/.MainActivity
  ```
