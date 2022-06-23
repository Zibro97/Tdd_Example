package com.zibro.fooddeliveryapp.widget.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.zibro.fooddeliveryapp.model.CellType
import com.zibro.fooddeliveryapp.model.Model
import com.zibro.fooddeliveryapp.util.mapper.ModelViewHolderMapper
import com.zibro.fooddeliveryapp.util.provider.ResourceProvider
import com.zibro.fooddeliveryapp.view.base.BaseViewModel
import com.zibro.fooddeliveryapp.widget.adapter.listener.AdapterListener
import com.zibro.fooddeliveryapp.widget.adapter.viewholder.ModelViewHolder

//Custom RecyclerView Adapter
class ModelRecyclerAdapter<M: Model, VM:BaseViewModel>(
    private var modelList:List<Model>,
    private val viewModel: VM,
    private val resourceProvider: ResourceProvider,
    private val adapterListener : AdapterListener
) : ListAdapter<Model, ModelViewHolder<M>>(Model.DIFF_CALLBACK) {
    override fun getItemCount(): Int = modelList.size

    override fun getItemViewType(position: Int): Int = modelList[position].type.ordinal

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ModelViewHolder<M> {
        return ModelViewHolderMapper.map(parent, CellType.values()[viewType],viewModel,resourceProvider)
    }

    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(holder: ModelViewHolder<M>, position: Int) {
        with(holder){
            bindData(modelList[position] as M)
            bindViews(modelList[position] as M, adapterListener)
        }
    }

    override fun submitList(list: List<Model>?) {
        list?.let {
            modelList = it
        }
        super.submitList(list)
    }


}