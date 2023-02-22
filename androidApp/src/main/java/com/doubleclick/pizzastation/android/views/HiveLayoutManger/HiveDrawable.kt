package com.doubleclick.pizzastation.android.views.HiveLayoutManger

import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.Log


class HiveDrawable @JvmOverloads constructor(
    @field:HiveLayoutManager.Orientation @get:HiveLayoutManager.Orientation
    @param:HiveLayoutManager.Orientation val orientation: Int, bitmap: Bitmap? = null
) :
    Drawable() {
    private var mHiveMathUtils: IHiveMathUtils? = null
    private val mRect = Rect()
    private var mPaint: Paint? = null
    private var mPath: Path? = null
    var bitmap: Bitmap? = null
        private set

    init {
        init()
        bitmap?.let { setBitmap(it) }
    }

    private fun init() {
        mHiveMathUtils = HiveMathUtils.instance
        initPaint()
        initPath()
    }

    private fun ensurePaint() {
        if (mPaint == null) {
            mPaint = Paint()
        }
    }

    private fun ensurePath() {
        if (mPath == null) {
            mPath = Path()
        }
    }

    private fun initPaint() {
        ensurePaint()
        mPaint!!.isAntiAlias = true
        mPaint!!.style = Paint.Style.FILL
        mPaint!!.strokeWidth = 3f
    }

    fun setBitmap(bitmap: Bitmap) {
        this.bitmap = bitmap
        val shader = BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        mPaint!!.shader = shader
    }

    private fun initPath() {
        ensurePath()
        val l = mHiveMathUtils!!.calculateLength(mRect, orientation)
        if (orientation == HiveLayoutManager.HORIZONTAL) {
            val h = (Math.sqrt(3.0) * l).toFloat()
            val top = (mRect.height() - h) / 2
            mPath!!.reset()
            mPath!!.moveTo(l / 2 + mRect.left, top + mRect.top)
            mPath!!.lineTo((0 + mRect.left).toFloat(), h / 2 + top + mRect.top)
            mPath!!.lineTo(l / 2 + mRect.left, h + top + mRect.top)
            mPath!!.lineTo((l * 1.5).toFloat() + mRect.left, h + top + mRect.top)
            mPath!!.lineTo(2 * l + mRect.left, h / 2 + top + mRect.top)
            mPath!!.lineTo((l * 1.5).toFloat() + mRect.left, top + mRect.top)
            mPath!!.lineTo(l / 2 + mRect.left, top + mRect.top)
            mPath!!.close()
        } else if (orientation == HiveLayoutManager.VERTICAL) {
            val w = (Math.sqrt(3.0) * l).toFloat()
            val left = (mRect.width() - w) / 2
            mPath!!.reset()
            mPath!!.moveTo(left + mRect.left, l / 2 + mRect.top)
            mPath!!.lineTo(w / 2 + left + mRect.left, (0 + mRect.top).toFloat())
            mPath!!.lineTo(w + left + mRect.left, l / 2 + mRect.top)
            mPath!!.lineTo(w + left + mRect.left, (l * 1.5).toFloat() + mRect.top)
            mPath!!.lineTo(w / 2 + left + mRect.left, 2 * l + mRect.top)
            mPath!!.lineTo(left + mRect.left, (l * 1.5).toFloat() + mRect.top)
            mPath!!.lineTo(left + mRect.left, l / 2 + mRect.top)
            mPath!!.close()
        } else {
            Log.e(
                TAG, String.format(
                    "hive drawable orientation mast be horizontal : %d or vertical : %d",
                    HiveLayoutHelper.HORIZONTAL,
                    HiveLayoutHelper.VERTICAL
                )
            )
        }
    }

    fun setColor(color: Int) {
        mPaint!!.color = color
    }

    override fun draw(canvas: Canvas) {
        canvas.drawPath(mPath!!, mPaint!!)
    }

    override fun setAlpha(alpha: Int) {
        if (mPaint != null) {
            mPaint!!.alpha = alpha
        }
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        if (mPaint != null) {
            mPaint!!.colorFilter = colorFilter
        }
    }

    override fun getOpacity(): Int {
        return PixelFormat.UNKNOWN
    }

    override fun setBounds(left: Int, top: Int, right: Int, bottom: Int) {
        super.setBounds(left, top, right, bottom)
        mRect[left, top, right] = bottom
        initPath()
    }

    override fun getIntrinsicWidth(): Int {
        return if (bitmap != null) {
            bitmap!!.width
        } else {
            super.getIntrinsicWidth()
        }
    }

    override fun getIntrinsicHeight(): Int {
        return if (bitmap != null) {
            bitmap!!.height
        } else super.getIntrinsicHeight()
    }

    companion object {
        private val TAG = HiveDrawable::class.java.simpleName
    }
}
