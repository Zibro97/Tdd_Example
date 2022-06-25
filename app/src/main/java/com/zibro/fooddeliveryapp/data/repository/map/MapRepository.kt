package com.zibro.fooddeliveryapp.data.repository.map

import com.zibro.fooddeliveryapp.data.entity.LocationLatLngEntity
import com.zibro.fooddeliveryapp.data.response.address.AddressInfo

interface MapRepository {
    suspend fun getReverseGeoInformation(locationLatLngEntity: LocationLatLngEntity) : AddressInfo?
}