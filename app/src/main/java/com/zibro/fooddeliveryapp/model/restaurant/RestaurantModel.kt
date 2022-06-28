package com.zibro.fooddeliveryapp.model.restaurant

import com.zibro.fooddeliveryapp.data.entity.RestaurantEntity
import com.zibro.fooddeliveryapp.model.CellType
import com.zibro.fooddeliveryapp.model.Model
import com.zibro.fooddeliveryapp.view.main.home.restaurant.RestaurantCategory

data class RestaurantModel(
    override val id: Long,
    override val type: CellType = CellType.RESTAURANT_CELL,
    val restaurantInfoId : Long,
    val restaurantCategory : RestaurantCategory,
    val restaurantTitle : String,
    val restaurantImageUrl : String,
    val grade : Float,
    val reviewCount : Int,
    val deliveryTimeRange : Pair<Int,Int>,
    val deliveryTipRange : Pair<Int,Int>,
    val restaurantTelNumber : String?
) : Model(id,type) {
    fun toEntity() = RestaurantEntity(
        id,
        restaurantInfoId,
        restaurantCategory,
        restaurantTitle,
        restaurantImageUrl,
        grade,
        reviewCount,
        deliveryTimeRange,
        deliveryTipRange,
        restaurantTelNumber
    )
}
