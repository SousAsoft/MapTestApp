package com.android.maptestapp.data.mapper

import com.android.maptestapp.base.mapper.Mapper
import com.android.maptestapp.domain.model.DirectionModel
import com.tomtom.online.sdk.common.location.LatLng
import com.tomtom.online.sdk.routing.data.RouteResponse

class FromRouteResponseToDirectionModelMapper : Mapper<RouteResponse, DirectionModel> {
    override suspend fun map(from: RouteResponse): DirectionModel {
        val pointsList = mutableListOf<LatLng>()
        from.routes?.forEach {
            pointsList.addAll(it.coordinates)
        }
        return DirectionModel(pointsList)
    }

}