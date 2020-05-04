package com.android.maptestapp.domain.interactor

import com.android.maptestapp.base.interactor.Interactor
import com.android.maptestapp.data.repository.TomTomRepository
import com.android.maptestapp.domain.model.DirectionWithGasStationsModel
import com.tomtom.online.sdk.common.location.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetDirectionPathAndAlongGasStationsInteractor(private val tomtomRepositoryTom: TomTomRepository) :
    Interactor<GetDirectionPathAndAlongGasStationsInteractor.Params, DirectionWithGasStationsModel> {

    override suspend fun invoke(executeParams: Params): DirectionWithGasStationsModel =
        withContext(Dispatchers.Default) {
            tomtomRepositoryTom.getDirectionsAndAlongGasStations(executeParams.startPoint, executeParams.endPoint)
        }

    data class Params(
        val startPoint: LatLng,
        val endPoint: LatLng
    )
}