package com.zibro.fooddeliveryapp.view.mylocation

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.zibro.fooddeliveryapp.R
import com.zibro.fooddeliveryapp.data.entity.LocationLatLngEntity
import com.zibro.fooddeliveryapp.data.entity.MapSearchInfoEntity
import com.zibro.fooddeliveryapp.databinding.ActivityMyLocationBinding
import com.zibro.fooddeliveryapp.view.base.BaseActivity
import com.zibro.fooddeliveryapp.view.main.home.HomeViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class MyLocationActivity : BaseActivity<MyLocationViewModel, ActivityMyLocationBinding>(),
    OnMapReadyCallback {
    private lateinit var map: GoogleMap

    private var isMapInitialized: Boolean = false
    private var isChangeLocation: Boolean = false

    override val viewModel by viewModel<MyLocationViewModel> {
        parametersOf(
            intent.getParcelableExtra<MapSearchInfoEntity>(HomeViewModel.MY_LOCATION_KEY)
        )
    }

    override fun getViewBinding() = ActivityMyLocationBinding.inflate(layoutInflater)

    override fun observeData() = viewModel.myLocationStateLiveData.observe(this) {
        when (it) {
            is MyLocationState.Loading -> {
                handleLoadingState()
            }
            is MyLocationState.Success -> {
                if (::map.isInitialized) {
                    handleSuccessState(it)
                }
            }
            is MyLocationState.Confirm -> {

            }
            is MyLocationState.Error -> {
                Toast.makeText(this,it.messageId, Toast.LENGTH_SHORT).show()
            }
            else -> Unit
        }
    }

    override fun initViews() = with(binding) {
        toolbar.setNavigationOnClickListener {
            finish()
        }
        confirmButton.setOnClickListener {

        }
        setUpGoogleMap()
    }

    private fun setUpGoogleMap() {
        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    companion object {
        fun newIntent(context: Context, mapSearchInfoEntity: MapSearchInfoEntity): Intent =
            Intent(context, MyLocationActivity::class.java).apply {
                putExtra(HomeViewModel.MY_LOCATION_KEY, mapSearchInfoEntity)
            }

        const val CAMERA_ZOOM_LEVEL = 17f
    }

    override fun onMapReady(map: GoogleMap) {
        this.map = map ?: return
        viewModel.fetchData()
    }

    private fun handleLoadingState() = with(binding) {
        locationLoading.isVisible = true
        locationTitleTextView.setText(R.string.loading)
    }

    private fun handleSuccessState(state: MyLocationState.Success) = with(binding) {
        val mapSearchInfoEntity = state.mapSearchInfoEntity
        locationLoading.isGone = true
        locationTitleTextView.text = mapSearchInfoEntity.fullAddress

        if (isMapInitialized.not()) {
            map.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(
                        mapSearchInfoEntity.locationLatLngEntity.latitude,
                        mapSearchInfoEntity.locationLatLngEntity.longitude
                    ), CAMERA_ZOOM_LEVEL
                )
            )
            //지도가 멈춰있는지 판단해주는 Listener
            map.setOnCameraIdleListener {
                if (isChangeLocation.not()) {
                    isChangeLocation = true
                    Handler(Looper.getMainLooper()).postDelayed({
                        val cameraLatLng = map.cameraPosition.target
                        viewModel.changeLocationInfo(
                            LocationLatLngEntity(
                                cameraLatLng.latitude,
                                cameraLatLng.longitude
                            )
                        )
                        isChangeLocation = false
                    }, 1000)
                }
            }
            isMapInitialized = true
        }
    }
}