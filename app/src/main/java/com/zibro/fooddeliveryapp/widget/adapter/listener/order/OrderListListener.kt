package com.zibro.fooddeliveryapp.widget.adapter.listener.order

import com.zibro.fooddeliveryapp.widget.adapter.listener.AdapterListener

interface OrderListListener: AdapterListener {
    fun writeRestaurantReview(orderId : String, restaurantTitle:String)
}