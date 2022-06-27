package com.zibro.fooddeliveryapp.view.main.home.restaurant

import android.util.Log
import android.widget.Toast
import androidx.core.os.bundleOf
import com.zibro.fooddeliveryapp.data.entity.LocationLatLngEntity
import com.zibro.fooddeliveryapp.databinding.FragmentRestaurantListBinding
import com.zibro.fooddeliveryapp.model.restaurant.RestaurantModel
import com.zibro.fooddeliveryapp.util.provider.ResourceProvider
import com.zibro.fooddeliveryapp.view.base.BaseFragment
import com.zibro.fooddeliveryapp.widget.adapter.ModelRecyclerAdapter
import com.zibro.fooddeliveryapp.widget.adapter.listener.restaurant.RestaurantListListener
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class RestaurantListFragment : BaseFragment<RestaurantListViewModel,FragmentRestaurantListBinding>() {
    private val restaurantCategory by lazy { arguments?.getSerializable(RESTAURANT_CATEGORY_KEY) as RestaurantCategory }
    private val locationLatLng by lazy { arguments?.getParcelable<LocationLatLngEntity>(LOCATION_KEY) }
    override val viewModel by viewModel<RestaurantListViewModel>{ parametersOf(restaurantCategory,locationLatLng)}

    override fun getViewBinding(): FragmentRestaurantListBinding = FragmentRestaurantListBinding.inflate(layoutInflater)
    private val resourcesProvider by inject<ResourceProvider>()
    private val adapter by lazy {
        ModelRecyclerAdapter<RestaurantModel,RestaurantListViewModel>(listOf(), viewModel,adapterListener = object :RestaurantListListener{
            override fun onClickItem(model: RestaurantModel) {
                Toast.makeText(requireContext(),"$model",Toast.LENGTH_LONG).show()
            }
        }, resourceProvider = resourcesProvider)
    }

    override fun initViews() = with(binding){
        restaurantRecyclerview.adapter = adapter
    }
    override fun observeData() {
        viewModel.restaurantListLiveData.observe(viewLifecycleOwner){
            Log.e("TAG", "observeData: ${it.toString()}")
            adapter.submitList(it)
        }
    }

    companion object{
        const val RESTAURANT_CATEGORY_KEY = "restaurantCategory"
        const val LOCATION_KEY = "location"
        fun newInstance(restaurantCategory: RestaurantCategory,locationLatLngEntity: LocationLatLngEntity)= RestaurantListFragment().apply {
            arguments = bundleOf(
                // TODO: 2022/06/23 아래 코드 kotlinx 더 알아보기
                RESTAURANT_CATEGORY_KEY to restaurantCategory,
                LOCATION_KEY to locationLatLngEntity
            )
        }
    }
}