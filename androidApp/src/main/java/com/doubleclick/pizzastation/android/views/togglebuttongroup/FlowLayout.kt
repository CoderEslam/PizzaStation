package com.doubleclick.pizzastation.android.views.togglebuttongroup

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.ViewGroup
import com.doubleclick.pizzastation.android.R

/**
 * Created By Eslam Ghazy on 11/20/2022
 */
open class FlowLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    ViewGroup(context, attrs) {
    private var mFlow = DEFAULT_FLOW
    private var mChildSpacing = DEFAULT_CHILD_SPACING
    private var mChildSpacingForLastRow = DEFAULT_CHILD_SPACING_FOR_LAST_ROW
    private var mRowSpacing = DEFAULT_ROW_SPACING
    private var mAdjustedRowSpacing = DEFAULT_ROW_SPACING
    private var mRtl = DEFAULT_RTL
    private val mHorizontalSpacingForRow: MutableList<Float> = ArrayList()
    private val mHeightForRow: MutableList<Int> = ArrayList()
    private val mChildNumForRow: MutableList<Int> = ArrayList()

    init {
        val a = context.theme.obtainStyledAttributes(
            attrs, R.styleable.FlowLayout, 0, 0
        )
        try {
            mFlow = a.getBoolean(R.styleable.FlowLayout_tbgFlow, DEFAULT_FLOW)
            mChildSpacing = try {
                a.getInt(R.styleable.FlowLayout_tbgChildSpacing, DEFAULT_CHILD_SPACING)
            } catch (e: NumberFormatException) {
                a.getDimensionPixelSize(
                    R.styleable.FlowLayout_tbgChildSpacing, dpToPx(
                        DEFAULT_CHILD_SPACING.toFloat()
                    ).toInt()
                )
            }
            mChildSpacingForLastRow = try {
                a.getInt(R.styleable.FlowLayout_tbgChildSpacingForLastRow, SPACING_UNDEFINED)
            } catch (e: NumberFormatException) {
                a.getDimensionPixelSize(
                    R.styleable.FlowLayout_tbgChildSpacingForLastRow, dpToPx(
                        DEFAULT_CHILD_SPACING.toFloat()
                    ).toInt()
                )
            }
            mRowSpacing = try {
                a.getInt(R.styleable.FlowLayout_tbgRowSpacing, 0).toFloat()
            } catch (e: NumberFormatException) {
                a.getDimension(R.styleable.FlowLayout_tbgRowSpacing, dpToPx(DEFAULT_ROW_SPACING))
            }
            mRtl = a.getBoolean(R.styleable.FlowLayout_tbgRtl, DEFAULT_RTL)
        } finally {
            a.recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        mHorizontalSpacingForRow.clear()
        mChildNumForRow.clear()
        mHeightForRow.clear()
        var measuredHeight = 0
        var measuredWidth = 0
        val childCount = childCount
        var rowWidth = 0
        var maxChildHeightInRow = 0
        var childNumInRow = 0
        val rowSize = widthSize - paddingLeft - paddingRight
        val allowFlow = widthMode != MeasureSpec.UNSPECIFIED && mFlow
        val childSpacing =
            if (mChildSpacing == SPACING_AUTO && widthMode == MeasureSpec.UNSPECIFIED) 0 else mChildSpacing
        val tmpSpacing: Float = if (childSpacing == SPACING_AUTO) 0F else childSpacing.toFloat()
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            if (child.visibility == GONE) {
                continue
            }
            val childParams = child.layoutParams
            var horizontalMargin = 0
            var verticalMargin = 0
            if (childParams is MarginLayoutParams) {
                measureChildWithMargins(
                    child,
                    widthMeasureSpec,
                    0,
                    heightMeasureSpec,
                    measuredHeight
                )
                val marginParams = childParams
                horizontalMargin = marginParams.leftMargin + marginParams.rightMargin
                verticalMargin = marginParams.topMargin + marginParams.bottomMargin
            } else {
                measureChild(child, widthMeasureSpec, heightMeasureSpec)
            }
            val childWidth = child.measuredWidth + horizontalMargin
            val childHeight = child.measuredHeight + verticalMargin
            if (allowFlow && rowWidth + childWidth > rowSize) { // Need flow to next row
                // Save parameters for current row
                mHorizontalSpacingForRow.add(
                    getSpacingForRow(childSpacing, rowSize, rowWidth, childNumInRow)
                )
                mChildNumForRow.add(childNumInRow)
                mHeightForRow.add(maxChildHeightInRow)
                measuredHeight += maxChildHeightInRow
                measuredWidth = max(measuredWidth, rowWidth)

                // Place the child view to next row
                childNumInRow = 1
                rowWidth = childWidth + tmpSpacing.toInt()
                maxChildHeightInRow = childHeight
            } else {
                childNumInRow++
                rowWidth += (childWidth + tmpSpacing).toInt()
                maxChildHeightInRow = max(maxChildHeightInRow, childHeight)
            }
        }

        // Measure remaining child views in the last row
        if (mChildSpacingForLastRow == SPACING_ALIGN) {
            // For SPACING_ALIGN, use the same spacing from the row above if there is more than one
            // row.
            if (mHorizontalSpacingForRow.size >= 1) {
                mHorizontalSpacingForRow.add(
                    mHorizontalSpacingForRow[mHorizontalSpacingForRow.size - 1]
                )
            } else {
                mHorizontalSpacingForRow.add(
                    getSpacingForRow(childSpacing, rowSize, rowWidth, childNumInRow)
                )
            }
        } else if (mChildSpacingForLastRow != SPACING_UNDEFINED) {
            // For SPACING_AUTO and specific DP values, apply them to the spacing strategy.
            mHorizontalSpacingForRow.add(
                getSpacingForRow(mChildSpacingForLastRow, rowSize, rowWidth, childNumInRow)
            )
        } else {
            // For SPACING_UNDEFINED, apply childSpacing to the spacing strategy for the last row.
            mHorizontalSpacingForRow.add(
                getSpacingForRow(childSpacing, rowSize, rowWidth, childNumInRow)
            )
        }
        mChildNumForRow.add(childNumInRow)
        mHeightForRow.add(maxChildHeightInRow)
        measuredHeight += maxChildHeightInRow
        measuredWidth = max(measuredWidth, rowWidth)
        measuredWidth = if (childSpacing == SPACING_AUTO) {
            widthSize
        } else if (widthMode == MeasureSpec.UNSPECIFIED) {
            measuredWidth + paddingLeft + paddingRight
        } else {
            min(measuredWidth + paddingLeft + paddingRight, widthSize)
        }
        measuredHeight += paddingTop + paddingBottom
        val rowNum = mHorizontalSpacingForRow.size
        val rowSpacing: Float =
            if (mRowSpacing == SPACING_AUTO.toFloat() && heightMode == MeasureSpec.UNSPECIFIED) 0F else mRowSpacing
        if (rowSpacing == SPACING_AUTO.toFloat()) {
            mAdjustedRowSpacing = if (rowNum > 1) {
                ((heightSize - measuredHeight) / (rowNum - 1)).toFloat()
            } else {
                0f
            }
            measuredHeight = heightSize
        } else {
            mAdjustedRowSpacing = rowSpacing
            measuredHeight = if (heightMode == MeasureSpec.UNSPECIFIED) {
                (measuredHeight + mAdjustedRowSpacing * (rowNum - 1)).toInt()
            } else {
                min((measuredHeight + mAdjustedRowSpacing * (rowNum - 1)).toInt(), heightSize)
            }
        }
        measuredWidth = if (widthMode == MeasureSpec.EXACTLY) widthSize else measuredWidth
        measuredHeight = if (heightMode == MeasureSpec.EXACTLY) heightSize else measuredHeight
        setMeasuredDimension(measuredWidth, measuredHeight)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val paddingLeft = paddingLeft
        val paddingRight = paddingRight
        val paddingTop = paddingTop
        var x = if (mRtl) width - paddingRight else paddingLeft
        var y = paddingTop
        val rowCount = mChildNumForRow.size
        var childIdx = 0
        for (row in 0 until rowCount) {
            val childNum = mChildNumForRow[row]
            val rowHeight = mHeightForRow[row]
            val spacing = mHorizontalSpacingForRow[row]
            var i = 0
            while (i < childNum && childIdx < childCount) {
                val child = getChildAt(childIdx++)
                if (child.visibility == GONE) {
                    continue
                } else {
                    i++
                }
                val childParams = child.layoutParams
                var marginLeft = 0
                var marginTop = 0
                var marginRight = 0
                if (childParams is MarginLayoutParams) {
                    val marginParams = childParams
                    marginLeft = marginParams.leftMargin
                    marginRight = marginParams.rightMargin
                    marginTop = marginParams.topMargin
                }
                val childWidth = child.measuredWidth
                val childHeight = child.measuredHeight
                if (mRtl) {
                    child.layout(
                        x - marginRight - childWidth, y + marginTop,
                        x - marginRight, y + marginTop + childHeight
                    )
                    x -= (childWidth + spacing + marginLeft + marginRight).toInt()
                } else {
                    child.layout(
                        x + marginLeft, y + marginTop,
                        x + marginLeft + childWidth, y + marginTop + childHeight
                    )
                    x += (childWidth + spacing + marginLeft + marginRight).toInt()
                }
            }
            x = if (mRtl) width - paddingRight else paddingLeft
            y += (rowHeight + mAdjustedRowSpacing).toInt()
        }
    }

