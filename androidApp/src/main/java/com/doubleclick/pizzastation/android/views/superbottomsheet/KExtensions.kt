package com.doubleclick.pizzastation.android.views.superbottomsheet

import android.content.Context
import android.content.res.Configuration
import android.util.TypedValue
import com.doubleclick.pizzastation.android.R

//region NULL

internal inline fun <T, R> T?.runIfNotNull(block: T.() -> R): R? = this?.block()

//endregion

//region CONTEXT

internal fun Context?.isTablet() = this?.resources?.getBoolean(R.bool.super_bottom_sheet_isTablet)
        ?: false

internal fun Context?.isInPortrait() = this?.resources?.configuration?.orientation == Configuration.ORIENTATION_PORTRAIT

internal fun Context.getAttrId(attrId: Int): Int {
    TypedValue().run {
        return when {
            !theme.resolveAttribute(attrId, this, true) -> INVALID_RESOURCE_ID
            else -> resourceId
        }
    }
}

//endregion