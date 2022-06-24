package com.zibro.fooddeliveryapp.widget.adapter.viewholder.Restaurant

import com.zibro.fooddeliveryapp.R
import com.zibro.fooddeliveryapp.databinding.ViewholderRestaurantBinding
import com.zibro.fooddeliveryapp.extension.clear
import com.zibro.fooddeliveryapp.extension.load
import com.zibro.fooddeliveryapp.model.restaurant.RestaurantModel
import com.zibro.fooddeliveryapp.util.provider.ResourceProvider
import com.zibro.fooddeliveryapp.view.base.BaseViewModel
import com.zibro.fooddeliveryapp.widget.adapter.listener.AdapterListener
import com.zibro.fooddeliveryapp.widget.adapter.listener.restaurant.RestaurantListListener
import com.zibro.fooddeliveryapp.widget.adapter.viewholder.ModelViewHolder

class RestaurantViewHolder(
    private val binding:ViewholderRestaurantBinding,
    viewModel : BaseViewModel,
    resourceProvider: ResourceProvider
)  : ModelViewHolder<RestaurantModel>(binding, viewModel,resourceProvider) {
    override fun reset() = with(binding){
        restaurantImage.clear()
    }
    override fun bindData(model: RestaurantModel) {
        super.bindData(model)
        with(binding) {
            restaurantImage.load(model.restaurantImageUrl, 24f)
            restaurantTitleText.text = model.restaurantTitle
            gradeText.text = resourcesProvider.getString(R.string.grade_format, model.grade)
            reviewCountText.text = resourcesProvider.getString(R.string.review_count, model.reviewCount)
            val (minTime, maxTime) = model.deliveryTimeRange
            deliveryTimeText.text = resourcesProvider.getString(R.string.delivery_time,minTime,maxTime)

            val (minTip,maxTip) = model.deliveryTipRange
            deliveryTipText.text = resourcesProvider.getString(R.string.delivery_tip,minTip,maxTip)
        }
    }
    override fun bindViews(model: RestaurantModel, adapterListener: AdapterListener) = with(binding) {
        if(adapterListener is RestaurantListListener){
            root.setOnClickListener {
                adapterListener.onClickItem(model)
            }
        }
    }
}