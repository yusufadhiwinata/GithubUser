package com.example.github.api


class ApiHelper(private val apiService: ApiService) {
    suspend fun getSearchUser(query: String) = apiService.getSearchUser(query)
    suspend fun getUserDetail(username: String) = apiService.getUserDetail(username)
    suspend fun getUserFollowers(username: String) = apiService.getFollowers(username)
    suspend fun getUserFollowing(username: String) = apiService.getFollowing(username)
}