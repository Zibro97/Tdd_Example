package com.zibro.fooddeliveryapp.view.main.home.restaurant

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zibro.fooddeliveryapp.data.entity.LocationLatLngEntity
import com.zibro.fooddeliveryapp.data.repository.restaurant.RestaurantRepository
import com.zibro.fooddeliveryapp.model.restaurant.RestaurantModel
import com.zibro.fooddeliveryapp.view.base.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class RestaurantListViewModel(
    private val restaurantCategory: RestaurantCategory,
    private var locationLatLngEntity: LocationLatLngEntity,
    private val restaurantRepository: RestaurantRepository,
    private var restaurantOrder: RestaurantOrder = RestaurantOrder.DEFAULT
):BaseViewModel() {
    val restaurantListLiveData = MutableLiveData<List<RestaurantModel>>()

    override fun fetchData(): Job = viewModelScope.launch {
        val restaurantList = restaurantRepository.getList(restaurantCategory,locationLatLngEntity)
        val sortedList = when(restaurantOrder){
            RestaurantOrder.DEFAULT -> {
                restaurantList
            }
            RestaurantOrder.LOW_DELIVERY_TIP ->{
                restaurantList.sortedBy { it.deliveryTipRange.first }
            }
            RestaurantOrder.FAST_DELIVERY ->{
                restaurantList.sortedBy { it.deliveryTimeRange.first }
            }
            RestaurantOrder.TOP_RATE ->{
                restaurantList.sortedByDescending { it.grade }
            }
        }
        restaurantListLiveData.value = sortedList.map {
            RestaurantModel(
                id = it.id,
                restaurantInfoId = it.restaurantInfoId,
                restaurantCategory = it.restaurantCategory,
                restaurantTitle = it.restaurantTitle,
                restaurantImageUrl = it.restaurantImageUrl,
                grade = it.grade,
                reviewCount = it.reviewCount,
                deliveryTimeRange = it.deliveryTimeRange,
                deliveryTipRange = it.deliveryTipRange,
                restaurantTelNumber = it.restaurantTelNumber
            )
        }
    }

    fun setLocationLatLng(locationLatLngEntity: LocationLatLngEntity){
        this.locationLatLngEntity = locationLatLngEntity
        fetchData()
    }

    fun setRestaurantOrder(order: RestaurantOrder){
        this.restaurantOrder = order
        fetchData()
    }
}