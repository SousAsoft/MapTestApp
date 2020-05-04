package com.android.maptestapp

import android.app.Application
import com.android.maptestapp.di.Koin


class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Koin.startKoin(this)
    }
}