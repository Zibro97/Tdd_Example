package com.zibro.fooddeliveryapp.view.order

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.zibro.fooddeliveryapp.R
import com.zibro.fooddeliveryapp.databinding.ActivityOrderMenuListBinding
import com.zibro.fooddeliveryapp.model.food.FoodModel
import com.zibro.fooddeliveryapp.util.provider.ResourceProvider
import com.zibro.fooddeliveryapp.view.base.BaseActivity
import com.zibro.fooddeliveryapp.widget.adapter.ModelRecyclerAdapter
import com.zibro.fooddeliveryapp.widget.adapter.listener.order.OrderMenuListListener
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class OrderMenuListActivity : BaseActivity<OrderMenuListViewModel,ActivityOrderMenuListBinding>() {
    private val resourcesProvider by inject<ResourceProvider>()

    private val adapter by lazy {
        ModelRecyclerAdapter<FoodModel, OrderMenuListViewModel>(listOf(), viewModel,resourcesProvider, adapterListener = object :
            OrderMenuListListener {
            override fun onRemoveItem(model: FoodModel) {
                viewModel.removeOrderMenu(model)
            }
        })
    }

    override val viewModel by viewModel<OrderMenuListViewModel>()

    override fun getViewBinding(): ActivityOrderMenuListBinding = ActivityOrderMenuListBinding.inflate(layoutInflater)

    override fun initViews() = with(binding){
        recyclerView.adapter = adapter
        toolbar.setNavigationOnClickListener { finish() }
        confirmButton.setOnClickListener {
            viewModel.orderMenu()
        }
        orderClearButton.setOnClickListener {
            viewModel.clearOrderMenu()
        }
    }

    override fun observeData() = viewModel.orderMenuStateLiveData.observe(this){ state ->
        when (state){
            is OrderMenuState.Loading -> {
                handleLoading()
            }
            is OrderMenuState.Success ->{
                handleSuccess(state)
            }
            is OrderMenuState.Order ->{
                handleOrderState()
            }
            is OrderMenuState.Error ->{
                handleErrorState(state)
            }
            else -> Unit
        }
    }

    private fun handleLoading() = with(binding){
        progressbar.isVisible = true
    }

    private fun handleSuccess(state : OrderMenuState.Success) = with(binding){
        progressbar.isGone= true
        adapter.submitList(state.restaurantFoodModelList)
        val menuOrderIsEmpty = state.restaurantFoodModelList.isNullOrEmpty()
        confirmButton.isEnabled = menuOrderIsEmpty.not()
        if(menuOrderIsEmpty){
            Toast.makeText(this@OrderMenuListActivity, "주문 메뉴가 없어 화면을 종료합니다.", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun handleOrderState(){
        Toast.makeText(this, "성공적으로 주문을 완료하였습니다.", Toast.LENGTH_SHORT).show()
    }

    private fun handleErrorState(state : OrderMenuState.Error){
        Toast.makeText(this, getString(R.string.request_error,state.e),Toast.LENGTH_SHORT).show()
    }

    companion object{
        fun newIntent(context: Context) = Intent(context,OrderMenuListActivity::class.java)
    }
}