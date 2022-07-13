package com.zibro.fooddeliveryapp.widget.adapter.listener.restaurant

import com.zibro.fooddeliveryapp.model.food.FoodModel
import com.zibro.fooddeliveryapp.widget.adapter.listener.AdapterListener

interface OrderMenuListListener : AdapterListener {
    fun onRemoveItem(model : FoodModel)
}