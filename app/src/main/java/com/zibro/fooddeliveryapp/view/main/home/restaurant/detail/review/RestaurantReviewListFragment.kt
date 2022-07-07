package com.zibro.fooddeliveryapp.view.main.home.restaurant.detail.review

import android.widget.Toast
import androidx.core.os.bundleOf
import com.zibro.fooddeliveryapp.databinding.FragmentListBinding
import com.zibro.fooddeliveryapp.model.food.FoodModel
import com.zibro.fooddeliveryapp.model.review.RestaurantReviewModel
import com.zibro.fooddeliveryapp.util.provider.ResourceProvider
import com.zibro.fooddeliveryapp.view.base.BaseFragment
import com.zibro.fooddeliveryapp.widget.adapter.ModelRecyclerAdapter
import com.zibro.fooddeliveryapp.widget.adapter.listener.AdapterListener
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class RestaurantReviewListFragment: BaseFragment<RestaurantReviewListViewModel,FragmentListBinding>() {
    private val resourceProvider by inject<ResourceProvider>()
    private val adapter by lazy {
        ModelRecyclerAdapter<RestaurantReviewModel, RestaurantReviewListViewModel>(
            listOf(),
            viewModel,
            resourceProvider = resourceProvider,
            adapterListener = object : AdapterListener{}
        )
    }

    override val viewModel by viewModel<RestaurantReviewListViewModel>() {
        parametersOf(
            arguments?.getString(RESTAURANT_ID_KEY)
        )
    }

    override fun getViewBinding(): FragmentListBinding = FragmentListBinding.inflate(layoutInflater)

    override fun initViews() {
        binding.recyclerview.adapter = adapter
    }

    override fun observeData() = viewModel.reviewStateLiveData.observe(viewLifecycleOwner){
        when(it){
            is RestaurantReviewState.Success ->{
                handleSuccess(it)
            }
            else -> Unit
        }
    }

    private fun handleSuccess(state : RestaurantReviewState.Success){
        adapter.submitList(state.reviewList)
    }

    companion object{
        const val RESTAURANT_ID_KEY = "restaurantTitle"
        fun newInstance(restaurantTitle: String) : RestaurantReviewListFragment{
            val bundle = bundleOf(
                RESTAURANT_ID_KEY to restaurantTitle,
            )
            return RestaurantReviewListFragment().apply {
                arguments = bundle
            }
        }
    }
}