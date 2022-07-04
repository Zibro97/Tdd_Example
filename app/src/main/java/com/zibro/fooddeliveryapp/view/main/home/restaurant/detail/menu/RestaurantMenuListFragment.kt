package com.zibro.fooddeliveryapp.view.main.home.restaurant.detail.menu

import android.widget.Toast
import androidx.core.os.bundleOf
import com.zibro.fooddeliveryapp.data.entity.restaurant.RestaurantFoodEntity
import com.zibro.fooddeliveryapp.databinding.FragmentListBinding
import com.zibro.fooddeliveryapp.model.food.FoodModel
import com.zibro.fooddeliveryapp.util.provider.ResourceProvider
import com.zibro.fooddeliveryapp.view.base.BaseFragment
import com.zibro.fooddeliveryapp.view.main.home.restaurant.detail.RestaurantDetailViewModel
import com.zibro.fooddeliveryapp.widget.adapter.ModelRecyclerAdapter
import com.zibro.fooddeliveryapp.widget.adapter.listener.restaurant.FoodMenuListListener
import okhttp3.internal.notify
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class RestaurantMenuListFragment: BaseFragment<RestaurantMenuListViewModel,FragmentListBinding>() {
    private val restaurantId by lazy {
        arguments?.getLong(RESTAURANT_ID_KEY,-1)
    }
    private val restaurantFoodList by lazy{
        arguments?.getParcelableArrayList<RestaurantFoodEntity>(FOOD_LIST_KEY)!!
    }

    private val restaurantDetailViewModel by sharedViewModel<RestaurantDetailViewModel>()

    override val viewModel by viewModel<RestaurantMenuListViewModel>(){
        parametersOf(
            restaurantId,
            restaurantFoodList
        )
    }

    override fun getViewBinding(): FragmentListBinding = FragmentListBinding.inflate(layoutInflater)

    private val resourceProvider by inject<ResourceProvider>()

    private val adapter by lazy {
        ModelRecyclerAdapter<FoodModel, RestaurantMenuListViewModel>(
            listOf(),
            viewModel,
            resourceProvider = resourceProvider,adapterListener = object : FoodMenuListListener{
                override fun onClickItem(model: FoodModel) {
                    viewModel.insertMenuInBasket(model)
                }
            })
    }

    override fun initViews() = with(binding){
        recyclerview.adapter = adapter
    }

    override fun observeData() = with(viewModel){
        restaurantFoodListLiveData.observe(viewLifecycleOwner){
            adapter.submitList(it)
        }
        menuBasketLiveData.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(),"장바구니에 담겼습니다. 메뉴 : ${it.title}",Toast.LENGTH_SHORT).show()
            restaurantDetailViewModel.notifyFoodMenuListBasket(it)
        }
        isClearNeedInBasketLiveData.observe(viewLifecycleOwner) { (isClearNeed, afterAction) ->
            if(isClearNeed){
                restaurantDetailViewModel.notifyClearNeedAlertInBasket(isClearNeed, afterAction)
            }
        }
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