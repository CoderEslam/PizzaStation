package com.doubleclick.pizzastation.android.views.shinebutton

import android.animation.ValueAnimator
import android.graphics.Canvas
import com.doubleclick.pizzastation.android.views.EasingInterpolator.Ease
import com.doubleclick.pizzastation.android.views.EasingInterpolator.EasingInterpolator


/**
 * Created By Eslam Ghazy on 1/14/2023
 */
class ShineAnimator : ValueAnimator {
    var MAX_VALUE = 1.5f
    var ANIM_DURATION: Long = 1500
    var canvas: Canvas? = null
    internal constructor() {
        setFloatValues(1f, MAX_VALUE)
        duration = ANIM_DURATION
        startDelay = 200
        interpolator = EasingInterpolator(Ease.QUART_OUT)
    }

    internal constructor(duration: Long, max_value: Float, delay: Long) {
        setFloatValues(1f, max_value)
        setDuration(duration)
        startDelay = delay
        interpolator = EasingInterpolator(Ease.QUART_OUT)
    }

    fun startAnim() {
        start()
    }

    @JvmName("setCanvas1")
    fun setCanvas(canvas: Canvas) {
        this.canvas = canvas
    }
}
