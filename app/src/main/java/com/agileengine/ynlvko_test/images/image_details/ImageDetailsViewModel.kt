package com.agileengine.ynlvko_test.images.image_details

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.agileengine.ynlvko_test.core.ServiceLocator
import com.agileengine.ynlvko_test.images.BaseImagesViewModel
import com.agileengine.ynlvko_test.images.Image
import com.agileengine.ynlvko_test.images.ImagesRepository
import io.reactivex.Flowable
import io.reactivex.Scheduler

class ImageDetailsViewModel(
    private val imagePosition: Int,
    private val imagesRepo: ImagesRepository,
    workerScheduler: Scheduler,
    resultScheduler: Scheduler
) : BaseImagesViewModel<Image>(
    workerScheduler, resultScheduler
) {
    init {
        fetchData()
    }

    override fun createDataObservable(): Flowable<Image> {
        return imagesRepo.getImageByPosition(imagePosition)
    }

    companion object {
        fun getInstance(fragment: Fragment, imagePosition: Int): ImageDetailsViewModel {
            return ViewModelProviders.of(fragment, object : ViewModelProvider.Factory {
                override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                    val serviceLocator = ServiceLocator.getInstance(fragment.requireContext())
                    val imagesRepo = serviceLocator.getImagesRepository()
                    val workerScheduler = serviceLocator.getWorkerScheduler()
                    val resultScheduler = serviceLocator.getResultScheduler()
                    return ImageDetailsViewModel(imagePosition, imagesRepo, workerScheduler, resultScheduler) as T
                }
            })[ImageDetailsViewModel::class.java]
        }
    }
}
