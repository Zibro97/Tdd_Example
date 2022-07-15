package com.zibro.fooddeliveryapp.model.order

import com.zibro.fooddeliveryapp.data.entity.order.OrderEntity
import com.zibro.fooddeliveryapp.data.entity.restaurant.RestaurantFoodEntity
import com.zibro.fooddeliveryapp.model.CellType
import com.zibro.fooddeliveryapp.model.Model

data class OrderModel(
    override val id: Long,
    override val type: CellType = CellType.ORDER_CELL,
    val orderId : String,
    val userId : String,
    val restaurantId : Long,
    val foodMenuList : List<RestaurantFoodEntity>
): Model(id,type){
    fun toEntity() = OrderEntity(
        id = orderId,
        userId = userId,
        restaurantId = restaurantId,
        foodMenuList = foodMenuList
    )
}
