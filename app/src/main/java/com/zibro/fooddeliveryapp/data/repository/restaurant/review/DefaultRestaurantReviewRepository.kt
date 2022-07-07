package com.zibro.fooddeliveryapp.data.repository.restaurant.review

import com.zibro.fooddeliveryapp.data.entity.review.RestaurantReviewEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DefaultRestaurantReviewRepository(
    private val ioDispatcher: CoroutineDispatcher
): RestaurantReviewRepository {
    override suspend fun getReviews(restaurantTitle: String) = withContext(ioDispatcher){
        return@withContext (0..10).map {
            RestaurantReviewEntity(
                id = 0,
                title = "제목 $it",
                description = "내용 $it",
                grade = (1 until 5).random(),
            )
        }
    }
}