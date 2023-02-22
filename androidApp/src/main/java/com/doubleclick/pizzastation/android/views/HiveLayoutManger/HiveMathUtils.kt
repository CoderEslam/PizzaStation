package com.doubleclick.pizzastation.android.views.HiveLayoutManger

import android.graphics.PointF
import android.graphics.Rect
import android.graphics.RectF
import android.util.Log
import com.doubleclick.pizzastation.android.views.HiveLayoutManger.HiveConstants.HorizontalNumber
import com.doubleclick.pizzastation.android.views.HiveLayoutManger.HiveConstants.VerticalNumber


internal class HiveMathUtils : IHiveMathUtils {
    override fun calculateItemBounds(current: RectF, number: Int, length: Float): RectF? {
        val pointF =
            calculateCenterPoint(PointF(current.centerX(), current.centerY()), number, length)
        val width = current.width()
        val height = current.height()
        val left = pointF!!.x - width / 2
        val right = pointF.x + width / 2
        val top = pointF.y - height / 2
        val bottom = pointF.y + height / 2
        return RectF(left, top, right, bottom)
    }

    override fun calculateCenterPoint(current: PointF, number: Int, length: Float): PointF? {
        val distance = getDistanceOfNeighbourCenter(length)
        val x = distance * Math.cos(number * Math.PI / 6)
        val y = distance * Math.sin(number * Math.PI / 6)
        val result = clone(current)
        result!!.offset(x.toFloat(), y.toFloat())
        return result
    }

    override fun getDistanceOfNeighbourCenter(length: Float): Double {
        return length * Math.sqrt(3.0)
    }

    override fun clone(pointF: PointF): PointF? {
        val result = PointF()
        result[pointF.x] = pointF.y
        return result
    }

    @VerticalNumber
    override fun getVerticalNumber(i: Int): Int {
        return when (i) {
            0 -> HiveConstants.VERTICAL_ONE
            1 -> HiveConstants.VERTICAL_TWO
            2 -> HiveConstants.VERTICAL_THREE
            3 -> HiveConstants.VERTICAL_FOUR
            4 -> HiveConstants.VERTICAL_FIVE
            5 -> HiveConstants.VERTICAL_SIX
            else -> throw IllegalArgumentException("i must >=0 and <6.")
        }
    }

    @HorizontalNumber
    override fun getHorizontalNumber(i: Int): Int {
        return when (i) {
            0 -> HiveConstants.HORIZONTAL_ONE
            1 -> HiveConstants.HORIZONTAL_TWO
            2 -> HiveConstants.HORIZONTAL_THREE
            3 -> HiveConstants.HORIZONTAL_FOUR
            4 -> HiveConstants.HORIZONTAL_FIVE
            5 -> HiveConstants.HORIZONTAL_SIX
            else -> throw IllegalArgumentException("i must >=0 and <6.")
        }
    }

    override fun getFloorOfPosition(position: Int): HivePositionInfo? {
        var position = position
        return if (position < 0) {
            throw IllegalArgumentException("mPosition must be >= 0")
        } else if (position == 0) {
            HivePositionInfo(0, 0)
        } else {
            var i = 0
            position -= 1 //减去第0层的一个
            var number: Int
            do {
                i++
                number = getNumberOfFloor(i)
                position -= number
            } while (position >= 0)
            HivePositionInfo(i, position + number)
        }
    }

    override fun getNumberOfFloor(floor: Int): Int {
        return if (floor < 0) {
            throw IllegalArgumentException("mFloor must be >= 0")
        } else if (floor == 0) {
            1
        } else {
            floor * 6
        }
    }

    override fun calculateLength(
        rectF: RectF,
        @HiveLayoutManager.Orientation orientation: Int
    ): Float {
        return if (orientation == HiveLayoutManager.HORIZONTAL) {
            rectF.width() / 2
        } else {
            rectF.height() / 2
        }
    }

    override fun calculateLength(
        rect: Rect,
        @HiveLayoutManager.Orientation orientation: Int
    ): Float {
        return calculateLength(
            RectF(
                rect.left.toFloat(), rect.top.toFloat(),
                rect.right.toFloat(), rect.bottom.toFloat()
            ), orientation
        )
    }

