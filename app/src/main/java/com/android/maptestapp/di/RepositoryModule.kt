package com.android.maptestapp.di

import com.android.maptestapp.data.repository.TomTomRepository
import org.koin.dsl.module

val repositoryModule = module {
    single { TomTomRepository(get(), get(), get()) }
}