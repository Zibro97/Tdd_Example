package com.zibro.fooddeliveryapp.data.repository.restaurant.review

import com.zibro.fooddeliveryapp.data.entity.review.RestaurantReviewEntity

interface RestaurantReviewRepository {
    suspend fun getReviews(restaurantTitle : String) : List<RestaurantReviewEntity>
}