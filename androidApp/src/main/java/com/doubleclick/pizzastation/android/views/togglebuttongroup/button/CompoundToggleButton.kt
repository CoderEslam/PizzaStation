package com.doubleclick.pizzastation.android.views.togglebuttongroup.button

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.annotation.CallSuper

/**
 * Created By Eslam Ghazy on 11/20/2022
 */
abstract class CompoundToggleButton : FrameLayout, ToggleButton {
    private var mChecked = false
    private var mBroadcasting = false
    private var mOnCheckedWidgetListener: OnCheckedChangeListener? = null

    constructor(context: Context?) : super(context!!) {
        isClickable = true
    }

    @JvmOverloads
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int = 0) : super(
        context!!, attrs, defStyleAttr
    ) {
        isClickable = true
    }

    override fun performClick(): Boolean {
        toggle()
        return super.performClick()
    }

    override fun setOnCheckedChangeListener(listener: OnCheckedChangeListener?) {
        mOnCheckedWidgetListener = listener
    }

    @CallSuper
    override fun setChecked(checked: Boolean) {
        if (mChecked != checked) {
            mChecked = checked
            if (mBroadcasting) {
                return
            }
            post {
                mBroadcasting = true
                if (mOnCheckedWidgetListener != null) {
                    mOnCheckedWidgetListener!!.onCheckedChanged(this@CompoundToggleButton, mChecked)
                }
                mBroadcasting = false
            }
        }
    }

    override fun isChecked(): Boolean {
        return mChecked
    }

    @CallSuper
    override fun toggle() {
        isChecked = !mChecked
    }

    companion object {
        private val LOG_TAG = CompoundToggleButton::class.java.simpleName
    }
}
