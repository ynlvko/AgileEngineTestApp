package com.agileengine.ynlvko_test.images

import io.reactivex.Flowable

interface ImagesRepository {
    fun getImages(page: Int): Flowable<List<Image>>
    fun getImageById(imageId: Int): Flowable<Image>
}

class DefaultImagesRepository : ImagesRepository {
    val images = arrayListOf(
        Image(), Image(), Image(), Image(), Image(), Image(), Image(), Image(), Image(), Image(), Image(), Image()
    )

    override fun getImages(page: Int): Flowable<List<Image>> {
        return Flowable.just(images)
    }

    override fun getImageById(imageId: Int): Flowable<Image> {
        return Flowable.just(images[imageId])
    }
}
