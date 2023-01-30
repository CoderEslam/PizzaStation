package com.doubleclick.pizzastation.android.views.togglebuttongroup.button

import android.R
import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.util.Log
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import androidx.core.content.ContextCompat

/**
 * Created By Eslam Ghazy on 11/20/2022
 */
class LabelToggle @JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null) :
    MarkerButton(context!!, attrs), ToggleButton {
    private val mAnimationDuration = DEFAULT_ANIMATION_DURATION.toLong()
    private lateinit var mCheckAnimation: Animation
    private lateinit var mUncheckAnimation: Animation
    private lateinit var mTextColorAnimator: ValueAnimator

    init {
        init()
    }

    override fun setMarkerColor(markerColor: Int) {
        super.setMarkerColor(markerColor)
        initBackground()
    }

    override fun setTextColor(color: Int) {
        super.setTextColor(color)
        initAnimation()
    }

    override fun setTextColor(colors: ColorStateList?) {
        super.setTextColor(colors)
        initAnimation()
    }

    private fun init() {
        initBackground()
        initText()
        initAnimation()
    }

    private fun initBackground() {
        val checked = GradientDrawable()
        checked.setColor(mMarkerColor)
        checked.cornerRadius = dpToPx(25F)
        checked.setStroke(1, mMarkerColor)
        mIvBg.setImageDrawable(checked)
        val unchecked = GradientDrawable()
        unchecked.setColor(ContextCompat.getColor(context, R.color.transparent))
        unchecked.cornerRadius = dpToPx(25F)
        unchecked.setStroke(dpToPx(1F).toInt(), mMarkerColor)
        mTvText.setBackgroundDrawable(unchecked)
    }

    private fun initText() {
        val padding = dpToPx(16F).toInt()
        mTvText.setPadding(padding, 0, padding, 0)
    }

    private fun initAnimation() {
        val defaultTextColor = getDefaultTextColor()
        val checkedTextColor = getCheckedTextColor()
        Log.v(
            LOG_TAG,
            "initAnimation(): defaultTextColor = $defaultTextColor, checkedTextColor = $checkedTextColor"
        )
        mTextColorAnimator = ValueAnimator.ofObject(
            ArgbEvaluator(), defaultTextColor, checkedTextColor
        )
        mTextColorAnimator.setDuration(mAnimationDuration)
        mTextColorAnimator.addUpdateListener(AnimatorUpdateListener { valueAnimator ->
            mTvText.setTextColor(
                (valueAnimator.animatedValue as Int)
            )
        })
        mCheckAnimation = AlphaAnimation(0f, 1f)
        mCheckAnimation.setDuration(mAnimationDuration)
        mCheckAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                mTvText.setTextColor(checkedTextColor)
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
        mUncheckAnimation = AlphaAnimation(1f, 0f)
        mUncheckAnimation.setDuration(mAnimationDuration)
        mUncheckAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                mIvBg.visibility = INVISIBLE
                mTvText.setTextColor(defaultTextColor)
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
    }

    override fun setChecked(checked: Boolean) {
        super.setChecked(checked)
        if (checked) {
            mIvBg.visibility = VISIBLE
            mIvBg.startAnimation(mCheckAnimation)
            mTextColorAnimator!!.start()
        } else {
            mIvBg.visibility = VISIBLE
            mIvBg.startAnimation(mUncheckAnimation)
            mTextColorAnimator!!.reverse()
        }
    }

    companion object {
        private val LOG_TAG = LabelToggle::class.java.simpleName
        private const val DEFAULT_ANIMATION_DURATION = 150
    }
}
