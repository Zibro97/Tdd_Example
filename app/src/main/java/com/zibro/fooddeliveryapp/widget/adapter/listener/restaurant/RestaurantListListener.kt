package com.zibro.fooddeliveryapp.widget.adapter.listener.restaurant

import com.zibro.fooddeliveryapp.model.restaurant.RestaurantModel
import com.zibro.fooddeliveryapp.widget.adapter.listener.AdapterListener

interface RestaurantListListener : AdapterListener{
    fun onClickItem(model : RestaurantModel)
}