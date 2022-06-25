package com.zibro.fooddeliveryapp.data.repository.map

import com.zibro.fooddeliveryapp.data.entity.LocationLatLngEntity
import com.zibro.fooddeliveryapp.data.network.MapApiService
import com.zibro.fooddeliveryapp.data.response.address.AddressInfo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DefaultMapRepository(
    private val mapApiService: MapApiService,
    private val ioDispatcher : CoroutineDispatcher
): MapRepository {
    override suspend fun getReverseGeoInformation(
        locationLatLngEntity: LocationLatLngEntity
    ): AddressInfo? = withContext(ioDispatcher) {
        val response = mapApiService.getReverseGeoCode(
            lat = locationLatLngEntity.latitude,
            lon = locationLatLngEntity.longitude
        )
        if(response.isSuccessful){
            response.body()?.addressInfo
        }else{
            null
        }
    }
}