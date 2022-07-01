package com.zibro.fooddeliveryapp.view.main.home.restaurant.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zibro.fooddeliveryapp.data.entity.RestaurantEntity
import com.zibro.fooddeliveryapp.data.repository.restaurant.food.RestaurantFoodRepository
import com.zibro.fooddeliveryapp.data.repository.user.UserRepository
import com.zibro.fooddeliveryapp.view.base.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class RestaurantDetailViewModel(
    private val restaurantEntity: RestaurantEntity,
    private val restaurantFoodRepository : RestaurantFoodRepository,
    private val userRepository: UserRepository
) : BaseViewModel() {
    val restaurantDetailStateLiveData = MutableLiveData<RestaurantDetailState>(RestaurantDetailState.Uninitialized)

    override fun fetchData(): Job = viewModelScope.launch{
        restaurantDetailStateLiveData.value = RestaurantDetailState.Success(
            restaurantEntity = restaurantEntity
        )
        restaurantDetailStateLiveData.value = RestaurantDetailState.Loading
        val foods = restaurantFoodRepository.getFoods(restaurantEntity.restaurantInfoId, restaurantEntity.restaurantTitle)
        val isLiked = userRepository.getUserLikedRestaurant(restaurantEntity.restaurantTitle) != null
        restaurantDetailStateLiveData.value = RestaurantDetailState.Success(
            restaurantEntity,
            foods,
            isLiked
        )
    }

    fun getRestaurantTelNumber(): String?  {
        return when(val data = restaurantDetailStateLiveData.value){
            is RestaurantDetailState.Success ->{
                data.restaurantEntity.restaurantTelNumber
            }
            else -> null
        }
    }

    fun getRestaurantInfo():RestaurantEntity? {
        return when(val data = restaurantDetailStateLiveData.value){
            is RestaurantDetailState.Success ->{
                data.restaurantEntity
            }
            else -> null
        }
    }

    fun toggleLikedRestaurant() = viewModelScope.launch {
        when(val data = restaurantDetailStateLiveData.value) {
            is RestaurantDetailState.Success -> {
                userRepository.getUserLikedRestaurant(restaurantEntity.restaurantTitle)?.let {
                    userRepository.deleteUserLikedRestaurant(it.restaurantTitle)
                    restaurantDetailStateLiveData.value = data.copy(
                        isLiked = false
                    )
                }?: kotlin.run {
                    userRepository.insertUserLikedRestaurant(restaurantEntity)
                    restaurantDetailStateLiveData.value = data.copy(
                        isLiked = true
                    )
                }
            }
        }
    }
}