package com.example.github.ui.detail


import androidx.lifecycle.*
import com.example.github.data.model.searchUser.detailUser.DetailUserResponse
import com.example.github.repository.MainRepository
import com.example.github.utils.Resource
import kotlinx.coroutines.Dispatchers

class DetailViewModel(private val repository: MainRepository) : ViewModel() {

    private val _detailResponse: MutableLiveData<String> = MutableLiveData()
    val detailResponse: LiveData<Resource<DetailUserResponse>> =
        Transformations.switchMap(_detailResponse) {
            getUserDetail(it)
        }

    private fun getUserDetail(username: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            emit(Resource.success(repository.getUserDetail(username)))

        } catch (e: Exception) {
            emit(Resource.error(null, e.message ?: "error Detected ${e.localizedMessage}"))
        }

    }

    fun setUserDetail(username: String){
        if (_detailResponse.value == username){
            return
        }
        _detailResponse.value = username
    }

}