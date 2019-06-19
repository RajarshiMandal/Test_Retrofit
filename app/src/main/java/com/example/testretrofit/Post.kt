package com.example.testretrofit

import com.google.gson.annotations.SerializedName

data class Post(
    @SerializedName("userId")
    var userId: Int?,
    @SerializedName("id")
    var id: Int?,
    @SerializedName("title")
    var title: String?,
    @SerializedName("body")
    var body: String?
)

data class Comments(
    @SerializedName("postId")
    var postId: Int?,
    @SerializedName("id")
    var id: Int?,
    @SerializedName("name")
    var name: String?,
    @SerializedName("email")
    var email: String?,
    @SerializedName("body")
    var body: String?
)