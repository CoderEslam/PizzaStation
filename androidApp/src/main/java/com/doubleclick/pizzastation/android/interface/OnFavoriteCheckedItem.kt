package com.doubleclick.pizzastation.android.`interface`

import com.doubleclick.pizzastation.android.model.FavoriteModel

interface OnFavoriteCheckedItem {
    fun onFavoriteChecked(postion: Int, favoriteModel: FavoriteModel);
}