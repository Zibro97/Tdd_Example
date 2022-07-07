package com.zibro.fooddeliveryapp.view.main.home.restaurant.detail

import android.content.ClipDescription.MIMETYPE_TEXT_PLAIN
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.zibro.fooddeliveryapp.R
import com.zibro.fooddeliveryapp.data.entity.RestaurantEntity
import com.zibro.fooddeliveryapp.data.entity.restaurant.RestaurantFoodEntity
import com.zibro.fooddeliveryapp.databinding.ActivityRestaurantDetailBinding
import com.zibro.fooddeliveryapp.extension.fromDpToPx
import com.zibro.fooddeliveryapp.extension.load
import com.zibro.fooddeliveryapp.view.base.BaseActivity
import com.zibro.fooddeliveryapp.view.main.home.restaurant.RestaurantListFragment
import com.zibro.fooddeliveryapp.view.main.home.restaurant.detail.menu.RestaurantMenuListFragment
import com.zibro.fooddeliveryapp.view.main.home.restaurant.detail.review.RestaurantReviewListFragment
import com.zibro.fooddeliveryapp.widget.adapter.RestaurantDetailListFragmentPagerAdapter
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import kotlin.math.abs

class RestaurantDetailActivity : BaseActivity<RestaurantDetailViewModel,ActivityRestaurantDetailBinding>() {
    private lateinit var viewPagerAdapter : RestaurantDetailListFragmentPagerAdapter

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
        //찜 기능
        likeButton.setOnClickListener {
            viewModel.toggleLikedRestaurant()
        }
        shareButton.setOnClickListener {
            /***
             * viewmodel에서 getRestaurantInfo가 있으면 info 객체를 Intent의
             * */
            viewModel.getRestaurantInfo()?.let { restaurantEntity ->
                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = MIMETYPE_TEXT_PLAIN
                    putExtra(
                        Intent.EXTRA_TEXT,
                        "맛집 :${restaurantEntity.restaurantTitle}" +
                                "\n평점 : ${restaurantEntity.grade}"+
                                "\n연락처 : ${restaurantEntity.restaurantTelNumber}"
                    )
                    Intent.createChooser(this, "친구에게 공유하기")
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
                // TODO: 2022/07/03 fragmentList
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
            // TODO: 2022/07/04 주문하기 View로 이동 or 로그인
        }
    }

    private fun alertClearNeedInBasket(afterAction: () -> Unit) {
        AlertDialog.Builder(this)
            .setTitle("장바구니에는 같은 가게의 메뉴만 담을 수 있습니다.")
            .setMessage("선택하신 메뉴를 장바구니에 담을 경우 이전에 담은 메뉴가 삭제됩니다.")
            .setPositiveButton("담기") { dialog, _ ->
                viewModel.notifyClearBasket()
                afterAction()
                dialog.dismiss()
            }
            .setNegativeButton("취소") { dialog, _ ->
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