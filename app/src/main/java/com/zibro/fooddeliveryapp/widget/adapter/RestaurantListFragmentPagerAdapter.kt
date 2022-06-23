package com.zibro.fooddeliveryapp.widget.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.zibro.fooddeliveryapp.view.main.home.restaurant.RestaurantListFragment


class RestaurantListFragmentPagerAdapter(
    fragment:Fragment,
    val fragmentList:List<RestaurantListFragment>
): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = fragmentList.size

    override fun createFragment(position: Int): Fragment = fragmentList[position]
}