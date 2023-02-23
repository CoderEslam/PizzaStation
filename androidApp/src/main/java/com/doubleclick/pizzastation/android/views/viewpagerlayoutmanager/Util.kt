package com.doubleclick.pizzastation.android.views.viewpagerlayoutmanager

import android.content.Context
import java.util.*

object Util {
    fun Dp2px(context: Context, dp: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dp * scale + 0.5f).toInt()
    }

    fun formatFloat(value: Float): String {
        return String.format(Locale.getDefault(), "%.3f", value)
    }
}