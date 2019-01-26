package com.agileengine.ynlvko_test.network

import com.agileengine.ynlvko_test.images.Image
import io.reactivex.Single
import retrofit2.http.*

interface ApiInterface {
    @POST("auth")
    fun auth(
        @Body apiKeyMap: Map<String, String>
    ): Single<AuthResponse>

    @GET("images")
    fun getImages(
        @Header("Authorization") bearerToken: String,
        @Query("page") page: Int?
    ): Single<GetImagesResponse>

    @GET("images/{imageId}")
    fun getImageById(
        @Header("Authorization") bearerToken: String,
        @Path("imageId") imageId: String
    ): Single<Image>
}
