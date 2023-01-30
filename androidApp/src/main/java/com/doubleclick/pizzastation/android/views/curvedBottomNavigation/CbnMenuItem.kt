package com.doubleclick.pizzastation.android.views.curvedBottomNavigation

import androidx.annotation.DrawableRes
import androidx.annotation.IdRes

/**
 * Created by suson on 10/1/20
 */
data class CbnMenuItem(
    @DrawableRes
    val icon: Int,
    @DrawableRes
    val avdIcon: Int,
    @IdRes
    val destinationId: Int = -1
)