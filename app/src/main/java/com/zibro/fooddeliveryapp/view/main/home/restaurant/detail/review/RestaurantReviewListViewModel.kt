package com.zibro.fooddeliveryapp.view.main.home.restaurant.detail.review

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zibro.fooddeliveryapp.data.entity.review.ReviewEntity
import com.zibro.fooddeliveryapp.data.repository.order.ResultState
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
        val result = restaurantReviewRepository.getReviews(restaurantTitle = restaurantTitle)
        when(result){
            is ResultState.Success<*> ->{
                val reviews = result.data as List<ReviewEntity>
                reviewStateLiveData.value=RestaurantReviewState.Success(
                    reviews.map {
                        RestaurantReviewModel(
                            id = it.hashCode().toLong(),
                            title = it.title,
                            description = it.content,
                            grade = it.rating,
                            thumbnailImageUri = if(it.imageUrlList.isNullOrEmpty()){
                                null
                            } else {
                                Uri.parse(it.imageUrlList.first())
                            }
                        )
                    }
                )
            }
            else -> Unit
        }
    }
}