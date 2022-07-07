package com.zibro.fooddeliveryapp.model.food

import com.zibro.fooddeliveryapp.data.entity.restaurant.RestaurantFoodEntity
import com.zibro.fooddeliveryapp.model.CellType
import com.zibro.fooddeliveryapp.model.Model

data class FoodModel(
    override val id: Long,
    override val type: CellType = CellType.FOOD_CELL,
    val title: String,
    val description: String,
    val price: Int,
    val imageUrl: String,
    val restaurantId: Long,
    val restaurantTitle: String,
    val foodId : String
) : Model(id, type) {

    fun toEntity(basketIdx : Int) = RestaurantFoodEntity(
        "${foodId}_${basketIdx}",
        title,
        description,
        price,
        imageUrl,
        restaurantId,
        restaurantTitle
    )

}
