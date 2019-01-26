package com.agileengine.ynlvko_test.images

import com.agileengine.ynlvko_test.auth.AuthHolder
import com.agileengine.ynlvko_test.network.ApiInterface
import io.reactivex.Flowable

interface ImagesSource {
    fun getImages(page: Int): Flowable<List<Image>>
    fun getImageById(imageId: String): Flowable<Image>
}

class DefaultImagesSource(
    private val authHolder: AuthHolder,
    private val apiInterface: ApiInterface
) : ImagesSource {
    override fun getImages(page: Int): Flowable<List<Image>> {
        return getToken()
            .flatMap { token ->
                apiInterface.getImages(getAuthorizationHeader(token), page)
                    .map { it.pictures as List<Image> }
                    .toFlowable()
            }
            .onErrorResumeNext { t: Throwable ->
                authHolder.clearToken()
                getImages(page)
            }
    }

    override fun getImageById(imageId: String): Flowable<Image> {
        return getToken()
            .flatMap { token ->
                apiInterface.getImageById(getAuthorizationHeader(token), imageId).toFlowable()
            }
            .onErrorResumeNext { t: Throwable ->
                authHolder.clearToken()
                getImageById(imageId)
            }
    }

    private fun getToken() = if (authHolder.isAuthorized()) {
        Flowable.just(authHolder.getToken())
    } else {
        authHolder.authorize().toFlowable()
    }

    private fun getAuthorizationHeader(token: String) = "Bearer $token"
}
