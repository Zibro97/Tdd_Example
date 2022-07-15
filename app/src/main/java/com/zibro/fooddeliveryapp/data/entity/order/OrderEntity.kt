package com.zibro.fooddeliveryapp.data.entity.order

import com.zibro.fooddeliveryapp.data.entity.restaurant.RestaurantFoodEntity

data class OrderEntity(
    val id : String,
    val userId :String,
    val restaurantId : Long,
    val foodMenuList : List<RestaurantFoodEntity>
)
