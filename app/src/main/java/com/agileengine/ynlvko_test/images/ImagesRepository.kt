package com.agileengine.ynlvko_test.images

import io.reactivex.Flowable

interface ImagesRepository {
    fun getImages(page: Int): Flowable<List<Image>>
    fun getImageById(imageId: Int): Flowable<Image>
}

class DefaultImagesRepository(
    private val imagesSource: ImagesSource
) : ImagesRepository {
    private var lastPage = 0
    private val imagesCache = arrayListOf<Image>()

    override fun getImages(page: Int): Flowable<List<Image>> {
        if (lastPage < page) {
            return imagesSource.getImages(page)
                .flatMap {
                    imagesCache.addAll(it)
                    return@flatMap Flowable.just(imagesCache as List<Image>)
                }
        }
        return Flowable.just(imagesCache as List<Image>)
    }

    override fun getImageById(imageId: Int): Flowable<Image> {
        return imagesSource.getImageById(imageId)
    }
}
