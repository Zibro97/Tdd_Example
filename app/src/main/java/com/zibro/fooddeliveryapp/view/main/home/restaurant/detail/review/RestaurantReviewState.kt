package com.zibro.fooddeliveryapp.view.main.home.restaurant.detail.review

import com.zibro.fooddeliveryapp.data.entity.review.RestaurantReviewEntity
import com.zibro.fooddeliveryapp.model.review.RestaurantReviewModel

sealed class RestaurantReviewState{
    object Uninitialized : RestaurantReviewState()

    object Loading : RestaurantReviewState()

    data class Success(
        val reviewList : List<RestaurantReviewModel>
    ) : RestaurantReviewState()
}
