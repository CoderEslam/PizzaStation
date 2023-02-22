package com.doubleclick.pizzastation.android.views.HiveLayoutManger

import androidx.annotation.IntDef


internal object HiveConstants {
    const val VERTICAL_ONE = 0
    const val VERTICAL_TWO = 2
    const val VERTICAL_THREE = 4
    const val VERTICAL_FOUR = 6
    const val VERTICAL_FIVE = 8
    const val VERTICAL_SIX = 10
    const val HORIZONTAL_ONE = 3
    const val HORIZONTAL_TWO = 5
    const val HORIZONTAL_THREE = 7
    const val HORIZONTAL_FOUR = 9
    const val HORIZONTAL_FIVE = 11
    const val HORIZONTAL_SIX = 1

    @IntDef(VERTICAL_ONE, VERTICAL_TWO, VERTICAL_THREE, VERTICAL_FOUR, VERTICAL_FIVE, VERTICAL_SIX)
    internal annotation class VerticalNumber

    @IntDef(
        HORIZONTAL_ONE,
        HORIZONTAL_TWO,
        HORIZONTAL_THREE,
        HORIZONTAL_FOUR,
        HORIZONTAL_SIX,
        HORIZONTAL_FIVE
    )
    internal annotation class HorizontalNumber
}
