package com.zibro.fooddeliveryapp.view.main.home

sealed class HomeState{
    object Uninitialized : HomeState()

    object Loading : HomeState()

    object Success : HomeState()
}
