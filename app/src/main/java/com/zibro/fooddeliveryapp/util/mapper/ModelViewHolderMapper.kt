package com.zibro.fooddeliveryapp.util.mapper

import android.view.LayoutInflater
import android.view.ViewGroup
import com.zibro.fooddeliveryapp.databinding.ViewholderEmptyBinding
import com.zibro.fooddeliveryapp.databinding.ViewholderFoodMenuBinding
import com.zibro.fooddeliveryapp.databinding.ViewholderRestaurantBinding
import com.zibro.fooddeliveryapp.model.CellType
import com.zibro.fooddeliveryapp.model.Model
import com.zibro.fooddeliveryapp.util.provider.ResourceProvider
import com.zibro.fooddeliveryapp.view.base.BaseViewModel
import com.zibro.fooddeliveryapp.widget.adapter.viewholder.EmptyViewHolder
import com.zibro.fooddeliveryapp.widget.adapter.viewholder.ModelViewHolder
import com.zibro.fooddeliveryapp.widget.adapter.viewholder.Restaurant.RestaurantViewHolder
import com.zibro.fooddeliveryapp.widget.adapter.viewholder.food.FoodMenuViewHolder

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
            CellType.Food_CELL -> FoodMenuViewHolder(
                ViewholderFoodMenuBinding.inflate(inflater, parent,false),
                viewModel,
                resourceProvider
            )
        }
        return viewHolder as ModelViewHolder<M>
    }
}