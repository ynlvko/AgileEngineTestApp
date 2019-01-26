package com.agileengine.ynlvko_test.images

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*
import com.agileengine.ynlvko_test.core.ServiceLocator
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable

class ImagesViewModel(
    private val imagesRepo: ImagesRepository,
    private val workerScheduler: Scheduler,
    private val resultScheduler: Scheduler
) : ViewModel() {
    private val data = MutableLiveData<List<Image>>()
    private val progress = MutableLiveData<Boolean>()

    fun data(): LiveData<List<Image>> = data
    fun progress(): LiveData<Boolean> = progress

    private val disposables = CompositeDisposable()
    private var page = 0

    init {
        fetchNextPage()
    }

    fun fetchNextPage() {
        progress.value = true
        disposables.add(
            imagesRepo.getImages(++page)
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
        disposables.dispose()
    }

    companion object {
        fun getInstance(activity: FragmentActivity): ImagesViewModel {
            return ViewModelProviders.of(activity, object : ViewModelProvider.Factory {
                override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                    val serviceLocator = ServiceLocator.getInstance(activity)
                    val imagesRepo = serviceLocator.getImagesRepository()
                    val workerScheduler = serviceLocator.getWorkerScheduler()
                    val resultScheduler = serviceLocator.getResultScheduler()
                    return ImagesViewModel(imagesRepo, workerScheduler, resultScheduler) as T
                }
            })[ImagesViewModel::class.java]
        }
    }
}
