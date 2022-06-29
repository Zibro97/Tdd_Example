package com.zibro.fooddeliveryapp.data.entity

import android.os.Parcelable
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.zibro.fooddeliveryapp.util.converter.RoomTypeConverters
import com.zibro.fooddeliveryapp.view.main.home.restaurant.RestaurantCategory
import kotlinx.parcelize.Parcelize


@Parcelize
@androidx.room.Entity
@TypeConverters(RoomTypeConverters::class)
data class RestaurantEntity(
    //List내 데이터의 고유 id
    override val id: Long,
    //Api의 데이터 id
    val restaurantInfoId : Long,
    val restaurantCategory : RestaurantCategory,
    @PrimaryKey
    val restaurantTitle : String,
    val restaurantImageUrl : String,
    val grade : Float,
    val reviewCount : Int,
    val deliveryTimeRange : Pair<Int,Int>,
    val deliveryTipRange : Pair<Int,Int>,
    val restaurantTelNumber : String?
) : Entity, Parcelable