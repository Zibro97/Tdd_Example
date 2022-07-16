package com.zibro.fooddeliveryapp.view.order

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.zibro.fooddeliveryapp.R
import com.zibro.fooddeliveryapp.data.repository.order.OrderRepository
import com.zibro.fooddeliveryapp.data.repository.order.ResultState
import com.zibro.fooddeliveryapp.data.repository.restaurant.food.RestaurantFoodRepository
import com.zibro.fooddeliveryapp.model.CellType
import com.zibro.fooddeliveryapp.model.food.FoodModel
import com.zibro.fooddeliveryapp.view.base.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class OrderMenuListViewModel(
    private val restaurantFoodRepository: RestaurantFoodRepository,
    private val orderRepository: OrderRepository,
    private val firebaseAuth: FirebaseAuth
):BaseViewModel() {

    val orderMenuStateLiveData = MutableLiveData<OrderMenuState>(OrderMenuState.Uninitialized)

    override fun fetchData(): Job = viewModelScope.launch{
        orderMenuStateLiveData.value = OrderMenuState.Loading
        val foodMenuList = restaurantFoodRepository.getAllFoodMenuListInBasket()
        orderMenuStateLiveData.value = OrderMenuState.Success(
            foodMenuList.map {
                FoodModel(
                    id = it.hashCode().toLong(),
                    type = CellType.ORDER_FOOD_CELL,
                    title = it.title,
                    description = it.description,
                    price = it.price,
                    imageUrl = it.imageUrl,
                    restaurantId = it.restaurantId,
                    restaurantTitle = it.restaurantTitle,
                    foodId = it.id
                )
            }
        )

    }

    fun orderMenu() = viewModelScope.launch{
        val foodMenuList = restaurantFoodRepository.getAllFoodMenuListInBasket()
        if(foodMenuList.isNotEmpty()){
            val restaurantId = foodMenuList.first().restaurantId
            val restaurantTitle = foodMenuList.first().restaurantTitle
            firebaseAuth.currentUser?.let { user->
                when(val data = orderRepository.orderMenu(userId = user.uid,restaurantId = restaurantId, foodMenuList = foodMenuList, restaurantTitle = restaurantTitle)){
                    is ResultState.Success<*> -> {
                        restaurantFoodRepository.clearFoodMenuListInBasket()
                        orderMenuStateLiveData.value = OrderMenuState.Order
                    }
                    is ResultState.Error -> {
                        orderMenuStateLiveData.value = OrderMenuState.Error(
                            R.string.request_error, data.e
                        )
                    }
                }
            } ?: kotlin.run {
                orderMenuStateLiveData.value = OrderMenuState.Error(
                    R.string.user_id_not_found, IllegalAccessException()
                )
            }
        }
    }

    fun clearOrderMenu() = viewModelScope.launch{
        restaurantFoodRepository.clearFoodMenuListInBasket()
        fetchData()
    }

    fun removeOrderMenu(model: FoodModel) = viewModelScope.launch{
        restaurantFoodRepository.removeFoodMenuListInBasket(model.foodId)
        fetchData()
    }
}