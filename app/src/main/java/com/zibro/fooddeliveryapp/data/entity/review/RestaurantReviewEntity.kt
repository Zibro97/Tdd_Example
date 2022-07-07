package com.zibro.fooddeliveryapp.data.entity.review

import android.net.Uri
import com.zibro.fooddeliveryapp.data.entity.Entity

data class RestaurantReviewEntity(
    override val id: Long,
    val title :String,
    val description : String,
    val grade : Int,
    val images : List<Uri>? = null
) : Entity
