package com.zibro.fooddeliveryapp.di

import com.zibro.fooddeliveryapp.data.entity.LocationLatLngEntity
import com.zibro.fooddeliveryapp.data.entity.MapSearchInfoEntity
import com.zibro.fooddeliveryapp.data.entity.RestaurantEntity
import com.zibro.fooddeliveryapp.data.repository.map.DefaultMapRepository
import com.zibro.fooddeliveryapp.data.repository.map.MapRepository
import com.zibro.fooddeliveryapp.data.repository.restaurant.DefaultRestaurantRepository
import com.zibro.fooddeliveryapp.data.repository.restaurant.RestaurantRepository
import com.zibro.fooddeliveryapp.data.repository.user.DefaultUserRepository
import com.zibro.fooddeliveryapp.data.repository.user.UserRepository
import com.zibro.fooddeliveryapp.util.provider.DefaultResourcesProvider
import com.zibro.fooddeliveryapp.util.provider.ResourceProvider
import com.zibro.fooddeliveryapp.view.main.home.HomeViewModel
import com.zibro.fooddeliveryapp.view.main.home.restaurant.RestaurantCategory
import com.zibro.fooddeliveryapp.view.main.home.restaurant.RestaurantListViewModel
import com.zibro.fooddeliveryapp.view.main.home.restaurant.detail.RestaurantDetailViewModel
import com.zibro.fooddeliveryapp.view.main.my.MyViewModel
import com.zibro.fooddeliveryapp.view.mylocation.MyLocationViewModel
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidApplication
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    viewModel { HomeViewModel(get(),get()) }
    viewModel { MyViewModel() }
    viewModel { (mapSearchInfoEntity : MapSearchInfoEntity) -> MyLocationViewModel(mapSearchInfoEntity,get(),get()) }
    viewModel { (restaurantCategory : RestaurantCategory,locationLatLng : LocationLatLngEntity) -> RestaurantListViewModel(restaurantCategory,locationLatLng,get())}
    viewModel { (restaurantEntity : RestaurantEntity) ->RestaurantDetailViewModel(restaurantEntity,get())}

    single<RestaurantRepository> { DefaultRestaurantRepository(get(),get(),get()) }
    single<MapRepository> { DefaultMapRepository(get(),get()) }
    single<UserRepository> { DefaultUserRepository(get(),get(),get()) }

    single { provideGsonConvertFactory()  }
    single { buildOkHttpClient()  }

    single { provideRetrofit(get(),get()) }

    single { provideMapApiService(get())}
    single { provideDB(androidApplication()) }
    single { provideLocationDao(get()) }
    single { provideRestaurantDao(get()) }

    single<ResourceProvider> { DefaultResourcesProvider(androidApplication()) }

    //Dispatchers
    single { Dispatchers.IO}
    single { Dispatchers.Main}
}