package com.example.github.api

import com.example.github.data.model.searchUser.Item
import com.example.github.data.model.searchUser.SearchUserResponse
import com.example.github.data.model.searchUser.detailUser.DetailUserResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("search/users")
    suspend fun getSearchUser(@Query("q") query: String?): SearchUserResponse

    @GET("users/{username}")
    suspend fun getUserDetail(@Path("username") username: String): DetailUserResponse

    @GET("users/{username}/followers")
    suspend fun getFollowers(@Path("username") username: String): ArrayList<Item>

    @GET("users/{username}/following")
    suspend fun getFollowing(@Path("username") username: String): ArrayList<Item>

}