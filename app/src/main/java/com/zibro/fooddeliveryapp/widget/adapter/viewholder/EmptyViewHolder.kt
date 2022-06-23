package com.zibro.fooddeliveryapp.widget.adapter.viewholder

import com.zibro.fooddeliveryapp.databinding.ViewholderEmptyBinding
import com.zibro.fooddeliveryapp.model.Model
import com.zibro.fooddeliveryapp.util.provider.ResourceProvider
import com.zibro.fooddeliveryapp.view.base.BaseViewModel
import com.zibro.fooddeliveryapp.widget.adapter.listener.AdapterListener

class EmptyViewHolder(
    private val binding:ViewholderEmptyBinding,
    viewModel : BaseViewModel,
    resourceProvider: ResourceProvider
)  : ModelViewHolder<Model>(binding, viewModel,resourceProvider) {
    override fun reset() = Unit

    override fun bindViews(model: Model, adapterListener: AdapterListener)  = Unit
}