    override fun getRectListOfFloor(
        lastFloorRects: List<RectF?>,
        length: Float,
        floor: Int,
        @HiveLayoutManager.Orientation orientation: Int
    ): List<RectF?> {
        Log.d(TAG, String.format("getRectListOfFloor: length : %f, mFloor : %d", length, floor))
        return if (floor <= 0) {
            throw IllegalArgumentException("mFloor must > 0 .")
        } else if (floor == 1) { //第一层特殊处理
            val result: MutableList<RectF?> = ArrayList()
            for (i in 0..5) {
                result.add(
                    calculateItemBounds(
                        lastFloorRects[0]!!,
                        getNumber(i, orientation),
                        length
                    )
                )
            }
            result
        } else { // 2~N层采用下面的方法
            val lastFloor = floor - 1
            val result: MutableList<RectF?> = ArrayList()
            val number = getNumberOfFloor(lastFloor)
            for (i in 0 until number) {
                if (isCorner(lastFloor, i)) {
                    result.addAll(
                        getNextRectListOfCorner(
                            lastFloorRects[i]!!,
                            lastFloor,
                            length,
                            i,
                            orientation
                        )
                    )
                } else {
                    result.add(
                        getNextRectOfMiddle(
                            lastFloorRects[i]!!,
                            lastFloor,
                            length,
                            i,
                            orientation
                        )
                    )
                }
            }
            result
        }
    }

    private fun getNextRectListOfCorner(
        cornerRectF: RectF,
        cornerFloor: Int,
        length: Float,
        index: Int,
        @HiveLayoutManager.Orientation orientation: Int
    ): List<RectF?> {
        val result: MutableList<RectF?> = ArrayList()
        for (i in 0..1) {
            val number = getNumber((i + index / cornerFloor) % 6, orientation)
            result.add(calculateItemBounds(cornerRectF, number, length))
        }
        return result
    }

    private fun getNextRectOfMiddle(
        middleRectF: RectF,
        middleFloor: Int,
        length: Float,
        index: Int,
        @HiveLayoutManager.Orientation orientation: Int
    ): RectF? {
        val number = getNumber((index / middleFloor + 1) % 6, orientation)
        return calculateItemBounds(middleRectF, number, length)
    }

    private fun isCorner(floor: Int, index: Int): Boolean {
        require(!(floor < 0 || index < 0)) { "mFloor and index must >= 0" }
        return index % floor == 0
    }

    private fun getNumber(index: Int, @HiveLayoutManager.Orientation orientation: Int): Int {
        return if (orientation == HiveLayoutManager.HORIZONTAL) {
            getHorizontalNumber(index)
        } else if (orientation == HiveLayoutManager.VERTICAL) {
            getVerticalNumber(index)
        } else {
            throw IllegalArgumentException("orientation must be HiveLayoutManager.VERTICAL or HiveLayoutManager.HORIZONTAL")
        }
    }

    override fun getTheLeftSideIndexOfTheFloor(
        floor: Int,
        @HiveLayoutManager.Orientation orientation: Int,
        maxPosition: Int
    ): Int {
        return if (orientation == HiveLayoutManager.VERTICAL) {
            val temp = floor * 3
            if (temp > maxPosition) maxPosition else temp
        } else if (orientation == HiveLayoutManager.HORIZONTAL) {
            if (floor > maxPosition) maxPosition else floor
        } else {
            throw IllegalArgumentException("orientation must be HiveLayoutManager.VERTICAL or HiveLayoutManager.HORIZONTAL")
        }
    }

    override fun getTheRightSideIndexOfTheFloor(
        floor: Int,
        @HiveLayoutManager.Orientation orientation: Int,
        maxPosition: Int
    ): Int {
        return if (orientation == HiveLayoutManager.VERTICAL) {
            0
        } else if (orientation == HiveLayoutManager.HORIZONTAL) {
            if (floor * 3 > maxPosition) 0 else if (floor * 4 > maxPosition) maxPosition else floor * 4
        } else {
            throw IllegalArgumentException("orientation must be HiveLayoutManager.VERTICAL or HiveLayoutManager.HORIZONTAL")
        }
    }

    override fun getTheTopSideIndexOfTheFloor(
        floor: Int,
        @HiveLayoutManager.Orientation orientation: Int,
        maxPosition: Int
    ): Int {
        return if (orientation == HiveLayoutManager.VERTICAL) {
            if (floor * 3 > maxPosition) 0 else if (floor * 4 > maxPosition) maxPosition else floor * 4
        } else if (orientation == HiveLayoutManager.HORIZONTAL) {
            val temp = floor * 3
            if (temp > maxPosition) maxPosition else temp
        } else {
            throw IllegalArgumentException("orientation must be HiveLayoutManager.VERTICAL or HiveLayoutManager.HORIZONTAL")
        }
    }

    override fun getTheBottomSideIndexOfTheFloor(
        floor: Int,
        @HiveLayoutManager.Orientation orientation: Int,
        maxPosition: Int
    ): Int {
        return if (orientation == HiveLayoutManager.VERTICAL) {
            if (floor > maxPosition) maxPosition else floor
        } else if (orientation == HiveLayoutManager.HORIZONTAL) {
            0
        } else {
            throw IllegalArgumentException("orientation must be HiveLayoutManager.VERTICAL or HiveLayoutManager.HORIZONTAL")
        }
    }

    companion object {
        private val TAG = HiveMathUtils::class.java.simpleName
        val instance: IHiveMathUtils
            get() = HiveMathUtils()
    }
}

