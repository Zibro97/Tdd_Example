package com.zibro.fooddeliveryapp.view.main.home

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.zibro.fooddeliveryapp.R
import com.zibro.fooddeliveryapp.data.entity.LocationLatLngEntity
import com.zibro.fooddeliveryapp.data.entity.MapSearchInfoEntity
import com.zibro.fooddeliveryapp.databinding.FragmentHomeBinding
import com.zibro.fooddeliveryapp.view.base.BaseFragment
import com.zibro.fooddeliveryapp.view.main.MainActivity
import com.zibro.fooddeliveryapp.view.main.MainTabMenu
import com.zibro.fooddeliveryapp.view.main.home.restaurant.RestaurantCategory
import com.zibro.fooddeliveryapp.view.main.home.restaurant.RestaurantListFragment
import com.zibro.fooddeliveryapp.view.main.home.restaurant.RestaurantOrder
import com.zibro.fooddeliveryapp.view.mylocation.MyLocationActivity
import com.zibro.fooddeliveryapp.view.order.OrderMenuListActivity
import com.zibro.fooddeliveryapp.widget.adapter.RestaurantListFragmentPagerAdapter
import org.koin.android.viewmodel.ext.android.viewModel

class HomeFragment : BaseFragment<HomeViewModel,FragmentHomeBinding>() {
    override val viewModel by viewModel<HomeViewModel>()

    override fun getViewBinding(): FragmentHomeBinding = FragmentHomeBinding.inflate(layoutInflater)

    private lateinit var viewPagerAdapter : RestaurantListFragmentPagerAdapter

    private lateinit var locationManager : LocationManager

    private lateinit var myLocationListener : MyLocationListener

    private val firebaseAuth by lazy { FirebaseAuth.getInstance() }

