package com.zibro.fooddeliveryapp.view.main.my

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.zibro.fooddeliveryapp.R
import com.zibro.fooddeliveryapp.data.entity.order.OrderEntity
import com.zibro.fooddeliveryapp.data.preference.AppPreferenceManager
import com.zibro.fooddeliveryapp.data.repository.order.DefaultOrderRepository
import com.zibro.fooddeliveryapp.data.repository.order.OrderRepository
import com.zibro.fooddeliveryapp.data.repository.order.ResultState
import com.zibro.fooddeliveryapp.data.repository.user.UserRepository
import com.zibro.fooddeliveryapp.model.order.OrderModel
import com.zibro.fooddeliveryapp.view.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyViewModel(
    private val prefs : AppPreferenceManager,
    private val userRepository: UserRepository,
    private val orderRepository: OrderRepository
) : BaseViewModel() {
    val myStateLivedata = MutableLiveData<MyState>(MyState.Uninitialized)

    override fun fetchData(): Job = viewModelScope.launch {
        myStateLivedata.value = MyState.Loading
        prefs.getIdToken()?.let {
            myStateLivedata.value = MyState.Login(it)
        } ?:kotlin.run {
            myStateLivedata.value = MyState.Success.NotRegistered
        }
    }

    fun saveToken(token: String) = viewModelScope.launch {
        withContext(Dispatchers.IO){
            prefs.putIdToken(idToken = token)
            fetchData()
        }
    }

    fun setUserInfo(firebaseUser: FirebaseUser?)= viewModelScope.launch {
        firebaseUser?.let { user->
            when (val orderMenuResult = orderRepository.getAllOrderMenus(user.uid)){
                is ResultState.Success<*> -> {
                    val orderList = orderMenuResult.data as List<OrderEntity>
                    myStateLivedata.value = MyState.Success.Registerd(
                        userName = user.displayName ?: "익명",
                        profileImageUri = user.photoUrl,
                        orderList = orderList.map {
                            OrderModel(
                                id = it.hashCode().toLong(),
                                orderId = it.id,
                                userId = it.userId,
                                restaurantId = it.restaurantId,
                                foodMenuList = it.foodMenuList
                            )
                        }
                    )
                }
                is ResultState.Error -> {
                    myStateLivedata.value = MyState.Error(
                        R.string.request_error,
                        orderMenuResult.e
                    )
                }
            }
        }?:kotlin.run {
            myStateLivedata.value = MyState.Success.NotRegistered
        }

    }

    fun signOut() = viewModelScope.launch {
        withContext(Dispatchers.IO){
            prefs.removeIdToken()
        }
        userRepository.deleteALlUserLikedRestaurant()
        fetchData()
    }
}