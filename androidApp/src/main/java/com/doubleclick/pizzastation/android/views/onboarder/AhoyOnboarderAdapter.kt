package com.doubleclick.pizzastation.android.views.onboarder

import android.graphics.Typeface
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.doubleclick.pizzastation.android.views.onboarder.utils.ShadowTransformer

/**
 * Created By Eslam Ghazy on 12/31/2022
 */
class AhoyOnboarderAdapter(
    pages: List<AhoyOnboarderCard>,
    fm: FragmentManager,
    baseElevation: Float,
    typeface: Typeface,
) :
    FragmentStatePagerAdapter(fm), ShadowTransformer.CardAdapter {
    var pages: List<AhoyOnboarderCard> = ArrayList()
    private val mFragments: MutableList<AhoyOnboarderFragment?> = ArrayList()
    private val mBaseElevation: Float
    private val typeface: Typeface
    override fun getItem(position: Int): Fragment {
        //return AhoyOnboarderFragment.newInstance(pages.get(position));
        return mFragments[position]!!
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val fragment = super.instantiateItem(container!!, position)
        mFragments[position] = fragment as AhoyOnboarderFragment
        return fragment
    }

    override fun getBaseElevation(): Float {
        return mBaseElevation
    }

    override fun getCardViewAt(position: Int): CardView? {
        setTypeface(typeface, position)
        return mFragments[position]!!.getCardView()
    }

    private fun addCardFragment(page: AhoyOnboarderCard) {
        mFragments.add(AhoyOnboarderFragment.newInstance(page))
    }

    //    override fun getCount(): Int {
//        return pages.size
//    }
    override fun getCount(): Int {
        return pages.size
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        // TODO Auto-generated method stub
        super.destroyItem(container!!, position, `object`!!)
    }

    private fun setTypeface(typeface: Typeface?, i: Int) {
        if (typeface != null) {
            if (mFragments[i] == null) {
                return
            }
            if (mFragments[i]!!.getTitleView() == null) {
                return
            }
            if (mFragments[i]!!.getTitleView() == null) {
                return
            }
            mFragments[i]!!.getTitleView()!!.typeface = typeface
            mFragments[i]!!.getDescriptionView()!!.typeface = typeface
        }
    }

    init {
        this.pages = pages
        this.typeface = typeface
        mBaseElevation = baseElevation
        for (i in pages.indices) {
            addCardFragment(pages[i])
        }

        //setTypeface(typeface);
    }
}
