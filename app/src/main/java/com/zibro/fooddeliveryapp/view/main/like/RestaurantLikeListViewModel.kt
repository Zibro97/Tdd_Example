package com.zibro.fooddeliveryapp.view.main.like

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zibro.fooddeliveryapp.data.entity.RestaurantEntity
import com.zibro.fooddeliveryapp.data.repository.user.UserRepository
import com.zibro.fooddeliveryapp.model.CellType
import com.zibro.fooddeliveryapp.model.restaurant.RestaurantModel
import com.zibro.fooddeliveryapp.view.base.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class RestaurantLikeListViewModel(
    private val userRepository:UserRepository
):BaseViewModel() {
    val restaurantListLiveData = MutableLiveData<List<RestaurantModel>>()

    override fun fetchData(): Job = viewModelScope.launch{
        restaurantListLiveData.value = userRepository.getAllUserListedRestaurantList().map {
            RestaurantModel(
                id = it.id,
                type = CellType.LIKE_RESTAURANT_CELL,
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

    fun disLikeRestaurant(restaurant:RestaurantEntity) = viewModelScope.launch {
        userRepository.deleteUserLikedRestaurant(restaurantTitle = restaurant.restaurantTitle)
        fetchData()
    }
}