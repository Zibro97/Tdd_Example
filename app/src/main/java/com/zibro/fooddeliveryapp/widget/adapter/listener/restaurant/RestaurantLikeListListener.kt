package com.zibro.fooddeliveryapp.widget.adapter.listener.restaurant

import com.zibro.fooddeliveryapp.model.restaurant.RestaurantModel
import com.zibro.fooddeliveryapp.widget.adapter.listener.AdapterListener

interface RestaurantLikeListListener : AdapterListener {
    fun onDisLikeItem(model : RestaurantModel)

    fun onClickItem(model : RestaurantModel)
}