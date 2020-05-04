package com.android.maptestapp.di

import com.android.maptestapp.domain.interactor.GetDirectionPathAndAlongGasStationsInteractor
import org.koin.dsl.module

val interactorsModule = module {
    factory { GetDirectionPathAndAlongGasStationsInteractor(get()) }
}