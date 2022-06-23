package com.zibro.fooddeliveryapp.data.entity

import android.os.Parcelable
import com.zibro.fooddeliveryapp.view.main.home.restaurant.RestaurantCategory
import kotlinx.parcelize.Parcelize


@Parcelize
data class RestaurantEntity(
    //List내 데이터의 고유 id
    override val id: Long,
    //Api의 데이터 id
    val restaurantInfoId : Long,
    val restaurantCategory : RestaurantCategory,
    val restaurantTitle : String,
    val restaurantImageUrl : String,
    val grade : Float,
    val reviewCount : Int,
    val deliveryTimeRange : Pair<Int,Int>,
    val deliveryTipRange : Pair<Int,Int>
) : Entity, Parcelable