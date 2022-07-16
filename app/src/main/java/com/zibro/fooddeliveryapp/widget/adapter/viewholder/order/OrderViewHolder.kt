package com.zibro.fooddeliveryapp.widget.adapter.viewholder.order

import com.zibro.fooddeliveryapp.R
import com.zibro.fooddeliveryapp.databinding.ViewholderOrderBinding
import com.zibro.fooddeliveryapp.model.order.OrderModel
import com.zibro.fooddeliveryapp.util.provider.ResourceProvider
import com.zibro.fooddeliveryapp.view.base.BaseViewModel
import com.zibro.fooddeliveryapp.widget.adapter.listener.AdapterListener
import com.zibro.fooddeliveryapp.widget.adapter.listener.order.OrderListListener
import com.zibro.fooddeliveryapp.widget.adapter.viewholder.ModelViewHolder

class OrderViewHolder(
    private val binding : ViewholderOrderBinding,
    viewModel : BaseViewModel,
    resourceProvider: ResourceProvider
) : ModelViewHolder<OrderModel>(binding,viewModel,resourceProvider) {

    override fun reset() = Unit

    override fun bindData(model: OrderModel) {
        super.bindData(model)
        with(binding){
            orderTitleText.text = resourcesProvider.getString(R.string.order_history_title,model.orderId)

            val foodMenuList = model.foodMenuList
            foodMenuList.groupBy { it.title }
                .entries.forEach{ (title,menuList) ->
                    val orderDataStr = orderContentText.text.toString() + "메뉴 : $title | 가격 : ${menuList.first().price}원 X ${menuList.size}\n"

                    orderContentText.text = orderDataStr
                }
            orderContentText.text = orderContentText.text.trim()
            orderTotalPriceText.text = resourcesProvider.getString(R.string.price, foodMenuList.map { it.price }.reduce{total, price ->
                total + price
            })
        }
    }

    override fun bindViews(model: OrderModel, adapterListener: AdapterListener) {
        if(adapterListener is OrderListListener){
            binding.root.setOnClickListener {
                adapterListener.writeRestaurantReview(model.orderId,model.restaurantTitle)
            }
        }
    }
}