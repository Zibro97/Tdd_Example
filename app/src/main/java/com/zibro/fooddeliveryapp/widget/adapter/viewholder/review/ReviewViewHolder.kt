package com.zibro.fooddeliveryapp.widget.adapter.viewholder.review

import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.zibro.fooddeliveryapp.R
import com.zibro.fooddeliveryapp.databinding.ViewholderRestaurantBinding
import com.zibro.fooddeliveryapp.databinding.ViewholderReviewBinding
import com.zibro.fooddeliveryapp.extension.clear
import com.zibro.fooddeliveryapp.extension.load
import com.zibro.fooddeliveryapp.model.restaurant.RestaurantModel
import com.zibro.fooddeliveryapp.model.review.RestaurantReviewModel
import com.zibro.fooddeliveryapp.util.provider.ResourceProvider
import com.zibro.fooddeliveryapp.view.base.BaseViewModel
import com.zibro.fooddeliveryapp.widget.adapter.listener.AdapterListener
import com.zibro.fooddeliveryapp.widget.adapter.listener.restaurant.RestaurantListListener
import com.zibro.fooddeliveryapp.widget.adapter.viewholder.ModelViewHolder

class ReviewViewHolder(
    private val binding:ViewholderReviewBinding,
    viewModel : BaseViewModel,
    resourceProvider: ResourceProvider
)  : ModelViewHolder<RestaurantReviewModel>(binding, viewModel,resourceProvider) {
    override fun reset() = with(binding){
        reviewThumbnailImage.clear()
        reviewThumbnailImage.isGone = true
    }
    override fun bindData(model: RestaurantReviewModel) {
        super.bindData(model)
        with(binding) {
            if (model.thumbnailImageUri != null) {
                reviewThumbnailImage.isVisible = true
                reviewThumbnailImage.load(model.thumbnailImageUri.toString())
            } else {
                reviewThumbnailImage.isGone = true
            }

            reviewTitleText.text = model.title
            reviewText.text = model.description
            ratingBar.rating = model.grade.toFloat()
        }
    }
    override fun bindViews(model: RestaurantReviewModel, adapterListener: AdapterListener) = Unit
}