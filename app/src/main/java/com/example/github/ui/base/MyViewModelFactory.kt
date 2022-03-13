package com.example.github.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.github.api.ApiHelper
import com.example.github.repository.MainRepository
import com.example.github.ui.detail.DetailViewModel
import com.example.github.ui.followers.FollowersViewModel
import com.example.github.ui.following.FollowingViewModel
import com.example.github.ui.main.MainViewModel

@Suppress("UNCHECKED_CAST")
class MyViewModelFactory constructor(private val apiHelper: ApiHelper) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(MainRepository(apiHelper)) as T
            }
            modelClass.isAssignableFrom(DetailViewModel::class.java) -> {
                DetailViewModel(MainRepository(apiHelper)) as T
            }
            modelClass.isAssignableFrom(FollowersViewModel::class.java) -> {
                FollowersViewModel(MainRepository(apiHelper)) as T
            }
            modelClass.isAssignableFrom(FollowingViewModel::class.java) -> {
                FollowingViewModel(MainRepository(apiHelper)) as T
            }

            else -> throw Throwable("unknown ViewModel class:" + modelClass.name)
        }
    }
}