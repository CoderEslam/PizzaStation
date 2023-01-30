package com.doubleclick.pizzastation.android.views.togglebuttongroup

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.Checkable
import com.doubleclick.pizzastation.android.R
import com.doubleclick.pizzastation.android.views.togglebuttongroup.button.ToggleButton

/**
 * Created By Eslam Ghazy on 11/20/2022
 */
class MultiSelectToggleGroup : ToggleButtonGroup {
    private var mOnCheckedStateChangeListener: OnCheckedStateChangeListener? = null
    private var mMaxSelectCount = -1

    constructor(context: Context?) : super(context!!) {}
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        val a = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.MultiSelectToggleGroup, 0, 0
        )
        mMaxSelectCount = try {
            a.getInt(R.styleable.MultiSelectToggleGroup_tbgMaxSelect, -1)
        } finally {
            a.recycle()
        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        val initialCheckedId =
            if (mInitialCheckedId !== NO_ID) mInitialCheckedId else mSilentInitialCheckedId
        if (initialCheckedId != NO_ID) {
            setCheckedStateForView(initialCheckedId, true)
        }
    }

    override fun <T> onChildCheckedChange(
        child: T,
        isChecked: Boolean
    ) where T : View?, T : Checkable? {
        checkSelectCount()
        if (mSilentInitialCheckedId === child!!.id) {
            mSilentInitialCheckedId = NO_ID
        } else {
            notifyCheckedStateChange(child!!.id, isChecked)
        }
    }

    fun check(id: Int) {
        setCheckedStateForView(id, true)
    }

    fun check(id: Int, checked: Boolean) {
        setCheckedStateForView(id, checked)
    }

    fun clearCheck() {
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            if (child is ToggleButton) {
                (child as ToggleButton).isChecked = false
            }
        }
    }

    fun getCheckedIds(): Set<Int> {
        val ids: MutableSet<Int> = LinkedHashSet()
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            if (child is ToggleButton && (child as ToggleButton).isChecked) {
                ids.add(child.id)
            }
        }
        return ids
    }

    fun toggle(id: Int) {
        toggleCheckedStateForView(id)
    }

    fun setOnCheckedChangeListener(listener: OnCheckedStateChangeListener?) {
        mOnCheckedStateChangeListener = listener
    }

    fun getMaxSelectCount(): Int {
        return mMaxSelectCount
    }

    fun setMaxSelectCount(maxSelectCount: Int) {
        mMaxSelectCount = maxSelectCount
        checkSelectCount()
    }

    private fun checkSelectCount() {
        if (mMaxSelectCount < 0) {
            return
        }
        val uncheckedViews: MutableList<View> = ArrayList()
        var checkedCount = 0
        for (i in 0 until childCount) {
            val view = getChildAt(i)
            if (view is Checkable) {
                if ((view as Checkable).isChecked) {
                    checkedCount++
                } else {
                    uncheckedViews.add(view)
                }
            }
        }
        if (checkedCount >= mMaxSelectCount) {
            for (view in uncheckedViews) {
                view.isEnabled = false
            }
        } else {
            for (view in uncheckedViews) {
                view.isEnabled = true
            }
        }
    }

    private fun notifyCheckedStateChange(id: Int, isChecked: Boolean) {
        if (mOnCheckedStateChangeListener != null) {
            mOnCheckedStateChangeListener!!.onCheckedStateChanged(this, id, isChecked)
        }
    }

    interface OnCheckedStateChangeListener {
        fun onCheckedStateChanged(
            group: MultiSelectToggleGroup?,
            checkedId: Int,
            isChecked: Boolean
        )
    }

    companion object {
        private val LOG_TAG = MultiSelectToggleGroup::class.java.simpleName
    }
}
