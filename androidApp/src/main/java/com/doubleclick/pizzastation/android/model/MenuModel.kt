package com.doubleclick.pizzastation.android.model

import android.os.Parcel
import android.os.Parcelable

data class MenuModel(
    val FB: String?,
    val L: String?,
    val M: String?,
    val Slice: String?,
    val XXL: String?,
    val category: String?,
    val created_at: String?,
    val half_L: String?,
    val half_stuffed_crust_L: String?,
    val id: Int,
    val image: String?,
    val name: String?,
    val quarter_XXL: String?,
    val status: String?,
    val stuffed_crust_L: String?,
    val stuffed_crust_M: String?,
    val updated_at: String?,
    var choose_0_1: Int = 0
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(FB)
        parcel.writeString(L)
        parcel.writeString(M)
        parcel.writeString(Slice)
        parcel.writeString(XXL)
        parcel.writeString(category)
        parcel.writeString(created_at)
        parcel.writeString(half_L)
        parcel.writeString(half_stuffed_crust_L)
        parcel.writeInt(id)
        parcel.writeString(image)
        parcel.writeString(name)
        parcel.writeString(quarter_XXL)
        parcel.writeString(status)
        parcel.writeString(stuffed_crust_L)
        parcel.writeString(stuffed_crust_M)
        parcel.writeString(updated_at)
        parcel.writeInt(choose_0_1)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MenuModel> {
        override fun createFromParcel(parcel: Parcel): MenuModel {
            return MenuModel(parcel)
        }

        override fun newArray(size: Int): Array<MenuModel?> {
            return arrayOfNulls(size)
        }
    }
}