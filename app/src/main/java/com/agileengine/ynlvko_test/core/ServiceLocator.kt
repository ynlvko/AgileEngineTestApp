package com.agileengine.ynlvko_test.core

import com.agileengine.ynlvko_test.images.DefaultImagesRepository
import com.agileengine.ynlvko_test.images.ImagesRepository
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

interface ServiceLocator {
    fun getImagesRepository(): ImagesRepository
    fun getWorkerScheduler(): Scheduler
    fun getResultScheduler(): Scheduler

    companion object {
        private val LOCK = Any()
        private var instance: ServiceLocator? = null

        fun getInstance(): ServiceLocator {
            synchronized(LOCK) {
                if (instance == null) {
                    instance = DefaultServiceLocator()
                }
                return instance!!
            }
        }
    }
}

class DefaultServiceLocator : ServiceLocator {

    private val imagesRepo by lazy { DefaultImagesRepository() }

    override fun getImagesRepository(): ImagesRepository {
        return imagesRepo
    }

    override fun getWorkerScheduler() = Schedulers.io()
    override fun getResultScheduler() = AndroidSchedulers.mainThread()
}
