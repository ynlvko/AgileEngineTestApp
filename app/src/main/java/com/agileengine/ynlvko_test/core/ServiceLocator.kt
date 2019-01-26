package com.agileengine.ynlvko_test.core

import android.content.Context
import com.agileengine.ynlvko_test.auth.DefaultAuthHolder
import com.agileengine.ynlvko_test.images.DefaultImagesRepository
import com.agileengine.ynlvko_test.images.DefaultImagesSource
import com.agileengine.ynlvko_test.images.ImagesRepository
import com.agileengine.ynlvko_test.network.ApiInterface
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

interface ServiceLocator {
    fun getImagesRepository(): ImagesRepository
    fun getWorkerScheduler(): Scheduler
    fun getResultScheduler(): Scheduler

    companion object {
        private val LOCK = Any()
        private var instance: ServiceLocator? = null

        fun getInstance(context: Context): ServiceLocator {
            synchronized(LOCK) {
                if (instance == null) {
                    instance = DefaultServiceLocator(context.applicationContext as App)
                }
                return instance!!
            }
        }
    }
}

class DefaultServiceLocator(
    private val app: App
) : ServiceLocator {

    private val imagesRepo by lazy {
        DefaultImagesRepository(
            DefaultImagesSource(
                DefaultAuthHolder(
                    app.getSharedPreferences("APP", Context.MODE_PRIVATE),
                    apiInterface
                ),
                apiInterface
            )
        )
    }

    private val apiInterface by lazy {
        createApiInterface()
    }

    override fun getImagesRepository(): ImagesRepository {
        return imagesRepo
    }

    override fun getWorkerScheduler() = Schedulers.io()
    override fun getResultScheduler() = AndroidSchedulers.mainThread()

    private fun createApiInterface(): ApiInterface {
        val okClient = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://195.39.233.28:8035/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okClient)
            .build()

        return retrofit.create(ApiInterface::class.java)
    }
}
