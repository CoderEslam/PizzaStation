package com.doubleclick.pizzastation.android.views.togglebuttongroup.button

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import androidx.core.content.ContextCompat
import com.doubleclick.pizzastation.android.R

/**
 * Created By Eslam Ghazy on 11/20/2022
 */
class CircularToggle @JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null) :
    MarkerButton(context!!, attrs) {
    private val mAnimationDuration = DEFAULT_ANIMATION_DURATION.toLong()
    private lateinit var mCheckAnimation: Animation
    private lateinit var mUncheckAnimation: Animation
    private lateinit var mTextColorAnimator: ValueAnimator

    init {
        init(context!!)
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

    private fun init(context: Context) {
        initBackground()
        initAnimation()
        initText(context)
    }

    fun getName(): CharSequence {
        return getText()
    }

    private fun initBackground() {
        val checked = ContextCompat.getDrawable(
            context, R.drawable.bg_circle
        ) as GradientDrawable?
        checked!!.setColor(mMarkerColor)
        mIvBg.setImageDrawable(checked)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun initText(context: Context) {
        mTvText.setBackgroundDrawable(context.resources.getDrawable(R.drawable.bg_label_unchecked))
    }

    private fun initAnimation() {
        val defaultTextColor = getDefaultTextColor()
        val checkedTextColor = getCheckedTextColor()
        mTextColorAnimator = ValueAnimator.ofObject(
            ArgbEvaluator(), defaultTextColor, checkedTextColor
        )
        mTextColorAnimator.setDuration(mAnimationDuration)
        mTextColorAnimator.addUpdateListener(AnimatorUpdateListener { valueAnimator ->
            mTvText.setTextColor(
                (valueAnimator.animatedValue as Int)
            )
        })
        mCheckAnimation = ScaleAnimation(
            0f, 1f, 0f, 1f,
            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f
        )
        mCheckAnimation.setDuration(mAnimationDuration)
        mCheckAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                mTvText.setTextColor(checkedTextColor)
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
        mUncheckAnimation = ScaleAnimation(
            1f, 0f, 1f, 0f,
            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f
        )
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
        private val LOG_TAG = CircularToggle::class.java.simpleName
        private const val DEFAULT_ANIMATION_DURATION = 150
    }
}
