package com.android.maptestapp.presentation.map

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.android.maptestapp.R
import com.android.maptestapp.base.utils.UIState
import com.android.maptestapp.base.utils.extensions.observe
import com.android.maptestapp.base.utils.extensions.toGoogle
import com.android.maptestapp.base.utils.extensions.toPx
import com.android.maptestapp.base.utils.extensions.toTomTom
import com.android.maptestapp.domain.model.DirectionModel
import com.android.maptestapp.domain.model.DirectionWithGasStationsModel
import com.android.maptestapp.domain.model.GasStationsModel
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.tomtom.online.sdk.common.location.LatLng
import kotlinx.android.synthetic.main.activity_maps.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MapActivity : AppCompatActivity(R.layout.activity_maps), OnMapReadyCallback {

    private var map: GoogleMap ?= null

    private val viewModel by viewModel<MapViewModel>()
    private var startPoint: LatLng? = null
    private var endPoint: LatLng? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        observe(viewModel.direction) { observeRoutePath(it) }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map?.uiSettings?.isZoomControlsEnabled = true
        map?.uiSettings?.isCompassEnabled = true
        map?.setOnMapLongClickListener {
            setPositionToBuildPath(it.toTomTom)
        }
    }

    private fun observeRoutePath(state: UIState<DirectionWithGasStationsModel>) {
        if (state is UIState.Success) {
            drawRoudeLinePath(state.data.direction)
            showGasStations(state.data.gasStations)
        }
        progressBar.isVisible = state is UIState.Loading
    }

    private fun showGasStations(gasStations: GasStationsModel) {
        gasStations.points.forEach {
            val icon = BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_BLUE)
            showMarker(it, icon)
        }
    }

    private fun setPositionToBuildPath(point: LatLng) {
        if (hasStartPoint() && hasEndPoint()) {
            startPoint = null
            endPoint = null
            map?.clear()
        }

        if (!hasStartPoint()) {
            startPoint = point
            val icon = BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_RED)
            showMarker(point, icon)
            return
        }

        if (!hasEndPoint()) {
            endPoint = point
            val icon = BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_RED)
            showMarker(point, icon)
        }
        if (startPoint == null || endPoint == null) return

        viewModel.findRoutePathAndGasStations(startPoint!!, endPoint!!)
    }

    private fun showMarker(
        point: LatLng,
        icon: BitmapDescriptor? = null
    ) {
        val markerOptions = MarkerOptions()
            .position(com.google.android.gms.maps.model.LatLng(point.latitude, point.longitude))
            .icon(icon)

        map?.addMarker(markerOptions)
    }

    private fun hasStartPoint(): Boolean = startPoint != null

    private fun hasEndPoint(): Boolean = endPoint != null

    private fun drawRoudeLinePath(pathModel: DirectionModel) {
        if (map == null) {
            return
        }
        if (pathModel.points.size < 2) {
            return
        }
        val options = PolylineOptions()
        options.color(Color.RED)
        options.width(5F.toPx)
        options.visible(true)
        pathModel.points.forEach { options.add(it.toGoogle) }
        map?.addPolyline(options)
    }
}
