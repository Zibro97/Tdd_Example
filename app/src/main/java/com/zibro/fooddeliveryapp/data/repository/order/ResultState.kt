package com.zibro.fooddeliveryapp.data.repository.order

sealed class ResultState{
    data class Success<T>(
        val data : T? = null
    ) : ResultState()

    data class Error(
        val e: Throwable
    ) : ResultState()
}
