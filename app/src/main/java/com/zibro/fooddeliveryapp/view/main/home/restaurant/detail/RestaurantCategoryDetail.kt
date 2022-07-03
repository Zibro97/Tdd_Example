package com.zibro.fooddeliveryapp.view.main.home.restaurant.detail

import androidx.annotation.StringRes
import com.zibro.fooddeliveryapp.R

enum class RestaurantCategoryDetail(
    @StringRes val categoryNameId : Int
) {
    MENU(R.string.menu),
    REVIEW(R.string.review)
}