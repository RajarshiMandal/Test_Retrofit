package com.example.testretrofit

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.CacheControl
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


private val TAG = MainActivity::class.java.simpleName

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val retrofit = RetrofitClient.retrofit(this)
        val retrofitService = retrofit.create(RetrofitService::class.java)
//
        val callAllPosts = retrofitService.getPosts()
        callAllPosts.request().newBuilder()
            .cacheControl(CacheControl.FORCE_CACHE)
        getAllPosts(callAllPosts)

//        val callCommentsByPost = retrofitService.getCommentsForPost(1)
//
//        val post = Post(1, null, "hello world", "a long body")
//        val createPost = retrofitService.createPost(post)
//
//        val map = hashMapOf<String, String>()
//        map["userId"] = "25"
//        map["title"] = "another hello world"
//        val createMapPost = retrofitService.createPost(map)
//
//        val postToUpdate = Post(null, null, "hello world", null)
//        val patchPostCall = retrofitService.patchPost(1, postToUpdate)


//        getCommentsByPosts(callCommentsByPost)
//        createAPost(createPost)
//        createAMapPost(createMapPost)
//        patchPost(patchPostCall)
    }

    private fun patchPost(patchPostCall: Call<Post>) {
        patchPostCall.enqueue(MyCallback())
    }

    private fun createAMapPost(createMapPost: Call<Post>) {
        createMapPost.enqueue(MyCallback())
    }

    private fun createAPost(retrofitService: Call<Post>) {
        retrofitService.enqueue(MyCallback())
    }

    private fun getCommentsByPosts(callCommentsByPost: Call<List<Comments>>) {
        callCommentsByPost.enqueue(MyCallback())
    }

    private fun getAllPosts(call: Call<List<Post>>) {
        call.enqueue(MyCallback())
    }

    inner class MyCallback<T> : Callback<T> {
        override fun onFailure(call: Call<T>, t: Throwable) {
            Log.d(TAG, "onFailure: ${t.printStackTrace()}")
        }

        override fun onResponse(call: Call<T>, response: Response<T>) {
            val rawResponse = response.raw()

            if (rawResponse.networkResponse() != null) {
                Log.d(TAG, "onResponse: is from network")
            } else if (rawResponse.cacheResponse() != null) {
                Log.d(TAG, "onResponse: is from cache")
            }
            if (response.isSuccessful) {
                text_view.text = response.body()?.toString()
            } else {
                text_view.text = response.code().toString()
            }
        }
    }

}
