package com.zibro.fooddeliveryapp.view.order

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.zibro.fooddeliveryapp.data.repository.restaurant.food.RestaurantFoodRepository
import com.zibro.fooddeliveryapp.model.CellType
import com.zibro.fooddeliveryapp.model.food.FoodModel
import com.zibro.fooddeliveryapp.view.base.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class OrderMenuListViewModel(
    private val restaurantFoodRepository: RestaurantFoodRepository
):BaseViewModel() {
    private val firebaseAuth by lazy { FirebaseAuth.getInstance()}

    val orderMenuStateLiveData = MutableLiveData<OrderMenuState>()

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

    fun orderMenu() {

    }

    fun clearOrderMenu() {

    }

    fun removeOrderMenu(model: FoodModel) {

    }
}