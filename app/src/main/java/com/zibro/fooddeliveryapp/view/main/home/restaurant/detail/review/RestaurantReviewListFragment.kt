package com.zibro.fooddeliveryapp.view.main.home.restaurant.detail.review

import androidx.core.os.bundleOf
import com.zibro.fooddeliveryapp.data.entity.restaurant.RestaurantFoodEntity
import com.zibro.fooddeliveryapp.databinding.FragmentListBinding
import com.zibro.fooddeliveryapp.view.base.BaseFragment
import org.koin.android.viewmodel.ext.android.viewModel

class RestaurantReviewListFragment: BaseFragment<RestaurantReviewListViewModel,FragmentListBinding>() {
    override val viewModel by viewModel<RestaurantReviewListViewModel>()

    override fun getViewBinding(): FragmentListBinding = FragmentListBinding.inflate(layoutInflater)

    override fun observeData() {

    }

    companion object{
        const val RESTAURANT_ID_KEY = "restaurantID"
        fun newInstance(restaurantId: Long, foodList : ArrayList<RestaurantFoodEntity>) : RestaurantReviewListFragment{
            val bundle = bundleOf(
                RESTAURANT_ID_KEY to restaurantId,
            )
            return RestaurantReviewListFragment().apply {
                arguments = bundle
            }
        }
    }
}