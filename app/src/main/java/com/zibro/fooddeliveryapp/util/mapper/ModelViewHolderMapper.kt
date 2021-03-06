package com.zibro.fooddeliveryapp.util.mapper

import android.view.LayoutInflater
import android.view.ViewGroup
import com.zibro.fooddeliveryapp.databinding.*
import com.zibro.fooddeliveryapp.model.CellType
import com.zibro.fooddeliveryapp.model.Model
import com.zibro.fooddeliveryapp.util.provider.ResourceProvider
import com.zibro.fooddeliveryapp.view.base.BaseViewModel
import com.zibro.fooddeliveryapp.widget.adapter.viewholder.EmptyViewHolder
import com.zibro.fooddeliveryapp.widget.adapter.viewholder.ModelViewHolder
import com.zibro.fooddeliveryapp.widget.adapter.viewholder.Restaurant.LikeRestaurantViewHolder
import com.zibro.fooddeliveryapp.widget.adapter.viewholder.Restaurant.RestaurantViewHolder
import com.zibro.fooddeliveryapp.widget.adapter.viewholder.food.FoodMenuViewHolder
import com.zibro.fooddeliveryapp.widget.adapter.viewholder.order.OrderMenuViewHolder
import com.zibro.fooddeliveryapp.widget.adapter.viewholder.order.OrderViewHolder
import com.zibro.fooddeliveryapp.widget.adapter.viewholder.review.ReviewViewHolder

object ModelViewHolderMapper {

    @Suppress("UNCHECKED_CAST")
    fun <M: Model> map(
        parent : ViewGroup,
        type : CellType,
        viewModel: BaseViewModel,
        resourceProvider: ResourceProvider
    ): ModelViewHolder<M>{
        val inflater = LayoutInflater.from(parent.context)
        val viewHolder = when(type){
            CellType.EMPTY_CELL -> EmptyViewHolder(
                ViewholderEmptyBinding.inflate(inflater,parent,false),
                viewModel,
                resourceProvider
            )
            CellType.RESTAURANT_CELL -> RestaurantViewHolder(
                ViewholderRestaurantBinding.inflate(inflater,parent,false),
                viewModel,
                resourceProvider
            )
            CellType.LIKE_RESTAURANT_CELL -> LikeRestaurantViewHolder(
                ViewholderRestaurantLikeListBinding.inflate(inflater,parent,false),
                viewModel,
                resourceProvider
            )
            CellType.FOOD_CELL -> FoodMenuViewHolder(
                ViewholderFoodMenuBinding.inflate(inflater, parent,false),
                viewModel,
                resourceProvider
            )
            CellType.REVIEW_CELL ->  ReviewViewHolder(
                ViewholderReviewBinding.inflate(inflater,parent,false),
                viewModel,
                resourceProvider
            )
            CellType.ORDER_FOOD_CELL -> OrderMenuViewHolder(
                ViewholderOrderMenuBinding.inflate(inflater, parent,false),
                viewModel,
                resourceProvider
            )
            CellType.ORDER_CELL -> OrderViewHolder(
                ViewholderOrderBinding.inflate(inflater,parent,false),
                viewModel,
                resourceProvider
            )
        }
        return viewHolder as ModelViewHolder<M>
    }
}