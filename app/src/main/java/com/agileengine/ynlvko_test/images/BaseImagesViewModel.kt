package com.agileengine.ynlvko_test.images

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.Flowable
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable

abstract class BaseImagesViewModel<T>(
    private val workerScheduler: Scheduler,
    private val resultScheduler: Scheduler
) : ViewModel() {
    private val data = MutableLiveData<ViewObject<T>>().apply {
        value = ViewObject(data = null, progress = false, error = false)
    }

    private val disposables = CompositeDisposable()

    protected abstract fun createDataObservable(): Flowable<T>

    fun data(): LiveData<ViewObject<T>> = data

    protected fun fetchData() {
        val currentData = data.value
        if (currentData?.progress == true) {
            return
        }
        data.value = currentData?.copy(progress = true)
        disposables.add(
            createDataObservable()
                .subscribeOn(workerScheduler)
                .observeOn(resultScheduler)
                .subscribe(
                    {
                        val currentData = data.value
                        data.value = currentData?.copy(data = it, progress = false, error = false)
                    },
                    {
                        val currentData = data.value
                        data.value = currentData?.copy(progress = false, error = true)
                    }
                ))
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }

    data class ViewObject<T>(
        val data: T?,
        val progress: Boolean,
        val error: Boolean
    )
}
