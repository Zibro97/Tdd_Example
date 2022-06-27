package com.zibro.fooddeliveryapp.view.mylocation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zibro.fooddeliveryapp.R
import com.zibro.fooddeliveryapp.data.entity.LocationLatLngEntity
import com.zibro.fooddeliveryapp.data.entity.MapSearchInfoEntity
import com.zibro.fooddeliveryapp.data.repository.map.MapRepository
import com.zibro.fooddeliveryapp.data.repository.user.UserRepository
import com.zibro.fooddeliveryapp.view.base.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MyLocationViewModel(
    private val mapSearchInfoEntity: MapSearchInfoEntity,
    private val mapRepository: MapRepository,
    private val userRepository: UserRepository
):BaseViewModel() {
    val myLocationStateLiveData = MutableLiveData<MyLocationState>(MyLocationState.UnInitialized)

    override fun fetchData(): Job = viewModelScope.launch{
        myLocationStateLiveData.value = MyLocationState.Loading
        myLocationStateLiveData.value = MyLocationState.Success(
            mapSearchInfoEntity
        )
    }

    fun changeLocationInfo(
        locationLatLngEntity : LocationLatLngEntity
    ) = viewModelScope.launch {
        val addressInfo =  mapRepository.getReverseGeoInformation(locationLatLngEntity)
        myLocationStateLiveData.value = MyLocationState.Loading
        addressInfo?.let { info ->
            myLocationStateLiveData.value = MyLocationState.Success(
                mapSearchInfoEntity = info.toSearchInfoEntity(locationLatLngEntity)
            )
        } ?: kotlin.run {
            myLocationStateLiveData.value = MyLocationState.Error(
                R.string.can_not_load_address_info
            )
        }
    }

    fun confirmSelectLocation() = viewModelScope.launch {
        when(val data = myLocationStateLiveData.value){
            is MyLocationState.Success -> {
                // TODO: 2022/06/26 유저 Location 관리하는 로직 현재 유저의 위치와 저장된 위치가 상이할 경우 고려해야함
                userRepository.insertUserLocation(data.mapSearchInfoEntity.locationLatLngEntity)
                myLocationStateLiveData.value = MyLocationState.Confirm(
                    data.mapSearchInfoEntity
                )
            }
        }
    }
}