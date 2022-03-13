package com.example.github.ui.main

import androidx.lifecycle.*
import com.example.github.data.model.searchUser.Item
import com.example.github.repository.MainRepository
import com.example.github.utils.Resource
import kotlinx.coroutines.Dispatchers


class MainViewModel constructor(private val repository: MainRepository) : ViewModel() {

    private val _listResponse: MutableLiveData<String> = MutableLiveData()
    val listResponse: LiveData<Resource<ArrayList<Item>>> =
        Transformations.switchMap(_listResponse) {
            listUser(it)
        }


    private fun listUser(query: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            val userSearch = repository.getSearchUser(query)
            emit(Resource.success(userSearch.items))

        } catch (e: Exception) {
            emit(Resource.error(null, e.message + "error"))
        }


    }

    fun setSearch(query: String) {
        if (_listResponse.value == query) {
            return
        }
        _listResponse.value = query
    }
}

