package com.zibro.fooddeliveryapp.data.repository.order

import com.google.firebase.firestore.FirebaseFirestore
import com.zibro.fooddeliveryapp.data.entity.restaurant.RestaurantFoodEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.lang.Exception

class DefaultOrderRepository(
    private val ioDispatcher : CoroutineDispatcher,
    private val fireStore : FirebaseFirestore
) : OrderRepository {
    override suspend fun orderMenu(
        userId: String,
        restaurantId: Long,
        foodMenuList: List<RestaurantFoodEntity>
    ) : ResultState = withContext(ioDispatcher){
        val result : ResultState
        val orderMenuData = hashMapOf(
            "restaurantId" to restaurantId,
            "userId" to userId,
            "orderMenuList" to foodMenuList
        )
        result = try {
            fireStore.collection("order")
                .add(orderMenuData)
            ResultState.Success<Any>()
        }catch (e : Exception){
            e.printStackTrace()
            ResultState.Error(e)
        }
        return@withContext result
    }
}