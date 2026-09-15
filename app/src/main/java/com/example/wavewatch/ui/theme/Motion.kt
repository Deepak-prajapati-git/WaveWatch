package com.example.wavewatch.ui.theme

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.ui.unit.dp

object MotionTokens {
    val DurationFast = 200
    val DurationMedium = 500
    val DurationSlow = 1500

    val StandardEasing = CubicBezierEasing(0.4f, 0.0f, 0.2f, 1.0f)
    val EmphasizedEasing = CubicBezierEasing(0.2f, 0.0f, 0.0f, 1.0f)

    val fastTween = tween<Float>(durationMillis = DurationFast, easing = StandardEasing)
    val mediumTween = tween<Float>(durationMillis = DurationMedium, easing = StandardEasing)
    val slowTween = tween<Float>(durationMillis = DurationSlow, easing = EmphasizedEasing)
}
