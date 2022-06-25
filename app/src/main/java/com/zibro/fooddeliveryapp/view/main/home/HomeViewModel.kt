package com.zibro.fooddeliveryapp.view.main.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zibro.fooddeliveryapp.R
import com.zibro.fooddeliveryapp.data.entity.LocationLatLngEntity
import com.zibro.fooddeliveryapp.data.entity.MapSearchInfoEntity
import com.zibro.fooddeliveryapp.data.repository.map.MapRepository
import com.zibro.fooddeliveryapp.view.base.BaseViewModel
import kotlinx.coroutines.launch

class HomeViewModel(
    private val mapRepository: MapRepository
) : BaseViewModel() {
    //State-Pattern LiveData
    val homeStateLiveData = MutableLiveData<HomeState>(HomeState.Uninitialized)

    fun loadReverseGeoInformation(locationLatLngEntity: LocationLatLngEntity) = viewModelScope.launch{
        val addressInfo =  mapRepository.getReverseGeoInformation(locationLatLngEntity)
        homeStateLiveData.value = HomeState.Loading
        addressInfo?.let { info ->
            homeStateLiveData.value = HomeState.Success(
                mapSearchInfoEntity = info.toSearchInfoEntity(locationLatLngEntity)
            )
        } ?: kotlin.run {
            homeStateLiveData.value = HomeState.Error(
                R.string.can_not_load_address_info
            )
        }
    }

    fun getMapSearchInfo() : MapSearchInfoEntity? {
        when(val data = homeStateLiveData.value){
            is HomeState.Success -> {
                return data.mapSearchInfoEntity
            }
        }
        return null
    }

    companion object{
        const val MY_LOCATION_KEY ="MyLocation"
    }

}