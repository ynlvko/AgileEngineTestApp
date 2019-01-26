package com.agileengine.ynlvko_test.images

import com.agileengine.ynlvko_test.auth.AuthHolder
import com.agileengine.ynlvko_test.network.ApiInterface
import io.reactivex.Flowable
import retrofit2.adapter.rxjava2.HttpException

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
            .retry(3, ::retryPredicate)
    }

    override fun getImageById(imageId: String): Flowable<Image> {
        return getToken()
            .flatMap { token ->
                apiInterface.getImageById(getAuthorizationHeader(token), imageId).toFlowable()
            }
            .retry(retryCount, ::retryPredicate)
    }

    private fun retryPredicate(t: Throwable): Boolean {
        if (t is HttpException && t.code() == 401) {
            authHolder.clearToken()
            return true
        }
        return false
    }

    private fun getToken() = if (authHolder.isAuthorized()) {
        Flowable.just(authHolder.getToken())
    } else {
        authHolder.authorize().toFlowable()
    }

    private fun getAuthorizationHeader(token: String) = "Bearer $token"

    companion object {
        private const val retryCount = 3L
    }
}
