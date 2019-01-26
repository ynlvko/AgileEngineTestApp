package com.agileengine.ynlvko_test.auth

import android.content.SharedPreferences
import com.agileengine.ynlvko_test.network.ApiInterface
import io.reactivex.Single

interface AuthHolder {
    fun authorize(): Single<String>
    fun isAuthorized(): Boolean
    fun getToken(): String
    fun clearToken()
}

class DefaultAuthHolder(
    private val sharedPreferences: SharedPreferences,
    private val apiInterface: ApiInterface
) : AuthHolder {

    override fun authorize(): Single<String> {
        return apiInterface.auth(HashMap<String, String>().apply {
            put("apiKey", "23567b218376f79d9415")
        })
            .flatMap { authResponse ->
                val token = authResponse.token
                if (!token.isNullOrBlank()) {
                    sharedPreferences.edit().apply {
                        putString(KeyToken, token)
                        apply()
                    }
                    return@flatMap Single.just(token)
                } else {
                    sharedPreferences.edit().apply {
                        putString(KeyToken, "")
                    }
                    return@flatMap Single.just("")
                }
            }
    }

    override fun isAuthorized(): Boolean {
        return !sharedPreferences.getString(KeyToken, "").isNullOrBlank()
    }

    override fun getToken(): String {
        return sharedPreferences.getString(KeyToken, "")!!
    }

    override fun clearToken() {
        sharedPreferences.edit().putString(KeyToken, "").apply()
    }

    companion object {
        private const val KeyToken = "KeyToken"
    }
}
