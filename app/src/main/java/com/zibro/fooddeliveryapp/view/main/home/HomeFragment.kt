package com.zibro.fooddeliveryapp.view.main.home

import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import com.zibro.fooddeliveryapp.databinding.FragmentHomeBinding
import com.zibro.fooddeliveryapp.view.base.BaseFragment
import com.zibro.fooddeliveryapp.view.main.home.restaurant.RestaurantCategory
import com.zibro.fooddeliveryapp.view.main.home.restaurant.RestaurantListFragment
import com.zibro.fooddeliveryapp.widget.adapter.RestaurantListFragmentPagerAdapter

class HomeFragment : BaseFragment<HomeViewModel,FragmentHomeBinding>() {
    override val viewModel: HomeViewModel by viewModels()

    override fun getViewBinding(): FragmentHomeBinding = FragmentHomeBinding.inflate(layoutInflater)

    private lateinit var viewPagerAdapter : RestaurantListFragmentPagerAdapter

    override fun observeData() {
    }

    override fun initViews() {
        super.initViews()
        initViewPager()
    }

    private fun initViewPager() = with(binding){
        val restaurantCategories = RestaurantCategory.values()
        if(::viewPagerAdapter.isInitialized.not()){
            val restaurantListFragmentList = restaurantCategories.map {
                RestaurantListFragment.newInstance(it)
            }
            viewPagerAdapter = RestaurantListFragmentPagerAdapter(
                this@HomeFragment,
                restaurantListFragmentList
            )
            viewPager.adapter = viewPagerAdapter
        }
        //매번 ViewPager의 Fragment가 바뀔때 마다 Fragment를 매번 새로 만드는 것이 아니라 기존것을 계속 쓸수있도록 설정
        viewPager.offscreenPageLimit = restaurantCategories.size
        //TabLayoutMediator : Tablayout에 Tab들을 뿌려주도록 설정
        TabLayoutMediator(tabLayout,viewPager) { tab,position ->
            tab.setText(restaurantCategories[position].categoryNameId)
        }.attach()
    }

    companion object {
        fun newInstance() = HomeFragment()

        const val  TAG = "HomeFragment"
    }
}