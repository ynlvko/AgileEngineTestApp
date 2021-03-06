package com.agileengine.ynlvko_test.images

import com.google.gson.annotations.SerializedName

class Image(
    @SerializedName("id") val id: String,
    @SerializedName("author") val author: String?,
    @SerializedName("camera") val camera: String?,
    @SerializedName("cropped_picture") val croppedUrl: String,
    @SerializedName("full_picture") val fullUrl: String?
)
