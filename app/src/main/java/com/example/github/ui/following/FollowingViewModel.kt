package com.example.github.ui.following

import androidx.lifecycle.*
import com.example.github.data.model.searchUser.Item
import com.example.github.repository.MainRepository
import com.example.github.utils.Resource
import kotlinx.coroutines.Dispatchers
import java.lang.Exception

class FollowingViewModel constructor(private val repository: MainRepository) : ViewModel() {

    private val _listFollowing: MutableLiveData<String> = MutableLiveData()
    val listFollowing: LiveData<Resource<ArrayList<Item>>> =
        Transformations.switchMap(_listFollowing) {
            getFollowingUser(it)
        }


    private fun getFollowingUser(username: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            emit(Resource.success(repository.getUserFollowing(username)))
        } catch (e: Exception) {
            emit(Resource.error(null, e.message ?: "error Detected ${e.localizedMessage}"))

        }

    }

    fun setFollowingUser(username: String) {
        if (_listFollowing.value == username) {
            return
        }
        _listFollowing.value = username
    }
}