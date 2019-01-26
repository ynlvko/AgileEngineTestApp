package com.agileengine.ynlvko_test.images

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*

class ImagesViewModel : ViewModel() {
    private val data = MutableLiveData<List<Image>>()
    private val progress = MutableLiveData<Boolean>()

    fun data(): LiveData<List<Image>> = data
    fun progress(): LiveData<Boolean> = progress

    init {
        data.value = listOf()
        progress.value = true
    }

    companion object {
        fun getInstance(activity: FragmentActivity): ImagesViewModel {
            return ViewModelProviders.of(activity, object : ViewModelProvider.Factory {
                override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                    return ImagesViewModel() as T
                }
            })[ImagesViewModel::class.java]
        }
    }
}
