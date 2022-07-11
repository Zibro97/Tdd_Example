package com.zibro.fooddeliveryapp.view.main.like

import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.zibro.fooddeliveryapp.databinding.FragmentRestaurantLikeListBinding
import com.zibro.fooddeliveryapp.model.restaurant.RestaurantModel
import com.zibro.fooddeliveryapp.util.provider.ResourceProvider
import com.zibro.fooddeliveryapp.view.base.BaseFragment
import com.zibro.fooddeliveryapp.view.main.home.restaurant.detail.RestaurantDetailActivity
import com.zibro.fooddeliveryapp.widget.adapter.ModelRecyclerAdapter
import com.zibro.fooddeliveryapp.widget.adapter.listener.restaurant.RestaurantLikeListListener
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class RestaurantLikeListFragment:BaseFragment<RestaurantLikeListViewModel,FragmentRestaurantLikeListBinding>() {
    override val viewModel by viewModel<RestaurantLikeListViewModel>()

    override fun getViewBinding(): FragmentRestaurantLikeListBinding = FragmentRestaurantLikeListBinding.inflate(layoutInflater)

    private val resourcesProvider by inject<ResourceProvider>()

    private val adapter by lazy{
        ModelRecyclerAdapter<RestaurantModel,RestaurantLikeListViewModel>(listOf(),viewModel,resourcesProvider,adapterListener = object : RestaurantLikeListListener {
            override fun onDisLikeItem(model: RestaurantModel) {
                viewModel.disLikeRestaurant(model.toEntity())
            }

            override fun onClickItem(model: RestaurantModel) {
                startActivity(
                    RestaurantDetailActivity.newIntent(requireContext(),model.toEntity())
                )
            }
        })
    }

    override fun initViews() = with(binding){
        recyclerview.adapter = adapter
    }

    override fun observeData() = viewModel.restaurantListLiveData.observe(viewLifecycleOwner){
        checkListEmpty(it)
    }

    private fun checkListEmpty(restaurantList : List<RestaurantModel>){
        val isEmpty = restaurantList.isEmpty()
        binding.recyclerview.isGone = isEmpty
        binding.emptyResultTextView.isVisible = isEmpty
        if (isEmpty.not()) {
            adapter.submitList(restaurantList)
        }
    }

    companion object{
        fun newInstance() = RestaurantLikeListFragment()

        const val TAG = "restaurantLikeListFragment"


    }
}