package com.zibro.fooddeliveryapp.data

import com.zibro.fooddeliveryapp.data.entity.order.OrderEntity
import com.zibro.fooddeliveryapp.data.entity.restaurant.RestaurantFoodEntity
import com.zibro.fooddeliveryapp.data.repository.order.DefaultOrderRepository
import com.zibro.fooddeliveryapp.data.repository.order.OrderRepository
import com.zibro.fooddeliveryapp.data.repository.order.ResultState

class TestOrderRepository : OrderRepository {

    private var orderEntities = mutableListOf<OrderEntity>()

    override suspend fun orderMenu(
        userId: String,
        restaurantId: Long,
        foodMenuList: List<RestaurantFoodEntity>,
        restaurantTitle : String
    ): ResultState {
        orderEntities.add(
            OrderEntity(
                id = orderEntities.size.toString(),
                userId = userId,
                restaurantId = restaurantId,
                foodMenuList = foodMenuList.map { it.copy() },
                restaurantTitle = restaurantTitle
            )
        )
        return ResultState.Success<Any>()
    }

    override suspend fun getAllOrderMenus(userId: String): ResultState {
        return ResultState.Success(orderEntities)
    }
}