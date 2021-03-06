package com.zibro.fooddeliveryapp.view.main.home.restaurant.detail

import android.content.ClipDescription.MIMETYPE_TEXT_PLAIN
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.zibro.fooddeliveryapp.R
import com.zibro.fooddeliveryapp.data.entity.RestaurantEntity
import com.zibro.fooddeliveryapp.data.entity.restaurant.RestaurantFoodEntity
import com.zibro.fooddeliveryapp.databinding.ActivityRestaurantDetailBinding
import com.zibro.fooddeliveryapp.extension.fromDpToPx
import com.zibro.fooddeliveryapp.extension.load
import com.zibro.fooddeliveryapp.util.event.MenuChangeEventBus
import com.zibro.fooddeliveryapp.view.base.BaseActivity
import com.zibro.fooddeliveryapp.view.main.MainTabMenu
import com.zibro.fooddeliveryapp.view.main.home.restaurant.RestaurantListFragment
import com.zibro.fooddeliveryapp.view.main.home.restaurant.detail.menu.RestaurantMenuListFragment
import com.zibro.fooddeliveryapp.view.main.home.restaurant.detail.review.RestaurantReviewListFragment
import com.zibro.fooddeliveryapp.view.order.OrderMenuListActivity
import com.zibro.fooddeliveryapp.widget.adapter.RestaurantDetailListFragmentPagerAdapter
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import kotlin.math.abs

class RestaurantDetailActivity : BaseActivity<RestaurantDetailViewModel,ActivityRestaurantDetailBinding>() {
    private lateinit var viewPagerAdapter : RestaurantDetailListFragmentPagerAdapter

    private val firebaseAuth by lazy { FirebaseAuth.getInstance() }

    private val menuChangeEventBus by inject<MenuChangeEventBus>()

    override val viewModel by viewModel<RestaurantDetailViewModel>(){
        parametersOf(
            intent.getParcelableExtra<RestaurantEntity>(RestaurantListFragment.RESTAURANT_KEY)
        )
    }

    override fun getViewBinding(): ActivityRestaurantDetailBinding = ActivityRestaurantDetailBinding.inflate(layoutInflater)

