package com.zibro.fooddeliveryapp.view.main.home.restaurant.detail.menu

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zibro.fooddeliveryapp.data.entity.restaurant.RestaurantFoodEntity
import com.zibro.fooddeliveryapp.data.repository.restaurant.food.RestaurantFoodRepository
import com.zibro.fooddeliveryapp.model.food.FoodModel
import com.zibro.fooddeliveryapp.view.base.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class RestaurantMenuListViewModel(
    private val restaurantId : Long,
    private val foodEntityList : List<RestaurantFoodEntity>,
    private val restaurantFoodRepository: RestaurantFoodRepository
) : BaseViewModel() {

    val restaurantFoodListLiveData = MutableLiveData<List<FoodModel>>()

    val menuBasketLiveData = MutableLiveData<RestaurantFoodEntity>()

    val isClearNeedInBasketLiveData = MutableLiveData<Pair<Boolean,() -> Unit>>()

    override fun fetchData(): Job = viewModelScope.launch {
        restaurantFoodListLiveData.value = foodEntityList.map {
            FoodModel(
                id = it.hashCode().toLong(),
                title = it.title,
                description = it.description,
                price = it.price,
                imageUrl = it.imageUrl,
                restaurantId = restaurantId,
                restaurantTitle = it.restaurantTitle,
                foodId = it.id
            )
        }
    }

    fun insertMenuInBasket(model: FoodModel) = viewModelScope.launch{
        val restaurantMenuListInBasket = restaurantFoodRepository.getFoodMenuListInBasket(restaurantId)
        val foodMenuEntity = model.toEntity(restaurantMenuListInBasket.size)
        val anotherRestaurantMenuListInBasket = restaurantFoodRepository.getAllFoodMenuListInBasket().filter{
            it.restaurantId != restaurantId
        }
        //다른 식당에 있는 장바구니 리스트가 비어있지 않을때 비움
        if(anotherRestaurantMenuListInBasket.isNotEmpty()){
            isClearNeedInBasketLiveData.value = Pair(true,{clearMenuAndInsertNewMenuBasket(foodMenuEntity)})
        } else {
            restaurantFoodRepository.insertFoodMenuInBasket(foodMenuEntity)
            menuBasketLiveData.value = foodMenuEntity
        }
    }

    private fun clearMenuAndInsertNewMenuBasket(foodMenuEntity : RestaurantFoodEntity) = viewModelScope.launch {
        restaurantFoodRepository.clearFoodMenuListInBasket()
        restaurantFoodRepository.insertFoodMenuInBasket(foodMenuEntity)
        menuBasketLiveData.value = foodMenuEntity
    }

}