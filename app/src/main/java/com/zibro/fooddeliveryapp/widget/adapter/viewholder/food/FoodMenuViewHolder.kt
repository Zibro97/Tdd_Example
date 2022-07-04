package com.zibro.fooddeliveryapp.widget.adapter.viewholder.food

import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.zibro.fooddeliveryapp.R
import com.zibro.fooddeliveryapp.databinding.ViewholderFoodMenuBinding
import com.zibro.fooddeliveryapp.extension.clear
import com.zibro.fooddeliveryapp.extension.load
import com.zibro.fooddeliveryapp.model.food.FoodModel
import com.zibro.fooddeliveryapp.util.provider.ResourceProvider
import com.zibro.fooddeliveryapp.view.base.BaseViewModel
import com.zibro.fooddeliveryapp.widget.adapter.listener.AdapterListener
import com.zibro.fooddeliveryapp.widget.adapter.listener.restaurant.FoodMenuListListener
import com.zibro.fooddeliveryapp.widget.adapter.viewholder.ModelViewHolder

class FoodMenuViewHolder(
    private val binding : ViewholderFoodMenuBinding,
    viewModel : BaseViewModel,
    resourceProvider: ResourceProvider
) : ModelViewHolder<FoodModel>(binding,viewModel,resourceProvider) {

    override fun reset() = with(binding) {
        foodImage.clear()
    }

    override fun bindData(model: FoodModel) {
        super.bindData(model)
        with(binding){
            foodImage.load(model.imageUrl,24f,CenterCrop())
            foodTitleText.text = model.title
            foodDescriptionText.text = model.description
            priceText.text = resourcesProvider.getString(R.string.price,model.price)
        }
    }

    override fun bindViews(model: FoodModel, adapterListener: AdapterListener) {
        if(adapterListener is FoodMenuListListener){
            binding.root.setOnClickListener {
                adapterListener.onClickItem(model)
            }
        }
    }
}