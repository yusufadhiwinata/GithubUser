package com.example.github.repository

import com.example.github.api.ApiHelper

class MainRepository constructor(private val apiHelper: ApiHelper) {
    suspend fun getSearchUser(query: String) = apiHelper.getSearchUser(query)
    suspend fun getUserDetail(username: String) = apiHelper.getUserDetail(username)
    suspend fun getUserFollower(username: String) = apiHelper.getUserFollowers(username)
    suspend fun getUserFollowing(username: String) = apiHelper.getUserFollowing(username)

}