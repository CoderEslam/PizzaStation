package com.doubleclick.pizzastation.android.views.onboarder

import android.animation.Animator
import android.animation.Animator.AnimatorListener
import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.doubleclick.pizzastation.android.R
import com.doubleclick.pizzastation.android.views.onboarder.utils.ShadowTransformer
import com.doubleclick.pizzastation.android.views.onboarder.views.CircleIndicatorView
import com.doubleclick.pizzastation.android.views.onboarder.views.FlowingGradientClass

/**
 * Created By Eslam Ghazy on 12/31/2022
 */
abstract class AhoyOnboarderActivity : AppCompatActivity(), View.OnClickListener,
    ViewPager.OnPageChangeListener {
    private lateinit var circleIndicatorView: CircleIndicatorView
    private lateinit var vpOnboarderPager: ViewPager
    private lateinit var ahoyOnboarderAdapter: AhoyOnboarderAdapter
    private lateinit var btnSkip: TextView
    private lateinit var ivNext: ImageView
    private lateinit var ivPrev: ImageView
    private lateinit var navigationControls: FrameLayout
    private lateinit var buttonsLayout: FrameLayout
    private lateinit var parentLayout: RelativeLayout
    private lateinit var backgroundImage: ImageView
    private lateinit var backgroundImageOverlay: View
    private lateinit var mCardShadowTransformer: ShadowTransformer
    private lateinit var typeface: Typeface
    private var colorList: List<Int>? = null
    private var solidBackground = false
    private var pages: List<AhoyOnboarderCard>? = null
    protected override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ahoy)
        setStatusBackgroundColor()
        hideActionBar()
        parentLayout = findViewById(R.id.parent_layout)
        circleIndicatorView = findViewById(R.id.circle_indicator_view)
        btnSkip = findViewById(R.id.btn_skip)
        buttonsLayout = findViewById(R.id.buttons_layout)
        navigationControls = findViewById(R.id.navigation_layout)
        ivNext = findViewById(R.id.ivNext)
        ivPrev = findViewById(R.id.ivPrev)
        backgroundImage = findViewById(R.id.background_image)
        backgroundImageOverlay = findViewById(R.id.background_image_overlay)
        vpOnboarderPager = findViewById(R.id.vp_pager)
        vpOnboarderPager.addOnPageChangeListener(this)
        btnSkip!!.setOnClickListener(this)
        ivPrev!!.setOnClickListener(this)
        ivNext!!.setOnClickListener(this)
        hideFinish(false)
        fadeOut(ivPrev, false)
    }

    fun setOnboardPages(pages: List<AhoyOnboarderCard>) {
        this.pages = pages
        ahoyOnboarderAdapter =
            AhoyOnboarderAdapter(pages, getSupportFragmentManager(), dpToPixels(0, this), typeface)
        mCardShadowTransformer = ShadowTransformer(vpOnboarderPager, ahoyOnboarderAdapter)
        mCardShadowTransformer.enableScaling(true)
        vpOnboarderPager.setAdapter(ahoyOnboarderAdapter)
        vpOnboarderPager.setPageTransformer(false, mCardShadowTransformer)
        circleIndicatorView.setPageIndicators(pages.size)
    }

    fun dpToPixels(dp: Int, context: Context): Float {
        return dp * context.resources.displayMetrics.density
    }

    private fun setStatusBackgroundColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            )
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.black_transparent))
        }
    }

    override fun onClick(v: View) {
        val i = v.id
        val isInFirstPage = vpOnboarderPager.currentItem === 0
        val isInLastPage = vpOnboarderPager.currentItem === ahoyOnboarderAdapter.getCount() - 1
        if (i == R.id.btn_skip && isInLastPage) {
            onFinishButtonPressed()
        } else if (i == R.id.ivPrev && !isInFirstPage) {
            vpOnboarderPager.setCurrentItem(vpOnboarderPager.getCurrentItem() - 1)
        } else if (i == R.id.ivNext && !isInLastPage) {
            vpOnboarderPager.setCurrentItem(vpOnboarderPager.getCurrentItem() + 1)
        }
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
    override fun onPageSelected(position: Int) {
        val firstPagePosition = 0
        val lastPagePosition: Int = ahoyOnboarderAdapter.getCount() - 1
        circleIndicatorView.setCurrentPage(position)
        circleIndicatorView.setCurrentPage(position)
        if (position == lastPagePosition) {
            fadeOut(circleIndicatorView)
            showFinish()
            fadeOut(ivNext)
            fadeIn(ivPrev)
        } else if (position == firstPagePosition) {
            fadeOut(ivPrev)
            fadeIn(ivNext)
            hideFinish()
            fadeIn(circleIndicatorView)
        } else {
            fadeIn(circleIndicatorView)
            hideFinish()
            fadeIn(ivPrev)
            fadeIn(ivNext)
        }
        if (solidBackground && pages!!.size == colorList!!.size) {
            backgroundImage!!.setBackgroundColor(
                ContextCompat.getColor(
                    this,
                    colorList!![position]
                )
            )
        }
    }

    override fun onPageScrollStateChanged(state: Int) {}
    private fun fadeOut(v: View?, delay: Boolean = true) {
        var duration: Long = 0
        if (delay) {
            duration = 300
        }
        if (v!!.visibility != View.GONE) {
            val fadeOut: Animation = AlphaAnimation(1f, 0f)
            fadeOut.interpolator = AccelerateInterpolator() //and this
            fadeOut.duration = duration
            fadeOut.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation) {}
                override fun onAnimationEnd(animation: Animation) {
                    v.visibility = View.GONE
                }

                override fun onAnimationRepeat(animation: Animation) {}
            })
            v.startAnimation(fadeOut)
        }
    }

    private fun fadeIn(v: View?) {
        if (v!!.visibility != View.VISIBLE) {
            val fadeIn: Animation = AlphaAnimation(0f, 1f)
            fadeIn.interpolator = DecelerateInterpolator() //add this
            fadeIn.duration = 300
            fadeIn.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation) {}
                override fun onAnimationEnd(animation: Animation) {
                    v.visibility = View.VISIBLE
                }

                override fun onAnimationRepeat(animation: Animation) {}
            })
            v.startAnimation(fadeIn)
        }
    }

    private fun showFinish() {
        btnSkip!!.visibility = View.VISIBLE
        btnSkip!!.animate().translationY(0 - dpToPixels(5, this)).setInterpolator(
            DecelerateInterpolator()
        ).setDuration(500).start()
    }

    private fun hideFinish(delay: Boolean = true) {
        var duration: Long = 0
        if (delay) {
            duration = 250
        }
        btnSkip!!.animate().translationY(btnSkip!!.bottom + dpToPixels(100, this))
            .setInterpolator(AccelerateInterpolator()).setDuration(duration)
            .setListener(object : AnimatorListener {
                override fun onAnimationStart(animator: Animator) {}
                override fun onAnimationEnd(animator: Animator) {
                    btnSkip!!.visibility = View.VISIBLE
                }

                override fun onAnimationCancel(animator: Animator) {}
                override fun onAnimationRepeat(animator: Animator) {}
            }).start()
    }

    private fun hideActionBar() {
        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }
    }

    abstract fun onFinishButtonPressed()

    fun showNavigationControls(navigation: Boolean) {
        if (navigation) {
            navigationControls!!.visibility = View.VISIBLE
        } else {
            navigationControls!!.visibility = View.GONE
        }
    }

    fun setImageBackground(resId: Int) {
        backgroundImageOverlay!!.visibility = View.VISIBLE
        backgroundImage!!.setImageResource(resId)
    }

    fun setColorBackground(@ColorRes color: Int) {
        backgroundImage!!.setBackgroundColor(ContextCompat.getColor(this, color))
    }

    fun setColorBackground(color: List<Int>) {
        colorList = color
        solidBackground = true
        backgroundImage!!.setBackgroundColor(ContextCompat.getColor(this, color[0]))
    }

    fun setGradientBackground() {
        val grad = FlowingGradientClass()
        grad.setBackgroundResource(R.drawable.translate)
            .onRelativeLayout(parentLayout)
            .setTransitionDuration(4000)
            .start()
    }

    fun setGradientBackground(drawable: Int) {
        val grad = FlowingGradientClass()
        grad.setBackgroundResource(drawable)
            .onRelativeLayout(parentLayout)
            .setTransitionDuration(4000)
            .start()
    }

    fun setInactiveIndicatorColor(color: Int) {
        circleIndicatorView!!.setInactiveIndicatorColor(color)
    }

    fun setActiveIndicatorColor(color: Int) {
        circleIndicatorView.setActiveIndicatorColor(color)
    }

    /**
     * <br></br><br></br>
     * **N.B. Builds before JELLY_BEAN will use the default style**
     * <br></br><br></br>
     * Set the xml drawable style of the skip/done button, <br></br>
     * using for example: ContextCompat.getDrawable(this, R.drawable.rectangle_button);
     *
     * @param res A drawable xml file representing your desired button style
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    fun setFinishButtonDrawableStyle(res: Drawable?) {
        btnSkip.background = res
    }

    fun setFinishButtonTitle(title: CharSequence?) {
        btnSkip.text = title
    }

    fun setFinishButtonTitle(@StringRes titleResId: Int) {
        btnSkip.setText(titleResId)
    }

    fun setFont(typeface: Typeface) {
        btnSkip.typeface = typeface
        this.typeface = typeface
    }
}
