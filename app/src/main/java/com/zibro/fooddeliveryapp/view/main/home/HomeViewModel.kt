package com.zibro.fooddeliveryapp.view.main.home

import androidx.lifecycle.MutableLiveData
import com.zibro.fooddeliveryapp.view.base.BaseViewModel

class HomeViewModel : BaseViewModel() {
    //State-Pattern LiveData
    val homeStateLiveData = MutableLiveData<HomeState>(HomeState.Uninitialized)

//    fun loadReverseGeoInformation(){
//
//    }

}