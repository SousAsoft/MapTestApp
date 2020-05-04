package com.android.maptestapp.data.mapper

import com.android.maptestapp.base.mapper.Mapper
import com.android.maptestapp.domain.model.GasStationsModel
import com.tomtom.online.sdk.common.location.LatLng
import com.tomtom.online.sdk.search.data.alongroute.AlongRouteSearchResponse

class FromAlongRouteSearchResponseToGasStationsModelMapper : Mapper<AlongRouteSearchResponse, GasStationsModel> {

    override suspend fun map(from: AlongRouteSearchResponse): GasStationsModel {
        val pointsList = mutableListOf<LatLng>()
        from.results?.forEach{
            pointsList.add(it.position)
        }
        return GasStationsModel(pointsList)
    }

}