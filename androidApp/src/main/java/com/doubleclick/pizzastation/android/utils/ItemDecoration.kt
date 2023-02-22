package com.doubleclick.pizzastation.android.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

//https://stackoverflow.com/questions/73397766/how-to-create-circle-layout-manager-for-recyclerview-in-android
class ItemDecoration : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.set(-20, 0, 0, 0)
    }
}