    override fun initViews() {
        initAppBar()
    }
    private fun initAppBar() = with(binding){
        appbar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener{ appBarLayout, verticalOffset ->
            val topPadding = 300f.fromDpToPx().toFloat()
            val realAlphaScrollHeight = appBarLayout.measuredHeight - appBarLayout.totalScrollRange
            val abstractOffset = abs(verticalOffset)

            val realAlphaVerticalOffset : Float = if(abstractOffset - topPadding < 0) 0f else abstractOffset - topPadding

            if(abstractOffset < topPadding) {
                restaurantTitleTextview.alpha = 0f
                return@OnOffsetChangedListener
            }

            val percentage = realAlphaVerticalOffset / realAlphaScrollHeight
            restaurantTitleTextview.alpha = 1 - (if(1 - percentage * 2 < 0) 0f else 1 - percentage * 2)
        })
        toolbar.setNavigationOnClickListener { finish() }
        callButton.setOnClickListener {
            viewModel.getRestaurantTelNumber()?.let { telNo ->
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$telNo"))
                startActivity(intent)
            }
        }
        //??? ??????
        likeButton.setOnClickListener {
            viewModel.toggleLikedRestaurant()
        }
        shareButton.setOnClickListener {
            viewModel.getRestaurantInfo()?.let { restaurantEntity ->
                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = MIMETYPE_TEXT_PLAIN
                    putExtra(
                        Intent.EXTRA_TEXT,
                        "?????? :${restaurantEntity.restaurantTitle}" +
                                "\n?????? : ${restaurantEntity.grade}"+
                                "\n????????? : ${restaurantEntity.restaurantTelNumber}"
                    )
                    Intent.createChooser(this, "???????????? ????????????")
                }
                startActivity(intent)
            }
        }
    }

    override fun observeData() = viewModel.restaurantDetailStateLiveData.observe(this){
        when(it){
            is RestaurantDetailState.Loading -> {
                handleLoading()
            }
            is RestaurantDetailState.Success ->{
                handleSuccess(it)
            }
            else -> Unit
        }
    }

    private fun handleLoading() = with(binding){
        progressbar.isVisible = true
    }

    private fun handleSuccess(state : RestaurantDetailState.Success) = with(binding){
        progressbar.isGone = true
        val restaurantEntity = state.restaurantEntity

        callButton.isGone = restaurantEntity.restaurantTelNumber == null

        restaurantTitleTextview.text = restaurantEntity.restaurantTitle
        restaurantImage.load(restaurantEntity.restaurantImageUrl)
        restaurantMainTitleTextview.text = restaurantEntity.restaurantTitle
        ratingbar.rating = restaurantEntity.grade
        deliveryTimeText.text = getString(R.string.delivery_expected_time,restaurantEntity.deliveryTimeRange.first,restaurantEntity.deliveryTimeRange.second)
        deliveryTipText.text = getString(R.string.delivery_tip_range,restaurantEntity.deliveryTipRange.first,restaurantEntity.deliveryTipRange.second)
        likeText.setCompoundDrawablesWithIntrinsicBounds(
            ContextCompat.getDrawable(this@RestaurantDetailActivity, if (state.isLiked == true) {
                R.drawable.ic_heart_enable
            } else {
                R.drawable.ic_heart_disable
            }),
            null, null, null
        )
        if(::viewPagerAdapter.isInitialized.not()){
            initViewPager(state.restaurantEntity.restaurantInfoId,state.restaurantEntity.restaurantTitle, state.restaurantFoodList)
        }
        notifyBasketCount(state.foodMenuListInBasket)

        val (isClearNeed,afterAction) = state.isClearNeedInBasketAndAction
        if(isClearNeed) {
            alertClearNeedInBasket(afterAction)
        }
    }

    private fun initViewPager(restaurantInfoId : Long, restaurantTitle : String,  restaurantFoodList : List<RestaurantFoodEntity>?){
        viewPagerAdapter = RestaurantDetailListFragmentPagerAdapter(
            this,
            listOf(
                RestaurantMenuListFragment.newInstance(
                    restaurantInfoId,ArrayList(restaurantFoodList ?: listOf()
                    )
                ),
                RestaurantReviewListFragment.newInstance(
                    restaurantTitle
                ),
            )
        )
        binding.menuAndReviewViewpager.adapter= viewPagerAdapter
        TabLayoutMediator(binding.menuAndReviewTabLayout, binding.menuAndReviewViewpager) { tab,position ->
            tab.setText(RestaurantCategoryDetail.values()[position].categoryNameId)
        }.attach()
    }

    private fun notifyBasketCount(foodMenuListInBasket: List<RestaurantFoodEntity>?) = with(binding){
        basketCountTextView.text = if(foodMenuListInBasket.isNullOrEmpty()){
            "0"
        } else {
            getString(R.string.basket_count,foodMenuListInBasket.size)
        }
        basketButton.setOnClickListener {
            if (firebaseAuth.currentUser == null){
                alertLoginNeed {
                    lifecycleScope.launch {
                        menuChangeEventBus.changeMenu(MainTabMenu.MY)
                        finish()
                    }
                }
            } else {
                startActivity(
                    OrderMenuListActivity.newIntent(this@RestaurantDetailActivity)
                )
            }
        }
    }

    private fun alertLoginNeed(afterAction : () -> Unit){
        AlertDialog.Builder(this)
            .setTitle("???????????? ???????????????.")
            .setMessage("??????????????? ???????????? ???????????????. MY ????????? ?????????????????????????")
            .setPositiveButton("??????") { dialog, _ ->
                afterAction()
                dialog.dismiss()
            }
            .setNegativeButton("??????") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun alertClearNeedInBasket(afterAction: () -> Unit) {
        AlertDialog.Builder(this)
            .setTitle("?????????????????? ?????? ????????? ????????? ?????? ??? ????????????.")
            .setMessage("???????????? ????????? ??????????????? ?????? ?????? ????????? ?????? ????????? ???????????????.")
            .setPositiveButton("??????") { dialog, _ ->
                viewModel.notifyClearBasket()
                afterAction()
                dialog.dismiss()
            }
            .setNegativeButton("??????") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    companion object{
        fun newIntent(context: Context, restaurantEntity:RestaurantEntity) = Intent(context, RestaurantDetailActivity::class.java).apply{
            putExtra(RestaurantListFragment.RESTAURANT_KEY,restaurantEntity)
        }
    }
}