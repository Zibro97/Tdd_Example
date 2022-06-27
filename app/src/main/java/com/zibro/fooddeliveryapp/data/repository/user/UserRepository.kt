package com.zibro.fooddeliveryapp.data.repository.user

import com.zibro.fooddeliveryapp.data.entity.LocationLatLngEntity

interface UserRepository {
    suspend fun getUserLocation() : LocationLatLngEntity?

    suspend fun insertUserLocation(locationLatLngEntity: LocationLatLngEntity)
}