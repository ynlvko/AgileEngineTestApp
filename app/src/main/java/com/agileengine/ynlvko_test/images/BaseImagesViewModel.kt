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
    private val data = MutableLiveData<T>()
    private val progress = MutableLiveData<Boolean>()

    private val disposables = CompositeDisposable()

    protected abstract fun createDataObservable(): Flowable<T>

    fun data(): LiveData<T> = data
    fun progress(): LiveData<Boolean> = progress

    protected fun fetchData() {
        if (progress.value == true) {
            return
        }
        progress.value = true
        disposables.add(
            createDataObservable()
                .subscribeOn(workerScheduler)
                .observeOn(resultScheduler)
                .subscribe(
                    {
                        progress.value = false
                        data.value = it
                    },
                    { it.printStackTrace() }
                ))
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}
