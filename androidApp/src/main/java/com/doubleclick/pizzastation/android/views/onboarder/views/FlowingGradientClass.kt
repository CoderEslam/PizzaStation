package com.doubleclick.pizzastation.android.views.onboarder.views

import android.graphics.drawable.AnimationDrawable
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout

/**
 * Created By Eslam Ghazy on 12/31/2022
 */
class FlowingGradientClass {
    var Duration = 4000
    var rl: RelativeLayout? = null
    var ll: LinearLayout? = null
    var im: ImageView? = null
    var alphaint = 0
    var d = 0
    var frameAnimation: AnimationDrawable? = null
    fun setTransitionDuration(time: Int): FlowingGradientClass {
        Duration = time
        return this
    }

    fun onLinearLayout(ll: LinearLayout?): FlowingGradientClass {
        this.ll = ll
        return this
    }

    fun onImageView(im: ImageView?): FlowingGradientClass {
        this.im = im
        return this
    }

    fun onRelativeLayout(rl: RelativeLayout?): FlowingGradientClass {
        this.rl = rl
        return this
    }

    fun start(): FlowingGradientClass {
        if (ll != null) {
            ll!!.setBackgroundResource(d)
        } else if (rl != null) {
            rl!!.setBackgroundResource(d)
        } else if (im != null) {
            im!!.setBackgroundResource(d)
        }
        if (ll != null) {
            frameAnimation = ll!!.background as AnimationDrawable
        } else if (rl != null) {
            frameAnimation = rl!!.background as AnimationDrawable
        } else if (im != null) {
            frameAnimation = im!!.background as AnimationDrawable
        }
        frameAnimation!!.setEnterFadeDuration(Duration)
        frameAnimation!!.setExitFadeDuration(Duration)
        frameAnimation!!.start()
        return this
    }

    fun setBackgroundResource(d: Int): FlowingGradientClass {
        this.d = d
        return this
    }

    fun setAlpha(alphaint: Int): FlowingGradientClass {
        this.alphaint = alphaint
        frameAnimation!!.alpha = alphaint
        return this
    }
}