package com.doubleclick.pizzastation.android.views.curvedBottomNavigation

import android.content.Context
import android.util.DisplayMetrics
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat

/**
 * Created by suson on 10/1/20
 */

fun Int.toPx(context: Context) = (this * context.resources.displayMetrics.densityDpi) / DisplayMetrics.DENSITY_DEFAULT

fun Context.getColorRes(@ColorRes colorId: Int) = ContextCompat.getColor(this, colorId)