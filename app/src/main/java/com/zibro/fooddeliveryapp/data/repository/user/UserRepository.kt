package com.zibro.fooddeliveryapp.data.repository.user

import com.zibro.fooddeliveryapp.data.entity.LocationLatLngEntity
import com.zibro.fooddeliveryapp.data.entity.RestaurantEntity

interface UserRepository {
    suspend fun getUserLocation() : LocationLatLngEntity?

    suspend fun insertUserLocation(locationLatLngEntity: LocationLatLngEntity)

    suspend fun getUserLikedRestaurant(restaurantTitle:String) : RestaurantEntity?

    suspend fun insertUserLikedRestaurant(restaurantEntity: RestaurantEntity)

    suspend fun deleteUserLikedRestaurant(restaurantTitle: String)

    suspend fun deleteALlUserLikedRestaurant()

    suspend fun getAllUserListedRestaurantList() : List<RestaurantEntity>
}