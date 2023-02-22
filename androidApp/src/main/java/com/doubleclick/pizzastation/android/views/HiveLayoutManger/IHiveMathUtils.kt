package com.doubleclick.pizzastation.android.views.HiveLayoutManger

import android.graphics.PointF
import android.graphics.Rect
import android.graphics.RectF
import com.doubleclick.pizzastation.android.views.HiveLayoutManger.HiveConstants.HorizontalNumber
import com.doubleclick.pizzastation.android.views.HiveLayoutManger.HiveConstants.VerticalNumber


internal interface IHiveMathUtils {
    /**
     * calculate a item's bounds that beside of current item .
     *
     * @param current current item bounds
     * @param number  the public edge number
     * @param length  the length of regular hexagon
     * @return the item bounds
     */
    fun calculateItemBounds(current: RectF, number: Int, length: Float): RectF?

    /**
     * calculate a item's center point that beside of current item .
     *
     * @param current current item center point
     * @param number  the public edge number
     * @param length  the length of regular hexagon
     * @return the center point
     */
    fun calculateCenterPoint(current: PointF, number: Int, length: Float): PointF?

    /**
     * get the distance of neighbour item' center point.
     *
     * @param length the length of regular hexagon
     * @return the distance of neighbour item' center point
     */
    fun getDistanceOfNeighbourCenter(length: Float): Double

    /**
     * clone a PointF object from current
     *
     * @param pointF resource
     * @return
     */
    fun clone(pointF: PointF): PointF?

    /**
     * get [jack.hive.HiveConstants.VerticalNumber] from the edge number
     *
     * @param i edge number
     * @return the [jack.hive.HiveConstants.VerticalNumber]
     */
    @VerticalNumber
    fun getVerticalNumber(i: Int): Int

    /**
     * get [jack.hive.HiveConstants.HorizontalNumber] from the edge number
     *
     * @param i edge number
     * @return the [jack.hive.HiveConstants.HorizontalNumber]
     */
    @HorizontalNumber
    fun getHorizontalNumber(i: Int): Int

    /**
     * calculate the mFloor of the mPosition
     *
     * @param position item mPosition
     * @return the mFloor
     */
    fun getFloorOfPosition(position: Int): HivePositionInfo?

    /**
     * calculate all item number int the mFloor
     *
     * @param floor mFloor
     * @return number of the mFloor
     */
    fun getNumberOfFloor(floor: Int): Int

    /**
     * calculate the Length of regular hexagon
     *
     * @param rectF       the item view bounds
     * @param orientation the layout orientation
     * @return the length of regular hexagon
     */
    fun calculateLength(rectF: RectF, @HiveLayoutManager.Orientation orientation: Int): Float

    /**
     * calculate the Length of regular hexagon
     *
     * @param rect        the item view bounds
     * @param orientation the layout orientation
     * @return the length of regular hexagon
     */
    fun calculateLength(rect: Rect, @HiveLayoutManager.Orientation orientation: Int): Float

    /**
     * calculate current mFloor Rects by last mFloor
     *
     * @param lastFloorRects Rects at last mFloor
     * @param length         the length of regular hexagon
     * @param floor          last mFloor
     * @param orientation    orientation [jack.hive.HiveLayoutManager.Orientation]
     * @return RectFs at current mFloor
     */
    fun getRectListOfFloor(
        lastFloorRects: List<RectF?>,
        length: Float,
        floor: Int,
        @HiveLayoutManager.Orientation orientation: Int
    ): List<RectF?>?

    fun getTheLeftSideIndexOfTheFloor(
        floor: Int,
        @HiveLayoutManager.Orientation orientation: Int,
        maxPosition: Int
    ): Int

    fun getTheRightSideIndexOfTheFloor(
        floor: Int,
        @HiveLayoutManager.Orientation orientation: Int,
        maxPosition: Int
    ): Int

    fun getTheTopSideIndexOfTheFloor(
        floor: Int,
        @HiveLayoutManager.Orientation orientation: Int,
        maxPosition: Int
    ): Int

    fun getTheBottomSideIndexOfTheFloor(
        floor: Int,
        @HiveLayoutManager.Orientation orientation: Int,
        maxPosition: Int
    ): Int
}
