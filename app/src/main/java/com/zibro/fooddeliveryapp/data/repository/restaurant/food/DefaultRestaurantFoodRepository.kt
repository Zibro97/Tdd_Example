package com.zibro.fooddeliveryapp.data.repository.restaurant.food

import com.zibro.fooddeliveryapp.data.entity.restaurant.RestaurantFoodEntity
import com.zibro.fooddeliveryapp.data.network.FoodApiService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DefaultRestaurantFoodRepository(
    private val foodApiService : FoodApiService,
    private val ioDispatcher: CoroutineDispatcher
) : RestaurantFoodRepository {
    override suspend fun getFoods(restaurantId: Long, restaurantTitle: String): List<RestaurantFoodEntity> = withContext(ioDispatcher){
        val response = foodApiService.getRestaurantFoods(restaurantId)
        if (response.isSuccessful){
            response.body()?.map {
                it.toEntity(restaurantId,restaurantTitle)
            } ?: listOf()
        } else{
            listOf()
        }
    }
}