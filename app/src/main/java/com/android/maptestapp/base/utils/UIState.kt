package com.android.maptestapp.base.utils

sealed class UIState<out R> {
    object Loading: UIState<Nothing>()
    object Failure: UIState<Nothing>()
    object Empty: UIState<Nothing>()
    data class Success<T>(val data: T): UIState<T>()
}