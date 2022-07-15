package com.zibro.fooddeliveryapp.data.repository.order

import com.zibro.fooddeliveryapp.data.entity.restaurant.RestaurantFoodEntity

interface OrderRepository {
    suspend fun orderMenu(userId : String,
                          restaurantId : Long,
                          foodMenuList : List<RestaurantFoodEntity>) : ResultState

    suspend fun getAllOrderMenus(
        userId:String
    ): ResultState
}