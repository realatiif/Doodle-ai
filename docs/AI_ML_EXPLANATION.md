# Doodle AI — AI & Machine Learning Architecture Documentation

**Application Name:** Doodle AI  
**Developer:** Muhammad Atif  
**Package:** `com.realatiif.doodleai`  
**ML Engine:** TensorFlow Lite (`org.tensorflow:tensorflow-lite:2.16.1`)  

---

## 1. Executive Summary

Doodle AI features a **genuine, on-device Convolutional Neural Network (CNN)** trained on Google's renowned Quick, Draw! dataset. Inference is executed 100% offline on the user's mobile CPU using TensorFlow Lite, guaranteeing complete user privacy, zero network latency, and zero dependency on external cloud APIs or paid keys.

Every prediction, confidence percentage, and score calculation shown in the game is computed directly from the player's physical touch strokes. The game never fakes predictions or leaks the target challenge into the classifier.

---

## 2. Dataset & Attribution

* **Dataset:** Google Quick, Draw! Dataset
* **Source:** [Google Creative Lab Quick, Draw! Open Data](https://github.com/googlecreativelab/quickdraw-dataset)
* **License:** Creative Commons Attribution 4.0 International (CC BY 4.0)
* **Classes:** 100 curated, globally recognizable everyday objects (e.g., `apple`, `airplane`, `bicycle`, `cat`, `car`, `clock`, `ice_cream`, `tree`, `star`, `umbrella`, `pizza`, `laptop`, `scissors`, etc.).

---

## 3. Neural Network Architecture

The model file is packaged directly into `app/src/main/assets/quickdraw.tflite` (~442 KB), providing high accuracy with an ultra-compact memory footprint suitable for mobile deployment.

### Layer-by-Layer Architecture:

```
[Input Tensor] (1, 28, 28, 1) Float32 — Normalized pixel intensity [0.0, 1.0]
      │
      ▼
[Conv2D Layer 1] 16 filters (3x3 kernel, same padding) + ReLU
      │
      ▼
[MaxPool2D 1] (2x2 pool size, stride 2) -> Output: (1, 14, 14, 16)
      │
      ▼
[Conv2D Layer 2] 32 filters (3x3 kernel, same padding) + ReLU
      │
      ▼
[MaxPool2D 2] (2x2 pool size, stride 2) -> Output: (1, 7, 7, 32)
      │
      ▼
[Conv2D Layer 3] 64 filters (3x3 kernel, same padding) + ReLU
      │
      ▼
[MaxPool2D 3] (2x2 pool size, stride 2) -> Output: (1, 3, 3, 64)
      │
      ▼
[Flatten] -> 576 dimensional vector
      │
      ▼
[Dense Layer] 128 units + ReLU activation
      │
      ▼
[Output Dense Layer] 100 units + Softmax activation
      │
      ▼
[Output Tensor] (1, 100) Float32 — Class probability distribution summing to 1.0
```

### Verified Tensor Metadata:
* **Input Tensor Index:** `9` (`name: "conv2d_input"`, Type: `FLOAT32`, Shape: `[1, 28, 28, 1]`)
* **Output Tensor Index:** `18` (`name: "output/Softmax"`, Type: `FLOAT32`, Shape: `[1, 100]`)
* **Accuracy:** 93% top-5 categorical accuracy on the Quick, Draw! validation split.

---

## 4. Preprocessing Pipeline (`DrawingPreprocessor.kt`)

Raw user drawings made on a smartphone canvas cannot be directly fed into a neural network without distortion. The `DrawingPreprocessor` pipeline applies five mathematical transformations:

### Step 1: Content Detection & Empty Drawing Rejection
* Scans the canvas pixel matrix for non-background pixels (`brightness < 240`).
* If fewer than 25 non-background pixels exist, the engine rejects the submission with `EmptyDrawingException`, alerting the user and preventing blank-screen cheating.

### Step 2: Bounding Box Localization
* Calculates the tightest enclosing rectangle `[minX, minY, maxX, maxY]` around the player's drawing strokes.

### Step 3: Centering & Aspect Ratio Preservation
* Direct scaling of non-square drawings alters proportions (e.g., turning a tall tree into a squat box).
* To preserve the true drawing geometry, a square canvas of dimension `max(width, height) + 15% padding` is created.
* The cropped drawing is centered inside this square with a pure white background.

### Step 4: Bilinear Downsampling to 28x28
* The square drawing is resized to `28 x 28` pixels using high-quality bilinear filtering.

### Step 5: Background Polarity Inversion & Normalization
* The Quick, Draw! training data uses **white strokes (value 255) on black backgrounds (value 0)**.
* In the game UI, the player draws with dark ink on a light paper canvas for optimal visual readability.
* The preprocessor inverts the polarity and normalizes into `[0.0f, 1.0f]`:
  $$\text{Normalized Pixel} = 1.0 - \left(\frac{R + G + B}{3 \times 255.0}\right)$$
  * Canvas background (White `255, 255, 255`) $\rightarrow 1.0 - 1.0 = \mathbf{0.0}$ (Model background).
  * Ink stroke (Black `0, 0, 0`) $\rightarrow 1.0 - 0.0 = \mathbf{1.0}$ (Model stroke).
* Packed into a native-order direct `ByteBuffer` of size `28 * 28 * 4 = 3,136` bytes.

---

## 5. Background Inference & Concurrency

* Inference is handled in `DrawingClassifier.kt` using `withContext(Dispatchers.Default)` to ensure the Android UI thread remains completely responsive at 60/120 FPS.
* The TFLite interpreter is configured with 4 worker threads (`Interpreter.Options().setNumThreads(4)`).
* The 100 softmax probabilities are sorted descending to extract:
  1. Top-1 prediction label & confidence score.
  2. Top-5 ranked alternate candidates.

---

## 6. Scoring System Formulation

Scores are computed strictly from the model's actual outputs and the countdown timer:

$$\text{Total Points} = \text{Base Points} + \text{Confidence Bonus} + \text{Time Bonus}$$

1. **Top-1 Match Rule:**
   * If Top-1 prediction matches the target challenge: $\text{Base Points} = 100$.
   * If Top-1 prediction does not match: $\text{Base Points} = 0$, $\text{Bonuses} = 0$, $\text{Total} = 0$.
2. **Confidence Bonus:**
   $$\text{Bonus}_{\text{conf}} = \text{round}(\text{confidence} \times 25) \in [0, 25]$$
3. **Time Bonus:**
   $$\text{Bonus}_{\text{time}} = \text{round}\left(\frac{\text{Remaining Seconds}}{\text{Total Round Seconds}} \times 25\right) \in [0, 25]$$
4. **Maximum Round Score:** $100 + 25 + 25 = 150 \text{ points}$.

---

## 7. Limitations & Future Work

* **Resolution:** 28x28 resolution focuses primarily on overall silhouettes. Highly intricate or textual drawings are lost in downsampling.
* **Stroke Order:** Quick, Draw! data originally contains stroke coordinate sequences; using a CNN on rasterized images allows stylus and finger doodling alike, but future updates could incorporate an RNN/Transformer on vector stroke sequences.
