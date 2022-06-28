package com.zibro.fooddeliveryapp.view.main.home.restaurant.detail

import com.zibro.fooddeliveryapp.data.entity.RestaurantEntity

sealed class RestaurantDetailState{
    object Uninitialized: RestaurantDetailState()

    object Loading: RestaurantDetailState()

    data class Success(
        val restaurantEntity: RestaurantEntity,
        val isLiked:Boolean? = null
    ): RestaurantDetailState()
}
