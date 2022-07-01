package com.zibro.fooddeliveryapp.view.main.home.restaurant.detail

import com.zibro.fooddeliveryapp.data.entity.RestaurantEntity
import com.zibro.fooddeliveryapp.data.entity.restaurant.RestaurantFoodEntity

sealed class RestaurantDetailState{
    object Uninitialized: RestaurantDetailState()

    object Loading: RestaurantDetailState()

    data class Success(
        val restaurantEntity: RestaurantEntity,
        val restaurantFoodList : List<RestaurantFoodEntity>? = null,
        val isLiked:Boolean? = null
    ): RestaurantDetailState()
}
