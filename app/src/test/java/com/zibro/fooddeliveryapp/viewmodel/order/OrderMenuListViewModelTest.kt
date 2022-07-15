package com.zibro.fooddeliveryapp.viewmodel.order

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.zibro.fooddeliveryapp.data.entity.order.OrderEntity
import com.zibro.fooddeliveryapp.data.entity.restaurant.RestaurantFoodEntity
import com.zibro.fooddeliveryapp.data.repository.order.DefaultOrderRepository
import com.zibro.fooddeliveryapp.data.repository.order.OrderRepository
import com.zibro.fooddeliveryapp.data.repository.order.ResultState
import com.zibro.fooddeliveryapp.data.repository.restaurant.food.RestaurantFoodRepository
import com.zibro.fooddeliveryapp.model.CellType
import com.zibro.fooddeliveryapp.model.food.FoodModel
import com.zibro.fooddeliveryapp.view.order.OrderMenuListViewModel
import com.zibro.fooddeliveryapp.view.order.OrderMenuState
import com.zibro.fooddeliveryapp.viewmodel.ViewModelTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import org.koin.core.parameter.parametersOf
import org.koin.test.inject
import org.mockito.Mock
import org.mockito.Mockito

/**
 * 1. 장바구니 메뉴 담기
 * 2. 장바구니에 담은 메뉴를 리스트로 뿌려줌
 * 3. 장바구니 목록에 있는 데이터를 갖고 주문
* */
@ExperimentalCoroutinesApi
internal class OrderMenuListViewModelTest:ViewModelTest() {

    @Mock
    lateinit var firebaseAuth : FirebaseAuth

    @Mock
    lateinit var firebaseUser: FirebaseUser

    private val orderMenuListViewModel by inject<OrderMenuListViewModel> {
        parametersOf(firebaseAuth)
    }

    private val restaurantFoodRepository by inject<RestaurantFoodRepository>()

    private val orderRepository by inject<OrderRepository>()

    private val restaurantId = 0L
    private val restaurantTitle = "식당명"

    //1. 장바구니 메뉴 담기
    @Test
    fun `insert food menu in basket`() = runBlockingTest {
        (0 until 10).forEach{
            restaurantFoodRepository.insertFoodMenuInBasket(
                RestaurantFoodEntity(
                    id = it.toString(),
                    title = "메뉴 $it",
                    description = "소개 $it",
                    price = it,
                    imageUrl = "",
                    restaurantId = restaurantId,
                    restaurantTitle = restaurantTitle
                )
            )
        }
        assert(restaurantFoodRepository.getAllFoodMenuListInBasket().size == 10)
    }

    //2.장바구니에 담은 메뉴를 리스트 테스트
    @Test
    fun `test load order menu list`() = runBlockingTest {
        val testObservable = orderMenuListViewModel.orderMenuStateLiveData.test()
        orderMenuListViewModel.fetchData()

        testObservable.assertValueSequence(
            listOf(
                OrderMenuState.Uninitialized,
                OrderMenuState.Loading,
                OrderMenuState.Success(
                    restaurantFoodModelList = restaurantFoodRepository.getAllFoodMenuListInBasket()
                        .map {
                            FoodModel(
                                id = it.hashCode().toLong(),
                                type = CellType.ORDER_FOOD_CELL,
                                title = it.title,
                                description = it.description,
                                price = it.price,
                                imageUrl = it.imageUrl,
                                restaurantId = it.restaurantId,
                                foodId = it.id,
                                restaurantTitle = it.restaurantTitle
                            )
                        }
                )
            )
        )
    }

    //3.
    @Test
    fun `test do order menu list`() = runBlockingTest {
        val userId = "asdf"
        Mockito.`when`(firebaseAuth.currentUser).then { firebaseUser }
        Mockito.`when`(firebaseUser.uid).then { userId }

        val testObservable = orderMenuListViewModel.orderMenuStateLiveData.test()
        orderMenuListViewModel.fetchData()

        val menuListInBasket = restaurantFoodRepository.getAllFoodMenuListInBasket().map { it.copy() }

        val menuListInBasketModel = menuListInBasket.map {
            FoodModel(
                id = it.hashCode().toLong(),
                type = CellType.ORDER_CELL,
                title = it.title,
                description = it.description,
                price = it.price,
                imageUrl = it.imageUrl,
                restaurantId = it.restaurantId,
                foodId = it.id,
                restaurantTitle = it.restaurantTitle
            )
        }

        orderMenuListViewModel.orderMenu()

        testObservable.assertValueSequence(
            listOf(
                OrderMenuState.Uninitialized,
                OrderMenuState.Loading,
                OrderMenuState.Success(
                    restaurantFoodModelList = menuListInBasketModel
                ),
                OrderMenuState.Order
            )
        )

        assert(orderRepository.getAllOrderMenus(userId) is ResultState.Success<*>)

        val result = (orderRepository.getAllOrderMenus(userId) as ResultState.Success<*>).data

        assert(
            result?.equals(
                listOf(
                    OrderEntity(
                        id = 0.toString(),
                        userId = userId,
                        restaurantId = restaurantId,
                        foodMenuList = menuListInBasket,
                    )
                )
            ) ?: false
        )

    }
}