    private val changeLocationLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result->
            if(result.resultCode == Activity.RESULT_OK){
                //MyLocationActivity에서 전달받은 결과값으로 위치 정보 수정
                result.data?.getParcelableExtra<MapSearchInfoEntity>(HomeViewModel.MY_LOCATION_KEY) ?.let { myLocationInfo->
                    viewModel.loadReverseGeoInformation(myLocationInfo.locationLatLngEntity)
                }
            }
    }

    private val locationPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        val responsePermissions = permissions.entries.filter {
            (it.key == Manifest.permission.ACCESS_FINE_LOCATION)
                    || (it.key == Manifest.permission.ACCESS_COARSE_LOCATION)
        }
        //permission true
        if(responsePermissions.filter { it.value == true}.size == locationPermissions.size){
            setMyLocationListener()
        }
        else{ //permission false
            with(binding.locationText){
                text = getString(R.string.please_request_location_permission)
                setOnClickListener{
                    getMyLocation()
                }
            }
            Toast.makeText(requireContext(),getString(R.string.can_not_assigned_permission),Toast.LENGTH_LONG).show()
        }
    }

    override fun observeData() = with(viewModel){
        viewModel.homeStateLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is HomeState.Uninitialized -> getMyLocation()
                is HomeState.Loading -> {
                    binding.locationLoading.isVisible = true
                    binding.locationText.setText(R.string.loading)
                }
                is HomeState.Success -> {
                    binding.locationLoading.isGone = true
                    binding.locationText.text = it.mapSearchInfoEntity.fullAddress
                    binding.tabLayout.isVisible = true
                    binding.filterScrollView.isVisible = true
                    binding.viewPager.isVisible = true
                    initViewPager(it.mapSearchInfoEntity.locationLatLngEntity)
                    if (it.isLocationSame.not()) {
                        Toast.makeText(requireContext(), "위치를 맞는지 확인해주세요!", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
                is HomeState.Error -> {
                    binding.locationLoading.isGone = true
                    binding.locationText.setText(R.string.location_not_found)
                    binding.locationText.setOnClickListener {
                        getMyLocation()
                    }
                    Toast.makeText(requireContext(), it.messageId, Toast.LENGTH_SHORT).show()
                }
            }
        }
        viewModel.foodMenuBasketLiveData.observe(viewLifecycleOwner) {
            if(it.isNotEmpty()){
                binding.basketButtonContainer.isVisible = true
                binding.basketCountTextview.text = getString(R.string.basket_count,it.size)
                binding.basketButton.setOnClickListener{
                    if(firebaseAuth.currentUser == null){
                        alertLoginNeed {
                            (requireActivity() as MainActivity).gotoTabMenu(MainTabMenu.MY)
                        }
                    }else {
                        startActivity(
                            OrderMenuListActivity.newIntent(requireContext())
                        )
                    }
                }
            }else {
                binding.basketButtonContainer.isGone = true
                binding.basketButton.setOnClickListener(null)
            }
        }
    }

    private fun alertLoginNeed(afterAction : () -> Unit){
        AlertDialog.Builder(requireContext())
            .setTitle("로그인이 필요합니다.")
            .setMessage("주문하려면 로그인이 필요합니다. MY 탭으로 이동하시겠습니까?")
            .setPositiveButton("이동") { dialog, _ ->
                afterAction()
                dialog.dismiss()
            }
            .setNegativeButton("취소") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    override fun initViews() = with(binding){
        locationText.setOnClickListener {
            viewModel.getMapSearchInfo()?.let { mapInfo ->
                changeLocationLauncher.launch(
                    MyLocationActivity.newIntent(requireContext(),mapInfo)
                )
            }
        }
        filterChipGroup.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId) {
                R.id.chip_default ->{
                    chipInit.isGone = true
                    changeRestaurantOrder(RestaurantOrder.DEFAULT)
                }
                R.id.chip_init ->{
                    chipDefault.isChecked = true
                }
                R.id.chip_low_delivery_tip -> {
                    chipInit.isVisible = true
                    changeRestaurantOrder(RestaurantOrder.LOW_DELIVERY_TIP)
                }
                R.id.chip_fast_delivery ->{
                    chipInit.isVisible = true
                    changeRestaurantOrder(RestaurantOrder.FAST_DELIVERY)
                }
                R.id.chip_top_rate -> {
                    chipInit.isVisible = true
                    changeRestaurantOrder(RestaurantOrder.TOP_RATE)
                }
            }
        }
    }

    private fun changeRestaurantOrder(order : RestaurantOrder){
        viewPagerAdapter.fragmentList.forEach {
            it.viewModel.setRestaurantOrder(order)
        }

    }

    private fun getMyLocation(){
        if(::locationManager.isInitialized.not()){
            locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        }
        val isGpsUnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if(isGpsUnabled) {
            locationPermissionLauncher.launch(locationPermissions)
        }
    }
    private fun initViewPager(locationLatLngEntity: LocationLatLngEntity) = with(binding){
        val restaurantCategories = RestaurantCategory.values()
        if(::viewPagerAdapter.isInitialized.not()){
            filterChipGroup.isVisible = true
            val restaurantListFragmentList = restaurantCategories.map {
                RestaurantListFragment.newInstance(it,locationLatLngEntity)
            }
            viewPagerAdapter = RestaurantListFragmentPagerAdapter(
                this@HomeFragment,
                restaurantListFragmentList,
                locationLatLngEntity
            )
            viewPager.adapter = viewPagerAdapter
            //매번 ViewPager의 Fragment가 바뀔때 마다 Fragment를 매번 새로 만드는 것이 아니라 기존것을 계속 쓸수있도록 설정
            viewPager.offscreenPageLimit = restaurantCategories.size
            //TabLayoutMediator : Tablayout에 Tab들을 뿌려주도록 설정
            TabLayoutMediator(tabLayout,viewPager) { tab,position ->
                tab.setText(restaurantCategories[position].categoryNameId)
            }.attach()
        }
        if(locationLatLngEntity != viewPagerAdapter.locationLatLngEntity){
            viewPagerAdapter.locationLatLngEntity = locationLatLngEntity
            viewPagerAdapter.fragmentList.forEach{
                it.viewModel.setLocationLatLng(locationLatLngEntity)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        //장바구니 개수 체크후
        viewModel.checkMyBasket()
        viewModel.fetchData()
    }

    @SuppressLint("MissingPermission")
    private fun setMyLocationListener() {
        val minTime = 1500L //1.5초
        val minDistance = 100f
        if(::myLocationListener.isInitialized.not()){
            myLocationListener = MyLocationListener()
        }
        with(locationManager){
            requestLocationUpdates(LocationManager.GPS_PROVIDER,minTime,minDistance,myLocationListener)
            requestLocationUpdates(LocationManager.NETWORK_PROVIDER,minTime,minDistance,myLocationListener)
        }
    }

    companion object {
        fun newInstance() = HomeFragment()

        val locationPermissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,

        )

        const val  TAG = "HomeFragment"
    }

    private fun removeLocationListener() {
        if(::locationManager.isInitialized && ::myLocationListener.isInitialized) {
            locationManager.removeUpdates(myLocationListener)
        }
    }

    inner class MyLocationListener : LocationListener{
        @SuppressLint("SetTextI18n")
        override fun onLocationChanged(location: Location) {
//            binding.locationText.text = "${location.latitude}/${location.longitude}"
//            binding.locationLoading.isVisible = false
            viewModel.loadReverseGeoInformation(
                LocationLatLngEntity(
                    latitude = location.latitude,
                    longitude = location.longitude
                )
            )
            removeLocationListener()
        }
    }
}