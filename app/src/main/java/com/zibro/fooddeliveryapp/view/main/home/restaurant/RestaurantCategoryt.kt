package com.zibro.fooddeliveryapp.view.main.home.restaurant

import androidx.annotation.StringRes
import com.zibro.fooddeliveryapp.R

enum class RestaurantCategory(
    @StringRes val categoryNameId : Int,
    @StringRes val categoryTypeID : Int
) {
    ALL(R.string.all,R.string.all_type)
}