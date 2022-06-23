package com.zibro.fooddeliveryapp.widget.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.zibro.fooddeliveryapp.model.Model
import com.zibro.fooddeliveryapp.util.provider.ResourceProvider
import com.zibro.fooddeliveryapp.view.base.BaseViewModel
import com.zibro.fooddeliveryapp.widget.adapter.listener.AdapterListener

abstract class ModelViewHolder<M: Model>(
    binding : ViewBinding,
    protected val viewModel : BaseViewModel,
    protected val resourcesProvider : ResourceProvider
) : RecyclerView.ViewHolder(binding.root) {

    abstract fun reset()

    open fun bindData(model : M){
        reset()
    }

    abstract fun bindViews(model : M, adapterListener:AdapterListener)
}