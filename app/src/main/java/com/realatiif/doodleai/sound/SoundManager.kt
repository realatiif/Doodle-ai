package com.realatiif.doodleai.sound

import android.content.Context
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Build
import android.os.CombinedVibration
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.util.Log

/**
 * Handles synthesized game audio sound effects and haptic feedback.
 * Operates without external audio assets, ensuring 100% offline portability and zero missing asset risk.
 */
class SoundManager(context: Context) : AutoCloseable {

    companion object {
        private const val TAG = "SoundManager"
    }

    private var toneGenerator: ToneGenerator? = null
    private val vibrator: Vibrator? = try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
            vibratorManager?.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        }
    } catch (e: Exception) {
        null
    }

    init {
        try {
            toneGenerator = ToneGenerator(AudioManager.STREAM_MUSIC, 70)
        } catch (e: Exception) {
            Log.w(TAG, "ToneGenerator could not be initialized: ${e.message}")
        }
    }

    fun playCorrectSound(soundEnabled: Boolean) {
        if (!soundEnabled) return
        try {
            toneGenerator?.startTone(ToneGenerator.TONE_PROP_ACK, 250)
        } catch (e: Exception) {
            Log.w(TAG, "Audio error: ${e.message}")
        }
    }

    fun playIncorrectSound(soundEnabled: Boolean) {
        if (!soundEnabled) return
        try {
            toneGenerator?.startTone(ToneGenerator.TONE_PROP_NACK, 300)
        } catch (e: Exception) {
            Log.w(TAG, "Audio error: ${e.message}")
        }
    }

    fun playSubmitSound(soundEnabled: Boolean) {
        if (!soundEnabled) return
        try {
            toneGenerator?.startTone(ToneGenerator.TONE_PROP_BEEP, 120)
        } catch (e: Exception) {
            Log.w(TAG, "Audio error: ${e.message}")
        }
    }

    fun playGameOverSound(soundEnabled: Boolean) {
        if (!soundEnabled) return
        try {
            toneGenerator?.startTone(ToneGenerator.TONE_CDMA_ALERT_NETWORK_LITE, 400)
        } catch (e: Exception) {
            Log.w(TAG, "Audio error: ${e.message}")
        }
    }

    fun vibrateSuccess(hapticsEnabled: Boolean) {
        if (!hapticsEnabled || vibrator == null) return
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                vibrator.vibrate(100)
            }
        } catch (e: Exception) {
            Log.w(TAG, "Vibration error: ${e.message}")
        }
    }

    fun vibrateError(hapticsEnabled: Boolean) {
        if (!hapticsEnabled || vibrator == null) return
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val pattern = longArrayOf(0, 80, 50, 80)
                vibrator.vibrate(VibrationEffect.createWaveform(pattern, -1))
            } else {
                @Suppress("DEPRECATION")
                vibrator.vibrate(150)
            }
        } catch (e: Exception) {
            Log.w(TAG, "Vibration error: ${e.message}")
        }
    }

    fun vibrateClick(hapticsEnabled: Boolean) {
        if (!hapticsEnabled || vibrator == null) return
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                vibrator.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK))
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(20, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                vibrator.vibrate(20)
            }
        } catch (e: Exception) {
            Log.w(TAG, "Vibration error: ${e.message}")
        }
    }

    override fun close() {
        try {
            toneGenerator?.release()
            toneGenerator = null
        } catch (e: Exception) {
            Log.w(TAG, "Error releasing ToneGenerator: ${e.message}")
        }
    }
}
