package com.zibro.fooddeliveryapp.data.network

import com.zibro.fooddeliveryapp.data.response.restaurant.RestaurantFoodResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface FoodApiService {
    @GET("restaurants/{restaurantId}/foods")
    suspend fun getRestaurantFoods(
        @Path("restaurantId") restaurantId: Long
    ): Response<List<RestaurantFoodResponse>>
}