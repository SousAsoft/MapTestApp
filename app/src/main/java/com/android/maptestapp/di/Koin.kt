package com.android.maptestapp.di

import android.content.Context
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class Koin {
    companion object {
        fun startKoin(appContext: Context) {
            startKoin {
                androidContext(appContext)
                modules(
                    listOf(
                        repositoryModule,
                        viewModelModule,
                        mapperModule,
                        interactorsModule
                    )
                )
            }
        }
    }
}