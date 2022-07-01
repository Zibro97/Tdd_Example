package com.zibro.fooddeliveryapp.data.repository.restaurant.food

import com.zibro.fooddeliveryapp.data.entity.restaurant.RestaurantFoodEntity

interface RestaurantFoodRepository {
    suspend fun getFoods(restaurantId : Long, restaurantTitle: String) : List<RestaurantFoodEntity>
}