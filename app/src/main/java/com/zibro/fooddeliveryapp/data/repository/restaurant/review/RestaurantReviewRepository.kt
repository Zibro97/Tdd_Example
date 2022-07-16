package com.zibro.fooddeliveryapp.data.repository.restaurant.review

import com.zibro.fooddeliveryapp.data.repository.order.ResultState

interface RestaurantReviewRepository {
    suspend fun getReviews(restaurantTitle : String) : ResultState
}