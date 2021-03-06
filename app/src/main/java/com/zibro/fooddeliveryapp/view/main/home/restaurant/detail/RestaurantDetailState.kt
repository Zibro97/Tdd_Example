package com.zibro.fooddeliveryapp.view.main.home.restaurant.detail

import com.zibro.fooddeliveryapp.data.entity.RestaurantEntity
import com.zibro.fooddeliveryapp.data.entity.restaurant.RestaurantFoodEntity

sealed class RestaurantDetailState{
    object Uninitialized: RestaurantDetailState()

    object Loading: RestaurantDetailState()

    data class Success(
        val restaurantEntity: RestaurantEntity,
        val restaurantFoodList : List<RestaurantFoodEntity>? = null,
        val foodMenuListInBasket : List<RestaurantFoodEntity>? = null,
        val isClearNeedInBasketAndAction : Pair<Boolean, ()-> Unit> = Pair(false, {}),
        val isLiked:Boolean? = null
    ): RestaurantDetailState()
}
