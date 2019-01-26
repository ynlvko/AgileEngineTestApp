package com.agileengine.ynlvko_test.images

import io.reactivex.Flowable

interface ImagesRepository {
    fun getImages(page: Int): Flowable<List<Image>>
    fun getImageById(imageId: Int): Flowable<Image>
}

class DefaultImagesRepository(
    private val imagesSource: ImagesSource
) : ImagesRepository {

    override fun getImages(page: Int): Flowable<List<Image>> {
        return imagesSource.getImages(page)
    }

    override fun getImageById(imageId: Int): Flowable<Image> {
        return imagesSource.getImageById(imageId)
    }
}
