package com.doubleclick.pizzastation.android.views.neumorph.internal.shape

import android.graphics.Canvas
import android.graphics.Path
import android.graphics.Rect
import com.doubleclick.pizzastation.android.views.neumorph.NeumorphShapeDrawable.NeumorphShapeDrawableState

internal interface Shape {
    fun setDrawableState(newDrawableState: NeumorphShapeDrawableState)
    fun draw(canvas: Canvas, outlinePath: Path)
    fun updateShadowBitmap(bounds: Rect)
}
