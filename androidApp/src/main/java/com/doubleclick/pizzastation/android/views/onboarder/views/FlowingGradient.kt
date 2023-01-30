package com.doubleclick.pizzastation.android.views.onboarder.views

import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.util.AttributeSet
import android.view.View
import com.doubleclick.pizzastation.android.R

/**
 * Created By Eslam Ghazy on 12/31/2022
 */
class FlowingGradient : View {
    var duration = 0
    var draw = 0

    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        val a = getContext().obtainStyledAttributes(attrs, R.styleable.gradient, 0, 0)
        draw = a.getResourceId(R.styleable.gradient_transition_drawable, R.drawable.translate)
        duration = a.getInt(R.styleable.gradient_transition_duration, 75)
        init()
    }

    constructor(context: Context?) : super(context) {
        init()
    }

    private fun init() {
        setBackgroundResource(draw)
        val frameAnimation = background as AnimationDrawable
        frameAnimation.setEnterFadeDuration(duration)
        frameAnimation.setExitFadeDuration(duration)
        post { frameAnimation.start() }
    }
}