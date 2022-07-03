package com.zibro.fooddeliveryapp.view.main.home.restaurant.detail.menu

import androidx.core.os.bundleOf
import com.zibro.fooddeliveryapp.data.entity.restaurant.RestaurantFoodEntity
import com.zibro.fooddeliveryapp.databinding.FragmentListBinding
import com.zibro.fooddeliveryapp.model.food.FoodModel
import com.zibro.fooddeliveryapp.util.provider.ResourceProvider
import com.zibro.fooddeliveryapp.view.base.BaseFragment
import com.zibro.fooddeliveryapp.widget.adapter.ModelRecyclerAdapter
import com.zibro.fooddeliveryapp.widget.adapter.listener.AdapterListener
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class RestaurantMenuListFragment: BaseFragment<RestaurantMenuListViewModel,FragmentListBinding>() {
    private val restaurantId by lazy {
        arguments?.getLong(RESTAURANT_ID_KEY,-1)
    }
    private val restaurantFoodList by lazy{
        arguments?.getParcelableArrayList<RestaurantFoodEntity>(FOOD_LIST_KEY)!!
    }

    override val viewModel by viewModel<RestaurantMenuListViewModel>(){
        parametersOf(
            restaurantId,
            restaurantFoodList
        )
    }

    override fun getViewBinding(): FragmentListBinding = FragmentListBinding.inflate(layoutInflater)

    private val resourceProvider by inject<ResourceProvider>()

    private val adapter by lazy {
        ModelRecyclerAdapter<FoodModel, RestaurantMenuListViewModel>(listOf(), viewModel ,resourceProvider = resourceProvider,adapterListener = object : AdapterListener{})
    }

    override fun initViews() = with(binding){
        recyclerview.adapter = adapter
    }

    override fun observeData() = viewModel.restaurantFoodListLiveData.observe(this){
        adapter.submitList(it)
    }

    companion object{
        const val RESTAURANT_ID_KEY = "restaurantID"
        const val FOOD_LIST_KEY = "foodList"
        fun newInstance(restaurantId: Long, foodList : ArrayList<RestaurantFoodEntity>) : RestaurantMenuListFragment{
            val bundle = bundleOf(
                RESTAURANT_ID_KEY to restaurantId,
                FOOD_LIST_KEY to foodList
            )
            return RestaurantMenuListFragment().apply {
                arguments = bundle
            }
        }
    }
}