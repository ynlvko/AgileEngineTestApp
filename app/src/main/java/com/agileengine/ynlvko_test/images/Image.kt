package com.agileengine.ynlvko_test.images

import com.google.gson.annotations.SerializedName

class Image(
    @SerializedName("id") val id: String,
    @SerializedName("cropped_picture") val croppedUrl: String
)
