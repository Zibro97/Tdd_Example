package com.zibro.fooddeliveryapp.data.repository.restaurant.review

import com.google.firebase.firestore.FirebaseFirestore
import com.zibro.fooddeliveryapp.data.entity.review.ReviewEntity
import com.zibro.fooddeliveryapp.data.repository.order.ResultState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.lang.Exception

class DefaultRestaurantReviewRepository(
    private val ioDispatcher: CoroutineDispatcher,
    private val firestore: FirebaseFirestore
): RestaurantReviewRepository {
    override suspend fun getReviews(restaurantTitle: String):ResultState = withContext(ioDispatcher){
        return@withContext try {
            val snapshot = firestore
                .collection("review")
                .whereEqualTo("restaurantTitle",restaurantTitle)
                .get()
                .await()
            ResultState.Success(snapshot.documents.map {
                ReviewEntity(
                    userId = it.get("userId") as String,
                    title = it.get("title") as String,
                    createdAt = it.get("createdAt") as Long,
                    content = it.get("content") as String,
                    rating = (it.get("rating") as Double).toFloat(),
                    imageUrlList = it.get("imageUrlList") as? List<String>,
                    orderId = it.get("orderId") as String,
                    restaurantTitle = it.get("restaurantTitle") as String
                )
            })
        }catch (e: Exception){
            e.printStackTrace()
            ResultState.Error(e)
        }
    }
}