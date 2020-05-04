package com.android.maptestapp.data.repository

import android.content.Context
import com.android.maptestapp.data.mapper.FromAlongRouteSearchResponseToGasStationsModelMapper
import com.android.maptestapp.data.mapper.FromRouteResponseToDirectionModelMapper
import com.android.maptestapp.domain.model.DirectionWithGasStationsModel
import com.tomtom.online.sdk.common.location.LatLng
import com.tomtom.online.sdk.routing.OnlineRoutingApi
import com.tomtom.online.sdk.routing.data.RouteQueryBuilder
import com.tomtom.online.sdk.routing.data.RouteType
import com.tomtom.online.sdk.search.OnlineSearchApi
import com.tomtom.online.sdk.search.SearchApi
import com.tomtom.online.sdk.search.data.alongroute.AlongRouteSearchQueryBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TomTomRepository(
    context: Context,
    private val routeResponseToDirectionModelMapper: FromRouteResponseToDirectionModelMapper,
    private val fromAlongRouteSearchResponseToGasStationsModelMapper: FromAlongRouteSearchResponseToGasStationsModelMapper

) {
    private val searchApi: SearchApi = OnlineSearchApi.create(context)
    private val routingApi = OnlineRoutingApi.create(context)

    suspend fun getDirectionsAndAlongGasStations(start: LatLng?, stop: LatLng?) =
        withContext(Dispatchers.Default) {
            val routeQuery = RouteQueryBuilder(start, stop).withRouteType(RouteType.FASTEST).build()
            val result = routingApi.planRoute(routeQuery).blockingGet()
            val routesCoordinates = routeResponseToDirectionModelMapper.map(result)

            val alongGasStations = searchApi.alongRouteSearch(
                AlongRouteSearchQueryBuilder.create(TERM_GAS_STATION, routesCoordinates.points, MAX_DETOUR_TIME).build()
            ).blockingGet()

            return@withContext DirectionWithGasStationsModel(
                routesCoordinates,
                fromAlongRouteSearchResponseToGasStationsModelMapper.map(alongGasStations)
            )
        }

    companion object {
        private const val MAX_DETOUR_TIME = 1000
        private const val TERM_GAS_STATION = "Gas Station"
    }
}