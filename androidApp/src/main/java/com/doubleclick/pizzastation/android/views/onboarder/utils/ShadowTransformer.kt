package com.doubleclick.pizzastation.android.views.onboarder.utils

import android.view.View
import androidx.cardview.widget.CardView
import androidx.viewpager.widget.ViewPager

/**
 * Created By Eslam Ghazy on 12/31/2022
 */
class ShadowTransformer(viewPager: ViewPager, adapter: CardAdapter) :
    ViewPager.OnPageChangeListener, ViewPager.PageTransformer {
    private val mViewPager: ViewPager
    private val mAdapter: CardAdapter
    private var mLastOffset = 0f
    private var mScalingEnabled = false

    interface CardAdapter {
        fun getBaseElevation(): Float
        fun getCardViewAt(position: Int): CardView?
        fun getCount(): Int

        companion object {
            const val MAX_ELEVATION_FACTOR = 6
        }
    }

    fun enableScaling(enable: Boolean) {
        if (mScalingEnabled && !enable) {
            // shrink main card
            val currentCard = mAdapter.getCardViewAt(mViewPager.currentItem)
            if (currentCard != null) {
                currentCard.animate().scaleY(1f)
                currentCard.animate().scaleX(1f)
            }
        } else if (!mScalingEnabled && enable) {
            // grow main card
            val currentCard = mAdapter.getCardViewAt(mViewPager.currentItem)
            if (currentCard != null) {
                currentCard.animate().scaleY(1.1f)
                currentCard.animate().scaleX(1.1f)
            }
        }
        mScalingEnabled = enable
    }

    override fun transformPage(page: View, position: Float) {}
    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        val realCurrentPosition: Int
        val nextPosition: Int
        val baseElevation = mAdapter.getBaseElevation()
        val realOffset: Float
        val goingLeft = mLastOffset > positionOffset

        // If we're going backwards, onPageScrolled receives the last position
        // instead of the current one
        if (goingLeft) {
            realCurrentPosition = position + 1
            nextPosition = position
            realOffset = 1 - positionOffset
        } else {
            nextPosition = position + 1
            realCurrentPosition = position
            realOffset = positionOffset
        }

        // Avoid crash on overscroll
        if (nextPosition > mAdapter.getCount() - 1
            || realCurrentPosition > mAdapter.getCount() - 1
        ) {
            return
        }
        val currentCard = mAdapter.getCardViewAt(realCurrentPosition)

        // This might be null if a fragment is being used
        // and the views weren't created yet
        if (currentCard != null) {
            if (mScalingEnabled) {
                currentCard.scaleX = (1 + 0.1 * (1 - realOffset)).toFloat()
                currentCard.scaleY = (1 + 0.1 * (1 - realOffset)).toFloat()
            }
            currentCard.cardElevation = baseElevation + (baseElevation
                    * (CardAdapter.MAX_ELEVATION_FACTOR - 1) * (1 - realOffset))
        }
        val nextCard = mAdapter.getCardViewAt(nextPosition)

        // We might be scrolling fast enough so that the next (or previous) card
        // was already destroyed or a fragment might not have been created yet
        if (nextCard != null) {
            if (mScalingEnabled) {
                nextCard.scaleX = (1 + 0.1 * realOffset).toFloat()
                nextCard.scaleY = (1 + 0.1 * realOffset).toFloat()
            }
            nextCard.cardElevation = baseElevation + (baseElevation
                    * (CardAdapter.MAX_ELEVATION_FACTOR - 1) * realOffset)
        }
        mLastOffset = positionOffset
    }

    override fun onPageSelected(position: Int) {}
    override fun onPageScrollStateChanged(state: Int) {}

    init {
        mViewPager = viewPager
        viewPager.addOnPageChangeListener(this)
        mAdapter = adapter
    }
}
