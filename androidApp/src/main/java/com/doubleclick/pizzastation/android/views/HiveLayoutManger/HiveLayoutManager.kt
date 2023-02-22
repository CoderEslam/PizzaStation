package com.doubleclick.pizzastation.android.views.HiveLayoutManger

import android.graphics.PointF
import android.graphics.RectF
import android.util.Log
import android.view.ViewGroup
import androidx.annotation.IntDef
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler

class HiveLayoutManager(@param:Orientation private val mOrientation: Int) :
    RecyclerView.LayoutManager() {
    /**
     * the HiveLayoutManager's orientation
     *
     * @see .VERTICAL
     *
     * @see .HORIZONTAL
     */
    @IntDef(HORIZONTAL, VERTICAL)
    internal annotation class Orientation

    private var mHiveMathUtils: IHiveMathUtils? = null
    private var mAnchorInfo: AnchorInfo? = null
    private var mLayoutState: LayoutState? = null
    private val mFloors: MutableList<List<RectF?>?> = ArrayList()
    private val mBooleanMap = HiveBucket()

    /**
     * set the Gravity for the item.
     *
     * @param gravity
     * @see .ALIGN_LEFT
     *
     * @see .ALIGN_TOP
     *
     * @see .ALIGN_RIGHT
     *
     * @see .ALIGN_BOTTOM
     *
     * @see .CENTER
     *
     *
     * you can use them combined, like setGravity
     */
    var gravity = CENTER
    private var firstLayout = true
    val padding = RectF()

    /**
     * @param orientation the LayoutManager orientation.
     * @see .VERTICAL
     *
     * @see .HORIZONTAL
     */
    init {
        init()
    }

    private fun init() {
        mHiveMathUtils = HiveMathUtils.instance
        mLayoutState = LayoutState()
        mBooleanMap.reset()
    }

    /**
     * set the layout manager padding.if you need recyclerView have a padding,you can use this to
     * solve the problem that item cannot disappear from the edge of view.
     *
     * @param paddingLeft
     * @param paddingTop
     * @param paddingRight
     * @param paddingBottom
     */
    fun setPadding(
        paddingLeft: Float,
        paddingTop: Float,
        paddingRight: Float,
        paddingBottom: Float
    ) {
        padding[paddingLeft, paddingTop, paddingRight] = paddingBottom
    }

    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        return RecyclerView.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onLayoutChildren(recycler: Recycler, state: RecyclerView.State) {
        detachAndScrapAttachedViews(recycler)
        val itemCount = state.itemCount
        if (itemCount <= 0) {
            return
        }
        initAnchorInfo(recycler)
        initFloors()
        initEdgeDistance()
        detachAndScrapAttachedViews(recycler)
        mBooleanMap.reset()
        fill(recycler, state)
        firstLayout = false
    }

    private fun initEdgeDistance() {
        mLayoutState!!.edgeDistance[(width / 2).toFloat(), (height / 2).toFloat(), (width / 2).toFloat()] =
            (height / 2).toFloat()
    }

    private fun updateLayoutState() {
        mLayoutState!!.containerRect[0f, 0f, width.toFloat()] = height.toFloat()
        mLayoutState!!.containerRect.offset(
            -mLayoutState!!.offsetX.toFloat(),
            -mLayoutState!!.offsetY.toFloat()
        )
    }

    private fun fill(recycler: Recycler, state: RecyclerView.State) {
        val itemCount = state.itemCount
        if (itemCount <= 0) {
            return
        }
        checkAllRect(itemCount)
        updateLayoutState()
        if (firstLayout) {
            firstLayout = false
            if (gravity and CENTER == 0) { // 不是Center的时候
                if (gravity and ALIGN_LEFT != 0) { // 如果是从左对其
                    doScrollHorizontalBx(
                        recycler,
                        state,
                        mLayoutState!!.outLineRect.left - padding.left
                    )
                } else if (gravity and ALIGN_RIGHT != 0) {
                    doScrollHorizontalBx(
                        recycler,
                        state,
                        mLayoutState!!.outLineRect.right - width + padding.right
                    )
                }
                if (gravity and ALIGN_TOP != 0) {
                    doScrollVerticalBy(
                        recycler,
                        state,
                        mLayoutState!!.outLineRect.top - padding.top
                    )
                } else if (gravity and ALIGN_BOTTOM != 0) {
                    doScrollVerticalBy(
                        recycler,
                        state,
                        mLayoutState!!.outLineRect.bottom - height + padding.bottom
                    )
                }
            } else {
                fill(recycler, state)
            }
        } else {
            for (i in 0 until itemCount) {
                val bounds = getBounds(i)
                if (!mBooleanMap[i] && RectF.intersects(bounds, mLayoutState!!.containerRect)) {
                    val view = recycler.getViewForPosition(i)
                    addView(view)
                    mBooleanMap.set(i)
                    measureChildWithMargins(view, 0, 0)
                    bounds.offset(
                        mLayoutState!!.offsetX.toFloat(),
                        mLayoutState!!.offsetY.toFloat()
                    )
                    calculateEdgeDistance(bounds.left, bounds.top, bounds.right, bounds.bottom)
                    layoutDecoratedWithMargins(
                        view,
                        bounds.left.toInt(),
                        bounds.top.toInt(),
                        bounds.right.toInt(),
                        bounds.bottom.toInt()
                    )
                }
            }
        }
    }

    private fun calculateEdgeDistance(left: Float, top: Float, right: Float, bottom: Float) {
        val temp = mLayoutState!!.edgeDistance
        val eLeft = Math.min(temp.left, left)
        val eTop = Math.min(temp.top, top)
        val eRight = Math.min(temp.right, width - right)
        val eBottom = Math.min(temp.bottom, height - bottom)
        mLayoutState!!.edgeDistance[eLeft, eTop, eRight] = eBottom
    }

    private fun getBounds(index: Int): RectF {
        val positionInfo = mHiveMathUtils!!.getFloorOfPosition(index)
        val floor = mFloors[positionInfo!!.mFloor]
        return RectF(floor!![positionInfo.mPosition])
    }

    private fun initFloors() {
        if (mFloors.size == 0) {
            val list: MutableList<RectF> = ArrayList()
            list.add(mAnchorInfo!!.anchorRect)
            mFloors.add(list)
        }
    }

    private fun checkAllRect(itemCount: Int) {
        val positionInfo = mHiveMathUtils!!.getFloorOfPosition(itemCount - 1)
        checkFloor(positionInfo!!.mFloor)
    }

    private fun checkFloor(floor: Int) {
        if (floor < 0) {
            return
        }
        if (mFloors.size <= floor) {
            for (i in mFloors.size..floor) {
                val i1 = i - 1
                Log.d(TAG, "checkFloor: i1 : $i1 , i : $i")
                val temp = mHiveMathUtils!!.getRectListOfFloor(
                    mFloors[i1]!!,
                    mAnchorInfo!!.length,
                    i,
                    mOrientation
                )
                mFloors.add(temp)
            }
            loadOutLineRect()
        }
    }

    private fun loadOutLineRect() {
        if (itemCount <= 0) {
            mLayoutState!!.outLineRect[(width / 2).toFloat(), (height / 2).toFloat(), (width / 2).toFloat()] =
                (height / 2).toFloat()
        } else {
            val positionInfo = mHiveMathUtils!!.getFloorOfPosition(itemCount)
            val lastFloorIndex = positionInfo!!.mFloor // 获得最后一层的索引
            if (lastFloorIndex == 0) {
                mLayoutState!!.outLineRect.set(mFloors[0]!![0]!!)
            } else if (lastFloorIndex > 0) {
                val lastSecondFloorIndex = lastFloorIndex - 1
                val lastFloor = mFloors[lastFloorIndex]
                val lastSecondFloor = mFloors[lastSecondFloorIndex]
                val leftF = Math.min(
                    lastSecondFloor!![mHiveMathUtils!!.getTheLeftSideIndexOfTheFloor(
                        lastSecondFloorIndex,
                        mOrientation,
                        lastSecondFloor.size - 1
                    )]!!.left,
                    lastFloor!![mHiveMathUtils!!.getTheLeftSideIndexOfTheFloor(
                        lastFloorIndex,
                        mOrientation,
                        positionInfo.mPosition
                    )]!!.left
                )
                val rightF = Math.max(
                    lastSecondFloor!![mHiveMathUtils!!.getTheRightSideIndexOfTheFloor(
                        lastSecondFloorIndex,
                        mOrientation,
                        lastSecondFloor.size - 1
                    )]!!.right,
                    lastFloor!![mHiveMathUtils!!.getTheRightSideIndexOfTheFloor(
                        lastFloorIndex,
                        mOrientation,
                        positionInfo.mPosition
                    )]!!.right
                )
                val topF = Math.min(
                    lastSecondFloor!![mHiveMathUtils!!.getTheTopSideIndexOfTheFloor(
                        lastSecondFloorIndex,
                        mOrientation,
                        lastSecondFloor.size - 1
                    )]!!.top,
                    lastFloor!![mHiveMathUtils!!.getTheTopSideIndexOfTheFloor(
                        lastFloorIndex,
                        mOrientation,
                        positionInfo.mPosition
                    )]!!.top
                )
                val bottomF = Math.max(
                    lastSecondFloor!![mHiveMathUtils!!.getTheBottomSideIndexOfTheFloor(
                        lastSecondFloorIndex,
                        mOrientation,
                        lastSecondFloor.size - 1
                    )]!!.bottom,
                    lastFloor!![mHiveMathUtils!!.getTheBottomSideIndexOfTheFloor(
                        lastFloorIndex,
                        mOrientation,
                        positionInfo.mPosition
                    )]!!.bottom
                )
                mLayoutState!!.outLineRect[leftF, topF, rightF] = bottomF
            }
        }
        Log.d(TAG, String.format("loadOutLineRect: out line rect:%s", mLayoutState!!.outLineRect))
    }

    private fun initAnchorInfo(recycler: Recycler) {
        if (mAnchorInfo == null) {
            mAnchorInfo = AnchorInfo()
            mAnchorInfo!!.anchorPoint[(width / 2).toFloat()] = (height / 2).toFloat()
            val view = recycler.getViewForPosition(0)
            addView(view)
            measureChildWithMargins(view, 0, 0)
            val height = view.measuredHeight
            val width = view.measuredWidth
            val left = mAnchorInfo!!.anchorPoint.x - width / 2f
            val right = mAnchorInfo!!.anchorPoint.x + width / 2f
            val top = mAnchorInfo!!.anchorPoint.y - height / 2f
            val bottom = mAnchorInfo!!.anchorPoint.y + height / 2f
            mAnchorInfo!!.anchorRect[left, top, right] = bottom
            mAnchorInfo!!.length =
                mHiveMathUtils!!.calculateLength(mAnchorInfo!!.anchorRect, mOrientation)
        }
    }

    override fun scrollHorizontallyBy(dx: Int, recycler: Recycler, state: RecyclerView.State): Int {
        return if (mLayoutState!!.edgeDistance.left < padding.left && dx < 0 || mLayoutState!!.edgeDistance.right < padding.right && dx > 0) {
            val distance = if (dx < 0) Math.max(
                mLayoutState!!.edgeDistance.left - padding.left,
                dx.toFloat()
            ) else Math.min(-(mLayoutState!!.edgeDistance.right - padding.right), dx.toFloat())
            doScrollHorizontalBx(recycler, state, distance)
            distance.toInt()
        } else if (mLayoutState!!.offsetX != 0) {
            if (mLayoutState!!.offsetX * dx > 0) {
                val distance = (Math.abs(dx) / dx * Math.min(
                    Math.abs(
                        mLayoutState!!.offsetX
                    ), Math.abs(dx)
                )).toFloat()
                doScrollHorizontalBx(recycler, state, distance)
                distance.toInt()
            } else {
                0
            }
        } else {
            0
        }
    }

    private fun doScrollHorizontalBx(
        recycler: Recycler,
        state: RecyclerView.State,
        distance: Float
    ) {
        mLayoutState!!.edgeDistance.left -= distance
        mLayoutState!!.edgeDistance.right += distance
        offsetChildrenHorizontal(-distance.toInt())
        mLayoutState!!.offsetX -= distance.toInt()
        mLayoutState!!.lastScrollDeltaX = distance.toInt()
        scrapOutSetViews(recycler)
        scrollBy(distance.toInt(), recycler, state)
    }

    private fun scrapOutSetViews(recycler: Recycler) {
        val count = childCount
        for (i in count - 1 downTo 0) {
            val view = getChildAt(i)
            if (!RectF.intersects(
                    RectF(0f, 0f, width.toFloat(), height.toFloat()), RectF(
                        view!!.left.toFloat(), view.top.toFloat(),
                        view.right.toFloat(), view.bottom.toFloat()
                    )
                )
            ) {
                val position = getPosition(view)
                mBooleanMap.clear(position)
                removeAndRecycleViewAt(i, recycler)
            }
        }
    }

    private fun scrollBy(distance: Int, recycler: Recycler, state: RecyclerView.State): Int {
        fill(recycler, state)
        return distance
    }

    override fun scrollVerticallyBy(dy: Int, recycler: Recycler, state: RecyclerView.State): Int {
        return if (mLayoutState!!.edgeDistance.top < padding.top && dy < 0 || mLayoutState!!.edgeDistance.bottom < padding.bottom && dy > 0) {
            val distance = if (dy < 0) Math.max(
                mLayoutState!!.edgeDistance.top - padding.top,
                dy.toFloat()
            ) else Math.min(-(mLayoutState!!.edgeDistance.bottom - padding.bottom), dy.toFloat())
            doScrollVerticalBy(recycler, state, distance)
            distance.toInt()
        } else if (mLayoutState!!.offsetY != 0) {
            if (mLayoutState!!.offsetY * dy > 0) {
                val distance = (Math.abs(dy) / dy * Math.min(
                    Math.abs(
                        mLayoutState!!.offsetY
                    ), Math.abs(dy)
                )).toFloat()
                doScrollVerticalBy(recycler, state, distance)
                distance.toInt()
            } else {
                0
            }
        } else {
            0
        }
    }

    private fun doScrollVerticalBy(recycler: Recycler, state: RecyclerView.State, distance: Float) {
        mLayoutState!!.edgeDistance.top -= distance
        mLayoutState!!.edgeDistance.bottom += distance
        offsetChildrenVertical(-distance.toInt())
        mLayoutState!!.offsetY -= distance.toInt()
        mLayoutState!!.lastScrollDeltaY = distance.toInt()
        scrapOutSetViews(recycler)
        scrollBy(distance.toInt(), recycler, state)
    }

    override fun canScrollHorizontally(): Boolean {
        return true
    }

    override fun canScrollVertically(): Boolean {
        return true
    }

    /**
     * the anchor info
     */
    private inner class AnchorInfo {
        val anchorPoint = PointF()
        val anchorRect = RectF()
        var length = 0f
    }

    /**
     * the layout state of the LayoutManager
     */
    private inner class LayoutState {
        var offsetX = 0
        var offsetY = 0
        var lastScrollDeltaX = 0
        var lastScrollDeltaY = 0
        val edgeDistance = RectF()
        val containerRect = RectF()
        val outLineRect = RectF()
    }

    companion object {
        private val TAG = HiveLayoutManager::class.java.simpleName

        /**
         * layout views in RV an horizontal direction
         */
        const val HORIZONTAL = HiveLayoutHelper.HORIZONTAL

        /**
         * layout views in RV an vertical direction
         */
        const val VERTICAL = HiveLayoutHelper.VERTICAL

        /**
         * at the center of parent at begin
         */
        const val CENTER = 0x00000001

        /**
         * at the left of parent at begin
         */
        const val ALIGN_LEFT = 0x00000002

        /**
         * at the right of parent at begin, if already set ALIGN_LEFT, this will not work
         */
        const val ALIGN_RIGHT = 0x00000004

        /**
         * at the top of parent at begin
         */
        const val ALIGN_TOP = 0x00000008

        /**
         * at the bottom of parent at begin, if already set ALIGN_TOP, this will not work
         */
        const val ALIGN_BOTTOM = 0x00000010
    }
}
