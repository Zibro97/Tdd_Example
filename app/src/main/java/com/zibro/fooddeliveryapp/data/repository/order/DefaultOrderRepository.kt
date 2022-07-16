package com.zibro.fooddeliveryapp.data.repository.order

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.zibro.fooddeliveryapp.data.entity.order.OrderEntity
import com.zibro.fooddeliveryapp.data.entity.restaurant.RestaurantFoodEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.lang.Exception

class DefaultOrderRepository(
    private val ioDispatcher : CoroutineDispatcher,
    private val fireStore : FirebaseFirestore
) : OrderRepository {
    override suspend fun orderMenu(
        userId: String,
        restaurantId: Long,
        foodMenuList: List<RestaurantFoodEntity>,
        restaurantTitle : String
    ) : ResultState = withContext(ioDispatcher){
        val result : ResultState
        val orderMenuData = hashMapOf(
            "restaurantId" to restaurantId,
            "userId" to userId,
            "orderMenuList" to foodMenuList,
            "restaurantTitle" to restaurantTitle
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

    override suspend fun getAllOrderMenus(userId: String): ResultState = withContext(ioDispatcher) {
        return@withContext try{
            val result : QuerySnapshot = fireStore
                .collection("order")
                .whereEqualTo("userId",userId)
                .get()
                .await()
            ResultState.Success(result.documents.map {
                OrderEntity(
                    id = it.id,
                    userId = it.get("userId") as String,
                    restaurantId = it.get("restaurantId") as Long,
                    foodMenuList = (it.get("orderMenuList") as ArrayList<Map<String, Any>>).map{ food ->
                        RestaurantFoodEntity(
                            id = food["id"] as String,
                            title = food["title"] as String,
                            description = food["description"] as String,
                            price = (food["price"] as Long).toInt(),
                            imageUrl = food["imageUrl"] as String,
                            restaurantId = food["restaurantId"] as Long,
                            restaurantTitle = food["restaurantTitle"] as String
                        )
                    },
                    restaurantTitle = it.get("restaurantTitle") as String
                )
            })
        }catch (e: Exception){
            e.printStackTrace()
            ResultState.Error(e)
        }
    }
}