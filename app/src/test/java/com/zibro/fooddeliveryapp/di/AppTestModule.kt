package com.zibro.fooddeliveryapp.di

import com.google.firebase.auth.FirebaseAuth
import com.zibro.fooddeliveryapp.data.TestOrderRepository
import com.zibro.fooddeliveryapp.data.TestRestaurantFoodRepository
import com.zibro.fooddeliveryapp.data.TestRestaurantRepository
import com.zibro.fooddeliveryapp.data.entity.LocationLatLngEntity
import com.zibro.fooddeliveryapp.data.repository.order.OrderRepository
import com.zibro.fooddeliveryapp.data.repository.restaurant.RestaurantRepository
import com.zibro.fooddeliveryapp.data.repository.restaurant.food.RestaurantFoodRepository
import com.zibro.fooddeliveryapp.view.main.home.restaurant.RestaurantCategory
import com.zibro.fooddeliveryapp.view.main.home.restaurant.RestaurantListViewModel
import com.zibro.fooddeliveryapp.view.order.OrderMenuListViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

internal val appTestModule = module{

    viewModel { (restaurantCategory: RestaurantCategory, locationLatLngEntity: LocationLatLngEntity) ->
        RestaurantListViewModel(restaurantCategory, locationLatLngEntity, get())
    }

    viewModel { (firebaseAuth: FirebaseAuth) -> OrderMenuListViewModel(get(), get(), firebaseAuth) }

    single<RestaurantRepository> { TestRestaurantRepository() }

    single<RestaurantFoodRepository>{ TestRestaurantFoodRepository() }

    single<OrderRepository> { TestOrderRepository() }
}