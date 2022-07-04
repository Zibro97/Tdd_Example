package com.zibro.fooddeliveryapp.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.zibro.fooddeliveryapp.data.db.ApplicationDataBase

fun provideDB(context: Context) : ApplicationDataBase = Room.databaseBuilder(context,ApplicationDataBase::class.java, ApplicationDataBase.DB_NAME).build()

fun provideLocationDao(dataBase: ApplicationDataBase) = dataBase.locationDao()

fun provideRestaurantDao(dataBase: ApplicationDataBase) = dataBase.restaurantDao()

fun provideFoodMenuBasketDao(dataBase: ApplicationDataBase) = dataBase.foodMenuBasketDao()