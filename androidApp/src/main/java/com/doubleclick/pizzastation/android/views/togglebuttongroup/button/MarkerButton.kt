package com.doubleclick.pizzastation.android.views.togglebuttongroup.button

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.doubleclick.pizzastation.android.R
import com.doubleclick.pizzastation.android.views.neumorph.NeumorphImageView

/**
 * Created By Eslam Ghazy on 11/20/2022
 */
abstract class MarkerButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) :
    CompoundToggleButton(context, attrs) {
    protected var mTvText: TextView
    protected var mIvBg: NeumorphImageView
    protected var mMarkerColor = 0
    private var mRadioStyle = false

    init {
        val inflater = context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.view_marker_button, this, true)
        mIvBg = findViewById(R.id.iv_bg) as NeumorphImageView
        mTvText = findViewById(R.id.tv_text) as TextView
        val a = context.theme.obtainStyledAttributes(
            attrs, R.styleable.MarkerButton, 0, 0
        )
        try {
            val text = a.getText(R.styleable.MarkerButton_android_text)
            mTvText.text = text
            var colors = a.getColorStateList(R.styleable.MarkerButton_android_textColor)
            if (colors == null) {
                colors = ContextCompat.getColorStateList(context, R.color.selector_marker_text)
            }
            mTvText.setTextColor(colors)
            val textSize = a.getDimension(
                R.styleable.MarkerButton_android_textSize, dpToPx(
                    DEFAULT_TEXT_SIZE_SP.toFloat()
                )
            )
            mTvText.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
            mMarkerColor = a.getColor(
                R.styleable.MarkerButton_tbgMarkerColor,
                ContextCompat.getColor(getContext(), R.color.red)
            )
            mRadioStyle = a.getBoolean(R.styleable.MarkerButton_tbgRadioStyle, false)
        } finally {
            a.recycle()
        }
    }

    override fun toggle() {
        // Do not allow toggle to unchecked state when mRadioStyle is true
        if (mRadioStyle && isChecked) {
            return
        }
        super.toggle()
    }

    fun isRadioStyle(): Boolean {
        return mRadioStyle
    }

    fun setRadioStyle(radioStyle: Boolean) {
        mRadioStyle = radioStyle
    }

    fun setText(text: CharSequence?) {
        mTvText.text = text
    }

    fun getText(): CharSequence {
        return mTvText.text
    }

    open fun setTextColor(color: Int) {
        mTvText.setTextColor(color)
    }

    open fun setTextColor(colors: ColorStateList?) {
        mTvText.setTextColor(colors)
    }

    fun getTextColors(): ColorStateList {
        return mTvText.textColors
    }

    fun setTextSize(size: Float) {
        mTvText.textSize = size
    }

    fun setTextSize(unit: Int, size: Float) {
        mTvText.setTextSize(unit, size)
    }

    fun getTextSize(): Float {
        return mTvText.textSize
    }

    fun setTextBackground(drawable: Drawable?) {
        mTvText.setBackgroundDrawable(drawable)
    }

    fun getTextBackground(): Drawable {
        return mTvText.background
    }

    fun setCheckedImageDrawable(drawable: Drawable?) {
        mIvBg.setImageDrawable(drawable)
    }

    fun getCheckedImageDrawable(): Drawable {
        return mIvBg.drawable
    }

    fun getMarkerColor(): Int {
        return mMarkerColor
    }

    open fun setMarkerColor(markerColor: Int) {
        mMarkerColor = markerColor
    }

    fun getTextView(): TextView {
        return mTvText
    }

    protected fun getDefaultTextColor(): Int {
        return getTextColors().defaultColor
    }

    protected fun getCheckedTextColor(): Int {
        return getTextColors().getColorForState(CHECKED_STATE_SET, getDefaultTextColor())
    }

    protected fun dpToPx(dp: Float): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics
        )
    }

    companion object {
        private val LOG_TAG = MarkerButton::class.java.simpleName
        private const val DEFAULT_TEXT_SIZE_SP = 14
        protected val CHECKED_STATE_SET = intArrayOf(android.R.attr.state_checked)
    }
}