    override fun generateLayoutParams(p: LayoutParams?): LayoutParams {
        return MarginLayoutParams(p)
    }

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
        return MarginLayoutParams(context, attrs)
    }

    /**
     * Returns whether to allow child views flow to next row when there is no enough space.
     *
     * @return Whether to flow child views to next row when there is no enough space.
     */
    fun isFlow(): Boolean {
        return mFlow
    }

    /**
     * Sets whether to allow child views flow to next row when there is no enough space.
     *
     * @param flow true to allow flow. false to restrict all child views in one row.
     */
    fun setFlow(flow: Boolean) {
        mFlow = flow
        requestLayout()
    }

    /**
     * Returns the horizontal spacing between child views.
     *
     * @return The spacing, either [FlowLayout.SPACING_AUTO], or a fixed size in pixels.
     */
    fun getChildSpacing(): Int {
        return mChildSpacing
    }

    /**
     * Sets the horizontal spacing between child views.
     *
     * @param childSpacing The spacing, either [FlowLayout.SPACING_AUTO], or a fixed size in
     * pixels.
     */
    fun setChildSpacing(childSpacing: Int) {
        mChildSpacing = childSpacing
        requestLayout()
    }

    /**
     * Returns the horizontal spacing between child views of the last row.
     *
     * @return The spacing, either [FlowLayout.SPACING_AUTO],
     * [FlowLayout.SPACING_ALIGN], or a fixed size in pixels
     */
    fun getChildSpacingForLastRow(): Int {
        return mChildSpacingForLastRow
    }

    /**
     * Sets the horizontal spacing between child views of the last row.
     *
     * @param childSpacingForLastRow The spacing, either [FlowLayout.SPACING_AUTO],
     * [FlowLayout.SPACING_ALIGN], or a fixed size in pixels
     */
    fun setChildSpacingForLastRow(childSpacingForLastRow: Int) {
        mChildSpacingForLastRow = childSpacingForLastRow
        requestLayout()
    }

    /**
     * Returns the vertical spacing between rows.
     *
     * @return The spacing, either [FlowLayout.SPACING_AUTO], or a fixed size in pixels.
     */
    fun getRowSpacing(): Float {
        return mRowSpacing
    }

    /**
     * Sets the vertical spacing between rows in pixels. Use SPACING_AUTO to evenly place all rows
     * in vertical.
     *
     * @param rowSpacing The spacing, either [FlowLayout.SPACING_AUTO], or a fixed size in
     * pixels.
     */
    fun setRowSpacing(rowSpacing: Float) {
        mRowSpacing = rowSpacing
        requestLayout()
    }

    /**
     * Sets whether to enable right to left mode.
     * @param rtl true to enable right to left mode, false to disable it.
     */
    fun setRtl(rtl: Boolean) {
        mRtl = rtl
        requestLayout()
    }

    /**
     * Returns whether to use right to left mode.
     * @return Whether to use right to left mode.
     */
    fun isRtl(): Boolean {
        return mRtl
    }

    private fun max(a: Int, b: Int): Int {
        return if (a > b) a else b
    }

    private fun min(a: Int, b: Int): Int {
        return if (a < b) a else b
    }

    private fun getSpacingForRow(
        spacingAttribute: Int,
        rowSize: Int,
        usedSize: Int,
        childNum: Int
    ): Float {
        val spacing: Float
        spacing = if (spacingAttribute == SPACING_AUTO) {
            if (childNum > 1) {
                ((rowSize - usedSize) / (childNum - 1)).toFloat()
            } else {
                0f
            }
        } else {
            spacingAttribute.toFloat()
        }
        return spacing
    }

    private fun dpToPx(dp: Float): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics
        )
    }

    companion object {
        private val LOG_TAG = FlowLayout::class.java.simpleName

        /**
         * Special value for the child view spacing.
         * SPACING_AUTO means that the actual spacing is calculated according to the size of the
         * container and the number of the child views, so that the child views are placed evenly in
         * the container.
         */
        const val SPACING_AUTO = -65536

        /**
         * Special value for the horizontal spacing of the child views in the last row
         * SPACING_ALIGN means that the horizontal spacing of the child views in the last row keeps
         * the same with the spacing used in the row above. If there is only one row, this value is
         * ignored and the spacing will be calculated according to childSpacing.
         */
        const val SPACING_ALIGN = -65537
        private const val SPACING_UNDEFINED = -65538
        private const val DEFAULT_FLOW = true
        private const val DEFAULT_CHILD_SPACING = 0
        private const val DEFAULT_CHILD_SPACING_FOR_LAST_ROW = SPACING_UNDEFINED
        private const val DEFAULT_ROW_SPACING = 0f
        private const val DEFAULT_RTL = false
    }
}