package com.android.maptestapp.di
import com.android.maptestapp.data.mapper.FromAlongRouteSearchResponseToGasStationsModelMapper
import com.android.maptestapp.data.mapper.FromRouteResponseToDirectionModelMapper
import org.koin.dsl.module

val mapperModule = module{
    factory { FromRouteResponseToDirectionModelMapper() }
    factory { FromAlongRouteSearchResponseToGasStationsModelMapper() }
}