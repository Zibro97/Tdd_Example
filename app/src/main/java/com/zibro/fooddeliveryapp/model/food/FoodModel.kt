package com.zibro.fooddeliveryapp.model.food

import com.zibro.fooddeliveryapp.model.CellType
import com.zibro.fooddeliveryapp.model.Model

data class FoodModel(
    override val id: Long,
    override val type: CellType = CellType.Food_CELL,
    val title: String,
    val description: String,
    val price: Int,
    val imageUrl: String,
    val restaurantId: Long,
    val restaurantTitle: String
) : Model(id, type)
