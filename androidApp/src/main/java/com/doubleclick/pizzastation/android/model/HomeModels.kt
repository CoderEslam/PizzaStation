package com.doubleclick.pizzastation.android.model

import com.doubleclick.pizzastation.android.`interface`.itemListener
import kotlin.properties.Delegates

/**
 * Created By Eslam Ghazy on 1/24/2023
 */
class HomeModels {

    constructor(type: Int, itemListener: itemListener, categoryList: CategoryList) {
        this.type = type
        this.itemListener = itemListener
        this.categoryList = categoryList
    }

    constructor(type: Int, menuList: MenuList) {
        this.type = type
        this.menuList = menuList
    }

    var type by Delegates.notNull<Int>()
    lateinit var itemListener: itemListener
    lateinit var categoryList: CategoryList
    lateinit var menuList: MenuList


}