package com.android.maptestapp.base.utils.extensions

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

inline fun <T> AppCompatActivity.observe(liveData: LiveData<T>, crossinline callback: (T) -> Unit) {
    liveData.observe(this, Observer {
        callback.invoke(it)
    })
}