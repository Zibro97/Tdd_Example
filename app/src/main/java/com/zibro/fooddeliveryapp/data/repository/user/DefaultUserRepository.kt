package com.zibro.fooddeliveryapp.data.repository.user

import com.zibro.fooddeliveryapp.data.db.dao.LocationDao
import com.zibro.fooddeliveryapp.data.entity.LocationLatLngEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DefaultUserRepository(
    private val locationDao: LocationDao,
    private val ioDispatcher: CoroutineDispatcher
) : UserRepository {

    override suspend fun getUserLocation(): LocationLatLngEntity? = withContext(ioDispatcher) {
        locationDao.get(-1)
    }

    override suspend fun insertUserLocation(locationLatLngEntity: LocationLatLngEntity) = withContext(ioDispatcher) {
        locationDao.insert(locationLatLngEntity)
    }
}