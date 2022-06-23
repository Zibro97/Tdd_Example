package com.zibro.fooddeliveryapp.di

import com.zibro.fooddeliveryapp.data.repository.DefaultRestaurantRepository
import com.zibro.fooddeliveryapp.data.repository.RestaurantRepository
import com.zibro.fooddeliveryapp.util.provider.DefaultResourcesProvider
import com.zibro.fooddeliveryapp.util.provider.ResourceProvider
import com.zibro.fooddeliveryapp.view.main.home.HomeViewModel
import com.zibro.fooddeliveryapp.view.main.home.restaurant.RestaurantCategory
import com.zibro.fooddeliveryapp.view.main.home.restaurant.RestaurantListViewModel
import com.zibro.fooddeliveryapp.view.main.my.MyViewModel
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidApplication
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    viewModel { HomeViewModel() }
    viewModel { MyViewModel() }
    viewModel { (restaurantCategory : RestaurantCategory) -> RestaurantListViewModel(restaurantCategory,get())}

    single<RestaurantRepository> { DefaultRestaurantRepository(get(),get()) }

    single { provideGsonConvertFactory()  }
    single { buildOkHttpClient()  }

    single { provideRetrofit(get(),get()) }

    single<ResourceProvider> { DefaultResourcesProvider(androidApplication()) }

    //Dispatchers
    single { Dispatchers.IO}
    single { Dispatchers.Main}
}