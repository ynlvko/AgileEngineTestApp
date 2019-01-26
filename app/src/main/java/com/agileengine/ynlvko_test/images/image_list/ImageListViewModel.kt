package com.agileengine.ynlvko_test.images.image_list

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.agileengine.ynlvko_test.core.ServiceLocator
import com.agileengine.ynlvko_test.images.BaseImagesViewModel
import com.agileengine.ynlvko_test.images.Image
import com.agileengine.ynlvko_test.images.ImagesRepository
import io.reactivex.Flowable
import io.reactivex.Scheduler

class ImageListViewModel(
    private val imagesRepo: ImagesRepository,
    workerScheduler: Scheduler,
    resultScheduler: Scheduler
) : BaseImagesViewModel<List<Image>>(
    workerScheduler, resultScheduler
) {

    private var page = 0

    override fun createDataObservable(): Flowable<List<Image>> {
        return imagesRepo.getImages(++page)
    }

    init {
        fetchNextPage()
    }

    fun fetchNextPage() {
        fetchData()
    }

    companion object {
        fun getInstance(activity: FragmentActivity): ImageListViewModel {
            return ViewModelProviders.of(activity, object : ViewModelProvider.Factory {
                override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                    val serviceLocator = ServiceLocator.getInstance(activity)
                    val imagesRepo = serviceLocator.getImagesRepository()
                    val workerScheduler = serviceLocator.getWorkerScheduler()
                    val resultScheduler = serviceLocator.getResultScheduler()
                    return ImageListViewModel(
                        imagesRepo,
                        workerScheduler,
                        resultScheduler
                    ) as T
                }
            })[ImageListViewModel::class.java]
        }
    }
}
