package com.zibro.fooddeliveryapp.model.review

import android.net.Uri
import com.zibro.fooddeliveryapp.model.CellType
import com.zibro.fooddeliveryapp.model.Model

data class RestaurantReviewModel(
    override val id: Long,
    override val type: CellType = CellType.REVIEW_CELL,
    val title: String,
    val description: String,
    val grade: Int,
    val thumbnailImageUri: Uri? = null
) : Model(id, type)
