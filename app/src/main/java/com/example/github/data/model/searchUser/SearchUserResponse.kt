package com.example.github.data.model.searchUser

data class SearchUserResponse(
    val incomplete_results: Boolean,
    val items: ArrayList<Item>,
    val total_count: Int
)