package com.agileengine.ynlvko_test.images

import io.reactivex.Flowable

interface ImagesRepository {
    fun getImages(page: Int): Flowable<List<Image>>
    fun getImageByPosition(imagePosition: Int): Flowable<Image>
}

class DefaultImagesRepository(
    private val imagesSource: ImagesSource
) : ImagesRepository {
    private val LOCK = Any()

    private var lastPage = 0
    private val imagesCache = arrayListOf<Image>()

    override fun getImages(page: Int): Flowable<List<Image>> {
        if (lastPage < page) {
            synchronized(LOCK) {
                return imagesSource.getImages(page)
                    .flatMap {
                        imagesCache.addAll(it)
                        return@flatMap Flowable.just(imagesCache as List<Image>)
                    }
            }
        }
        return Flowable.just(imagesCache as List<Image>)
    }

    override fun getImageByPosition(imagePosition: Int): Flowable<Image> {
        val image = imagesCache[imagePosition]
        if (image.fullUrl != null) {
            return Flowable.just(image)
        }
        synchronized(LOCK) {
            return imagesSource.getImageById(image.id)
                .flatMap { newImage ->
                    val oldImage = imagesCache[imagePosition]
                    if (oldImage.id == newImage.id) {
                        imagesCache[imagePosition] = newImage
                    } else {
                        val newIndex = imagesCache.indexOf(oldImage)
                        if (newIndex != -1) {
                            imagesCache[newIndex] = newImage
                        }
                    }
                    return@flatMap Flowable.just(newImage)
                }
        }
    }
}
