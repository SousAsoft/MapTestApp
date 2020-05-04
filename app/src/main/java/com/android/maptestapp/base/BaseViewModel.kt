package com.android.maptestapp.base

import androidx.annotation.CallSuper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.maptestapp.base.utils.SingleLiveData
import kotlinx.coroutines.*
import org.koin.android.BuildConfig
import kotlin.coroutines.CoroutineContext

open class BaseViewModel : ViewModel() {
    private val exceptionHandler: CoroutineContext =
        CoroutineExceptionHandler { coroutineContext, throwable ->
            handleException(coroutineContext, throwable)
        }

    private val job = SupervisorJob()
    protected val uiScope = CoroutineScope(Dispatchers.Main + job + exceptionHandler)


    /**Livedata for posting error to UI*/
    val error = SingleLiveData<Throwable>()

    /**Livedata for showing progress on UI*/
    protected val _progress = MutableLiveData<Boolean>()
    val progress: LiveData<Boolean>
        get() = _progress

    override fun onCleared() {
        super.onCleared()
        uiScope.coroutineContext.cancelChildren()
    }

    @CallSuper
    open fun handleException(coroutineContext: CoroutineContext, throwable: Throwable) {
        if (BuildConfig.DEBUG) throwable.printStackTrace()
        error.postValue(throwable)
    }

    protected fun runCoroutine(
        dispatcher: CoroutineDispatcher = Dispatchers.Main,
        exceptionHandler: ((Exception) -> Boolean)? = null,
        withProgress: Boolean = false,
        block: suspend CoroutineScope.() -> Unit
    ): Job {
        return uiScope.launch(dispatcher) {
            if (withProgress) _progress.postValue(true)
            try {
                block.invoke(this)
            } catch (e: Exception) {
                if (exceptionHandler?.invoke(e) != false)
                    handleException(coroutineContext, e)
            }
            if (withProgress) _progress.postValue(false)
        }
    }
}