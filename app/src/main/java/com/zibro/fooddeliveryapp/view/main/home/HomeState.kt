package com.zibro.fooddeliveryapp.view.main.home

import androidx.annotation.StringRes
import com.zibro.fooddeliveryapp.data.entity.MapSearchInfoEntity
import com.zibro.fooddeliveryapp.data.response.search.SearchPoiInfo

sealed class HomeState{
    object Uninitialized : HomeState()

    object Loading : HomeState()

    data class Success(
        val mapSearchInfoEntity: MapSearchInfoEntity,
        val isLocationSame : Boolean
    ) : HomeState()

    data class Error(
        @StringRes val messageId : Int
    ) : HomeState()
}
