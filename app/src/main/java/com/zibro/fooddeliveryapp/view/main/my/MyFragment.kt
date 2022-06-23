package com.zibro.fooddeliveryapp.view.main.my

import androidx.fragment.app.viewModels
import com.zibro.fooddeliveryapp.databinding.FragmentMyBinding
import com.zibro.fooddeliveryapp.view.base.BaseFragment
import com.zibro.fooddeliveryapp.view.main.home.HomeFragment

class MyFragment : BaseFragment<MyViewModel,FragmentMyBinding>() {
    override val viewModel: MyViewModel by viewModels()

    override fun getViewBinding(): FragmentMyBinding = FragmentMyBinding.inflate(layoutInflater)

    override fun observeData() {
    }

    companion object {
        fun newInstance() = MyFragment()

        const val  TAG = "MyFragment"
    }
}