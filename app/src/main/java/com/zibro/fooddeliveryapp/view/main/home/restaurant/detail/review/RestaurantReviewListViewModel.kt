package com.zibro.fooddeliveryapp.view.main.home.restaurant.detail.review

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zibro.fooddeliveryapp.data.repository.restaurant.review.RestaurantReviewRepository
import com.zibro.fooddeliveryapp.model.review.RestaurantReviewModel
import com.zibro.fooddeliveryapp.view.base.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class RestaurantReviewListViewModel(
    private val restaurantTitle : String,
    private val restaurantReviewRepository: RestaurantReviewRepository
) : BaseViewModel() {
    val reviewStateLiveData = MutableLiveData<RestaurantReviewState>(RestaurantReviewState.Uninitialized)

    override fun fetchData(): Job = viewModelScope.launch {
        reviewStateLiveData.value=RestaurantReviewState.Loading
        val reviews = restaurantReviewRepository.getReviews(restaurantTitle = restaurantTitle)
        reviewStateLiveData.value=RestaurantReviewState.Success(
            reviews.map {
                RestaurantReviewModel(
                    id = it.id,
                    title = it.title,
                    description = it.description,
                    grade = it.grade,
                    thumbnailImageUri = it.images?.first()
                )
            }
        )
    }
}