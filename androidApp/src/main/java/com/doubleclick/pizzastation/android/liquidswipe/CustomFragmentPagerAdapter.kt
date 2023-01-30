package com.doubleclick.pizzastation.android.liquidswipe

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class CustomFragmentPagerAdapter(fm: FragmentManager) :
    FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        return SplashFragment.newInstance(
            dots[(position % title.count())],
            lotteAnimation[(position % title.count())],
            title[(position % title.count())],
            position
        )
    }

    override fun getCount(): Int {
        return title.count()
    }
}