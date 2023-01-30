package com.doubleclick.pizzastation.android.views.onboarder

import android.graphics.drawable.Drawable
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

/**
 * Created By Eslam Ghazy on 12/31/2022
 */
class AhoyOnboarderCard {
    private lateinit var title: String
    private lateinit var description: String
    private lateinit var imageResource: Drawable

    @StringRes
    var titleResourceId = 0

    @StringRes
    var descriptionResourceId = 0

    @DrawableRes
    var imageResourceId = 0

    @ColorRes
    var titleColor = 0

    @ColorRes
    var descriptionColor = 0

    @ColorRes
    var backgroundColor = 0
    var titleTextSize = 0f
    var descriptionTextSize = 0f
    var iconWidth = 0
    var iconHeight = 0
    var marginTop = 0
    var marginLeft = 0
    var marginRight = 0
    var marginBottom = 0

    constructor(title: String, description: String) {
        this.title = title
        this.description = description
    }

    constructor(title: Int, description: Int) {
        titleResourceId = title
        descriptionResourceId = description
    }

    constructor(title: String, description: String, imageResourceId: Int) {
        this.title = title
        this.description = description
        this.imageResourceId = imageResourceId
    }

    constructor(title: String, description: String, imageResource: Drawable) {
        this.title = title
        this.description = description
        this.imageResource = imageResource
    }

    constructor(title: Int, description: Int, imageResourceId: Int) {
        titleResourceId = title
        descriptionResourceId = description
        this.imageResourceId = imageResourceId
    }

    constructor(title: Int, description: Int, imageResource: Drawable) {
        titleResourceId = title
        descriptionResourceId = description
        this.imageResource = imageResource
    }

    fun getTitle(): String {
        return title
    }

    @JvmName("getTitleResourceId1")
    fun getTitleResourceId(): Int {
        return titleResourceId
    }

    fun getDescription(): String {
        return description
    }

    @JvmName("getDescriptionResourceId1")
    fun getDescriptionResourceId(): Int {
        return descriptionResourceId
    }

    @JvmName("getTitleColor1")
    fun getTitleColor(): Int {
        return titleColor
    }

    @JvmName("getDescriptionColor1")
    fun getDescriptionColor(): Int {
        return descriptionColor
    }

    @JvmName("setTitleColor1")
    fun setTitleColor(color: Int) {
        titleColor = color
    }

    @JvmName("setDescriptionColor1")
    fun setDescriptionColor(color: Int) {
        descriptionColor = color
    }

    @JvmName("setImageResourceId1")
    fun setImageResourceId(imageResourceId: Int) {
        this.imageResourceId = imageResourceId
    }

    @JvmName("getImageResourceId1")
    fun getImageResourceId(): Int {
        return imageResourceId
    }

    @JvmName("getTitleTextSize1")
    fun getTitleTextSize(): Float {
        return titleTextSize
    }

    @JvmName("setTitleTextSize1")
    fun setTitleTextSize(titleTextSize: Float) {
        this.titleTextSize = titleTextSize
    }

    @JvmName("getDescriptionTextSize1")
    fun getDescriptionTextSize(): Float {
        return descriptionTextSize
    }

    @JvmName("setDescriptionTextSize1")
    fun setDescriptionTextSize(descriptionTextSize: Float) {
        this.descriptionTextSize = descriptionTextSize
    }

    @JvmName("getBackgroundColor1")
    fun getBackgroundColor(): Int {
        return backgroundColor
    }

    @JvmName("setBackgroundColor1")
    fun setBackgroundColor(backgroundColor: Int) {
        this.backgroundColor = backgroundColor
    }

    @JvmName("getIconWidth1")
    fun getIconWidth(): Int {
        return iconWidth
    }

    fun setIconLayoutParams(
        iconWidth: Int,
        iconHeight: Int,
        marginTop: Int,
        marginLeft: Int,
        marginRight: Int,
        marginBottom: Int
    ) {
        this.iconWidth = iconWidth
        this.iconHeight = iconHeight
        this.marginLeft = marginLeft
        this.marginRight = marginRight
        this.marginTop = marginTop
        this.marginBottom = marginBottom
    }

    @JvmName("getIconHeight1")
    fun getIconHeight(): Int {
        return iconHeight
    }

    @JvmName("getMarginTop1")
    fun getMarginTop(): Int {
        return marginTop
    }

    @JvmName("getMarginLeft1")
    fun getMarginLeft(): Int {
        return marginLeft
    }

    @JvmName("getMarginRight1")
    fun getMarginRight(): Int {
        return marginRight
    }

    @JvmName("getMarginBottom1")
    fun getMarginBottom(): Int {
        return marginBottom
    }
}
