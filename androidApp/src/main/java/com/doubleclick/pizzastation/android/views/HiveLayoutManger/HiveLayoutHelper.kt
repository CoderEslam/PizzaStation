package com.doubleclick.pizzastation.android.views.HiveLayoutManger

import androidx.recyclerview.widget.RecyclerView


abstract class HiveLayoutHelper(var mLayoutManager: RecyclerView.LayoutManager) {
    abstract fun getChildBounds(position: Int)

    companion object {
        const val HORIZONTAL = 0
        const val VERTICAL = 1
        fun getInstance(layoutManager: RecyclerView.LayoutManager): HiveLayoutHelper {
            return object : HiveLayoutHelper(layoutManager) {
                override fun getChildBounds(position: Int) {}
            }
        }
    }
}

