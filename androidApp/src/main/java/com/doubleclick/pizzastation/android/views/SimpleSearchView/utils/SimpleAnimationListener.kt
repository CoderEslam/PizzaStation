package com.doubleclick.pizzastation.android.views.SimpleSearchView.utils

import android.view.View


abstract class SimpleAnimationListener : SimpleAnimationUtils.AnimationListener {
    override fun onAnimationStart(view: View): Boolean {
        // No action
        return false
    }

    override fun onAnimationEnd(view: View): Boolean {
        // No action
        return false
    }

    override fun onAnimationCancel(view: View): Boolean {
        // No action
        return false
    }
}