package com.doubleclick.pizzastation.android.views.viewpagerlayoutmanager

import android.content.Context
import android.view.WindowManager
import android.widget.PopupWindow

abstract class SettingPopUpWindow(context: Context?) :
    PopupWindow(context) {
    init {
        isOutsideTouchable = true
        width = Util.Dp2px(context!!, 320f)
        height = WindowManager.LayoutParams.WRAP_CONTENT
    }
}