package com.zibro.fooddeliveryapp.view.base

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {
    //각 컴포넌트의 상태를 저장해 관리하기 위한 번들객체
    protected var stateBundle : Bundle? = null

    open fun fetchData() : Job = viewModelScope.launch{}

    open fun storeState(stateBundle: Bundle){
        this.stateBundle = stateBundle
    }
}