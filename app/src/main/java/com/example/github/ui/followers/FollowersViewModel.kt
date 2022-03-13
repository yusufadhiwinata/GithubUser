package com.example.github.ui.followers

import androidx.lifecycle.*
import com.example.github.data.model.searchUser.Item
import com.example.github.repository.MainRepository
import com.example.github.utils.Resource
import kotlinx.coroutines.Dispatchers

class FollowersViewModel constructor(private val repository: MainRepository) : ViewModel() {

    private val _listFollowers: MutableLiveData<String> = MutableLiveData()
    val listFollowers: LiveData<Resource<ArrayList<Item>>> =
        Transformations.switchMap(_listFollowers) {
            getFollowersUsers(it)
        }

    private fun getFollowersUsers(username: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            emit(Resource.success(repository.getUserFollower(username)))
        } catch (e: Exception) {
            emit(Resource.error(null, e.message ?: "error Detected ${e.localizedMessage}"))

        }

    }

    fun setFollowersUser(username: String) {
        if (_listFollowers.value == username) {
            return
        }
        _listFollowers.value = username
    }


}