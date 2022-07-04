package com.zibro.fooddeliveryapp.data.repository.restaurant.food

import com.zibro.fooddeliveryapp.data.db.dao.FoodMenuBasketDao
import com.zibro.fooddeliveryapp.data.entity.restaurant.RestaurantFoodEntity
import com.zibro.fooddeliveryapp.data.network.FoodApiService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DefaultRestaurantFoodRepository(
    private val foodApiService : FoodApiService,
    private val ioDispatcher: CoroutineDispatcher,
    private val foodMenuBasketDao: FoodMenuBasketDao
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

    override suspend fun getFoodMenuListInBasket(restaurantId: Long): List<RestaurantFoodEntity> = withContext(ioDispatcher){
        foodMenuBasketDao.getAllByRestaurantId(restaurantId)
    }

    override suspend fun getAllFoodMenuListInBasket(): List<RestaurantFoodEntity>  = withContext(ioDispatcher){
        foodMenuBasketDao.getAll()
    }

    override suspend fun insertFoodMenuInBasket(restaurantFoodEntity: RestaurantFoodEntity) = withContext(ioDispatcher){
        foodMenuBasketDao.insert(restaurantFoodEntity)
    }

    override suspend fun removeFoodMenuListInBasket(foodId: String) = withContext(ioDispatcher){
        foodMenuBasketDao.delete(foodId)
    }

    override suspend fun clearFoodMenuListInBasket() = withContext(ioDispatcher) {
        foodMenuBasketDao.deleteAll()
    }
}