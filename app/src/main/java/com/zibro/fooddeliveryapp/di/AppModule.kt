package com.zibro.fooddeliveryapp.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.zibro.fooddeliveryapp.data.entity.LocationLatLngEntity
import com.zibro.fooddeliveryapp.data.entity.MapSearchInfoEntity
import com.zibro.fooddeliveryapp.data.entity.RestaurantEntity
import com.zibro.fooddeliveryapp.data.entity.restaurant.RestaurantFoodEntity
import com.zibro.fooddeliveryapp.data.preference.AppPreferenceManager
import com.zibro.fooddeliveryapp.data.repository.map.DefaultMapRepository
import com.zibro.fooddeliveryapp.data.repository.map.MapRepository
import com.zibro.fooddeliveryapp.data.repository.order.DefaultOrderRepository
import com.zibro.fooddeliveryapp.data.repository.order.OrderRepository
import com.zibro.fooddeliveryapp.data.repository.restaurant.DefaultRestaurantRepository
import com.zibro.fooddeliveryapp.data.repository.restaurant.RestaurantRepository
import com.zibro.fooddeliveryapp.data.repository.restaurant.food.DefaultRestaurantFoodRepository
import com.zibro.fooddeliveryapp.data.repository.restaurant.food.RestaurantFoodRepository
import com.zibro.fooddeliveryapp.data.repository.restaurant.review.DefaultRestaurantReviewRepository
import com.zibro.fooddeliveryapp.data.repository.restaurant.review.RestaurantReviewRepository
import com.zibro.fooddeliveryapp.data.repository.user.DefaultUserRepository
import com.zibro.fooddeliveryapp.data.repository.user.UserRepository
import com.zibro.fooddeliveryapp.util.event.MenuChangeEventBus
import com.zibro.fooddeliveryapp.util.provider.DefaultResourcesProvider
import com.zibro.fooddeliveryapp.util.provider.ResourceProvider
import com.zibro.fooddeliveryapp.view.main.home.HomeViewModel
import com.zibro.fooddeliveryapp.view.main.home.restaurant.RestaurantCategory
import com.zibro.fooddeliveryapp.view.main.home.restaurant.RestaurantListViewModel
import com.zibro.fooddeliveryapp.view.main.home.restaurant.detail.RestaurantDetailViewModel
import com.zibro.fooddeliveryapp.view.main.home.restaurant.detail.menu.RestaurantMenuListViewModel
import com.zibro.fooddeliveryapp.view.main.home.restaurant.detail.review.RestaurantReviewListViewModel
import com.zibro.fooddeliveryapp.view.main.like.RestaurantLikeListViewModel
import com.zibro.fooddeliveryapp.view.main.my.MyViewModel
import com.zibro.fooddeliveryapp.view.mylocation.MyLocationViewModel
import com.zibro.fooddeliveryapp.view.order.OrderMenuListActivity
import com.zibro.fooddeliveryapp.view.order.OrderMenuListViewModel
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidApplication
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val appModule = module {

    viewModel { HomeViewModel(get(),get(),get()) }
    viewModel { MyViewModel(get()) }
    viewModel { (mapSearchInfoEntity : MapSearchInfoEntity) -> MyLocationViewModel(mapSearchInfoEntity,get(),get()) }
    viewModel { (restaurantCategory : RestaurantCategory,locationLatLng : LocationLatLngEntity) -> RestaurantListViewModel(restaurantCategory,locationLatLng,get())}
    viewModel { (restaurantEntity : RestaurantEntity) ->RestaurantDetailViewModel(restaurantEntity,get(),get())}
    viewModel { (restaurantId : Long, restaurantFoodList:List<RestaurantFoodEntity>) -> RestaurantMenuListViewModel(restaurantId,restaurantFoodList,get())}
    viewModel { (restaurantTitle: String) -> RestaurantReviewListViewModel(restaurantTitle,get()) }
    viewModel { RestaurantLikeListViewModel(get()) }
    viewModel { (firebaseAuth : FirebaseAuth)->OrderMenuListViewModel(get(),get(),firebaseAuth) }

    single<RestaurantRepository> { DefaultRestaurantRepository(get(),get(),get()) }
    single<MapRepository> { DefaultMapRepository(get(),get()) }
    single<UserRepository> { DefaultUserRepository(get(),get(),get()) }
    single<RestaurantFoodRepository> { DefaultRestaurantFoodRepository(get(),get(),get()) }
    single<RestaurantReviewRepository> { DefaultRestaurantReviewRepository(get()) }
    single<OrderRepository> { DefaultOrderRepository(get(),get()) }

    single { provideGsonConvertFactory()  }
    single { buildOkHttpClient()  }

    single(named("map"))  { provideMapRetrofit(get(),get()) }
    single(named("food")) { provideFoodRetrofit(get(),get()) }

    single { provideMapApiService(get(qualifier = named("map")))}
    single { provideRestaurantFoodApiService(get(qualifier = named("food")))}
    single { provideDB(androidApplication()) }
    single { provideLocationDao(get()) }
    single { provideRestaurantDao(get()) }
    single { provideFoodMenuBasketDao(get()) }

    single<ResourceProvider> { DefaultResourcesProvider(androidApplication()) }
    single { AppPreferenceManager(androidApplication()) }

    //Dispatchers
    single { Dispatchers.IO}
    single { Dispatchers.Main}

    single { MenuChangeEventBus() }

    single { Firebase.firestore }
}