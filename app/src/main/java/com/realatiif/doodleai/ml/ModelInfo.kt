package com.realatiif.doodleai.ml

/**
 * Metadata and specifications for the on-device AI drawing classifier.
 */
object ModelInfo {
    const val MODEL_FILENAME = "quickdraw.tflite"
    const val LABELS_FILENAME = "labels.txt"
    const val INPUT_WIDTH = 28
    const val INPUT_HEIGHT = 28
    const val INPUT_CHANNELS = 1
    const val NUM_CLASSES = 100
    const val MODEL_ARCHITECTURE = "Convolutional Neural Network (3 Conv2D + 3 MaxPool + Dense + Softmax)"
    const val DATASET_SOURCE = "Google Quick, Draw! Dataset (Creative Commons Attribution 4.0)"
    const val INFERENCE_ENGINE = "TensorFlow Lite Runtime (on-device local inference)"
    const val ACCURACY = "93% top-5 accuracy on Quick, Draw! test split"
    const val PRIVACY_STATEMENT = "100% offline. No sketches, personal data, or network requests leave your device."
}
