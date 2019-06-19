package com.example.testretrofit

import retrofit2.Call
import retrofit2.http.*
import java.lang.StringBuilder


interface RetrofitService {

    @GET("posts")
    fun getPosts(): Call<List<Post>>

    @GET("/posts/{id}/comments")
    fun getCommentsForPost(@Path("id") id: Int): Call<List<Comments>>

    @POST("posts")
    fun createPost(@Body post: Post): Call<Post>

    @FormUrlEncoded
    @POST("posts")
    fun createPost(@FieldMap map: Map<String, String>) : Call<Post>

    @PATCH("posts/{id}")
    fun patchPost(@Path("id") id: Int, @Body post: Post) : Call<Post>
}