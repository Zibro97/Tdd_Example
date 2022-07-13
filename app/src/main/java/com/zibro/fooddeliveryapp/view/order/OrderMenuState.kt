package com.zibro.fooddeliveryapp.view.order

import androidx.annotation.StringRes
import com.zibro.fooddeliveryapp.model.food.FoodModel

sealed class OrderMenuState{
    object Uninitialized: OrderMenuState()

    object Loading: OrderMenuState()

    data class Success(
        val restaurantFoodModelList: List<FoodModel>? = null
    ): OrderMenuState()

    object Order: OrderMenuState()

    data class Error(
        @StringRes val messageId: Int,
        val e: Throwable
    ): OrderMenuState()
}
