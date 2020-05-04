package com.android.maptestapp.presentation.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.maptestapp.base.BaseViewModel
import com.android.maptestapp.base.utils.UIState
import com.android.maptestapp.domain.interactor.GetDirectionPathAndAlongGasStationsInteractor
import com.android.maptestapp.domain.model.DirectionWithGasStationsModel
import com.tomtom.online.sdk.common.location.LatLng

class MapViewModel(val getDirectionPathAndAlongGasStations: GetDirectionPathAndAlongGasStationsInteractor) : BaseViewModel() {

    private val _direction = MutableLiveData<UIState<DirectionWithGasStationsModel>>()
    val direction: LiveData<UIState<DirectionWithGasStationsModel>>
        get() = _direction

    fun findRoutePathAndGasStations(
        pointStart: LatLng, pointEnd: LatLng
    ) = runCoroutine(exceptionHandler = {
        _direction.value = UIState.Failure
        true
    }) {
        _direction.value = UIState.Loading
        val directionAndGasStations = getDirectionPathAndAlongGasStations(
            GetDirectionPathAndAlongGasStationsInteractor.Params(pointStart, pointEnd)
        )
        if (directionAndGasStations.direction.points.isNullOrEmpty()) {
            _direction.value = UIState.Empty
            return@runCoroutine
        }

        _direction.value = UIState.Success(directionAndGasStations)
    }
}