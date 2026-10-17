package com.realatiif.doodleai.ml

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class PreprocessingMathTest {

    @Test
    fun pixelNormalization_invertsPolarityCorrectly() {
        // Quick Draw requires white strokes (1.0f) on black background (0.0f)
        // User draws with black ink (0, 0, 0) on white paper (255, 255, 255)

        // Case 1: Pure white paper (r=255, g=255, b=255)
        val whiteBrightness = (255f + 255f + 255f) / (3f * 255f) // 1.0f
        val whiteNormalizedStroke = (1f - whiteBrightness).coerceIn(0f, 1f)
        assertEquals(0.0f, whiteNormalizedStroke, 0.001f) // Model background = 0.0f

        // Case 2: Pure black stroke (r=0, g=0, b=0)
        val blackBrightness = (0f + 0f + 0f) / (3f * 255f) // 0.0f
        val blackNormalizedStroke = (1f - blackBrightness).coerceIn(0f, 1f)
        assertEquals(1.0f, blackNormalizedStroke, 0.001f) // Model stroke = 1.0f

        // Case 3: Anti-aliased mid-gray edge (r=128, g=128, b=128)
        val grayBrightness = (128f + 128f + 128f) / (3f * 255f)
        val grayNormalizedStroke = (1f - grayBrightness).coerceIn(0f, 1f)
        assertTrue(grayNormalizedStroke in 0.45f..0.55f)
    }

    @Test
    fun targetTensorDimensions_matchModelSpecifications() {
        assertEquals(28, ModelInfo.INPUT_WIDTH)
        assertEquals(28, ModelInfo.INPUT_HEIGHT)
        assertEquals(1, ModelInfo.INPUT_CHANNELS)
        assertEquals(100, ModelInfo.NUM_CLASSES)
    }
}
