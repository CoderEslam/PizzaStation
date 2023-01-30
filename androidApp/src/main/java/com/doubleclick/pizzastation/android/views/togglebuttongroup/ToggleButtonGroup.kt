package com.doubleclick.pizzastation.android.views.togglebuttongroup

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.widget.Checkable
import android.widget.CompoundButton
import com.doubleclick.pizzastation.android.R
import com.doubleclick.pizzastation.android.views.togglebuttongroup.FlowLayout
import com.doubleclick.pizzastation.android.views.togglebuttongroup.button.OnCheckedChangeListener
import com.doubleclick.pizzastation.android.views.togglebuttongroup.button.ToggleButton


/**
 * Created By Eslam Ghazy on 11/20/2022
 */
abstract class ToggleButtonGroup @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) :
    FlowLayout(context, attrs) {
    protected var mInitialCheckedId = NO_ID
    protected var mSilentInitialCheckedId = NO_ID
    private var mCheckedStateTracker: OnCheckedChangeListener? = null
    private var mCompoundButtonStateTracker: CompoundButton.OnCheckedChangeListener? = null
    private var mPassThroughListener: PassThroughHierarchyChangeListener? = null
    protected abstract fun <T> onChildCheckedChange(
        child: T,
        isChecked: Boolean
    ) where T : View?, T : Checkable?

    init {
        val a = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.ToggleButtonGroup, 0, 0
        )
        try {
            mInitialCheckedId = a.getResourceId(
                R.styleable.ToggleButtonGroup_tbgCheckedButton,
                NO_ID
            )
            mSilentInitialCheckedId = a.getResourceId(
                R.styleable.ToggleButtonGroup_android_checkedButton, NO_ID
            )
        } finally {
            a.recycle()
        }
        init()
    }

    private fun init() {
        mPassThroughListener = PassThroughHierarchyChangeListener()
        super.setOnHierarchyChangeListener(mPassThroughListener)
    }

    override fun setOnHierarchyChangeListener(listener: OnHierarchyChangeListener?) {
        mPassThroughListener!!.mOnHierarchyChangeListener = listener
    }

    fun isChecked(childId: Int): Boolean {
        val child: View = findViewById(childId)
        return child is Checkable && (child as Checkable).isChecked
    }

    protected fun setCheckedStateForView(viewId: Int, checked: Boolean) {
        val target: View? = findViewById(viewId)
        if (target != null && target is Checkable) {
            (target as Checkable).isChecked = checked
        }
    }

    protected fun toggleCheckedStateForView(viewId: Int) {
        val target: View? = findViewById(viewId)
        if (target != null && target is Checkable) {
            (target as Checkable).toggle()
        }
    }

    private inner class CheckedStateTracker : OnCheckedChangeListener {
        override fun <T> onCheckedChanged(
            view: T,
            isChecked: Boolean
        ) where T : View?, T : Checkable? {
            onChildCheckedChange(view, isChecked)
        }
    }

    private inner class CompoundButtonCheckedStateTracker : CompoundButton.OnCheckedChangeListener {
        override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
            onChildCheckedChange(buttonView, isChecked)
        }
    }

    private inner class PassThroughHierarchyChangeListener :
        OnHierarchyChangeListener {
        var mOnHierarchyChangeListener: OnHierarchyChangeListener? = null
        override fun onChildViewAdded(parent: View, child: View) {
            if (parent === this@ToggleButtonGroup && child is Checkable) {
                if (child.id == NO_ID) {
                    child.id = generateIdForView(child)
                }
                if (child is ToggleButton) {
                    setStateTracker(child as ToggleButton)
                } else if (child is CompoundButton) {
                    setStateTracker(child)
                }
            }
            mOnHierarchyChangeListener?.onChildViewAdded(parent, child)
        }

        override fun onChildViewRemoved(parent: View, child: View) {
            if (parent === this@ToggleButtonGroup && child is Checkable) {
                if (child is ToggleButton) {
                    clearStateTracker(child as ToggleButton)
                } else if (child is CompoundButton) {
                    clearStateTracker(child)
                }
            }
            mOnHierarchyChangeListener?.onChildViewRemoved(parent, child)
        }
    }

    private fun setStateTracker(view: ToggleButton) {
        if (mCheckedStateTracker == null) {
            mCheckedStateTracker = CheckedStateTracker()
        }
        view.setOnCheckedChangeListener(mCheckedStateTracker)
    }

    private fun clearStateTracker(view: ToggleButton) {
        view.setOnCheckedChangeListener(null)
    }

    private fun setStateTracker(view: CompoundButton) {
        if (mCompoundButtonStateTracker == null) {
            mCompoundButtonStateTracker = CompoundButtonCheckedStateTracker()
        }
        view.setOnCheckedChangeListener(mCompoundButtonStateTracker)
    }

    private fun clearStateTracker(view: CompoundButton) {
        view.setOnCheckedChangeListener(null)
    }

    protected fun generateIdForView(view: View): Int {
        return if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN_MR1) view.hashCode() else generateViewId()
    }

    companion object {
        private val LOG_TAG = ToggleButtonGroup::class.java.simpleName
    }
}
