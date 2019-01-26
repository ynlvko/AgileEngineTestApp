package com.agileengine.ynlvko_test.network

import com.agileengine.ynlvko_test.images.Image
import com.google.gson.annotations.SerializedName

class GetImagesResponse(
    @SerializedName("pictures") val pictures: ArrayList<Image>
)
