package com.agileengine.ynlvko_test.network

import com.google.gson.annotations.SerializedName

class AuthResponse(
    @SerializedName("token") val token: String?
)
