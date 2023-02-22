package com.doubleclick.pizzastation.android.views.HiveLayoutManger

internal class HivePositionInfo {
    var mFloor = 0
    var mPosition = 0

    constructor() {}
    constructor(floor: Int, position: Int) {
        mFloor = floor
        mPosition = position
    }

    override fun toString(): String {
        return "HivePositionInfo{" +
                "mFloor=" + mFloor +
                ", mPosition=" + mPosition +
                '}'
    }
}
