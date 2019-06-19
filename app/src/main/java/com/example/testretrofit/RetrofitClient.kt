package com.example.testretrofit

import android.content.Context
import android.util.Log
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private var retrofit: Retrofit? = null
    private const val BASE_URL = "https://jsonplaceholder.typicode.com/"
    private val TAG = RetrofitClient::class.java.simpleName

    fun retrofit(context: Context): Retrofit {
        return retrofit ?: Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient(context.applicationContext))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .also { retrofit ->
                this.retrofit = retrofit
            }
    }

    private fun okHttpClient(context: Context): OkHttpClient {
        return OkHttpClient.Builder()
            .cache(cache(context))
            .addInterceptor(
                HttpLoggingInterceptor {
                    Log.d(TAG, "okHttpClient: logging = $it")
                }.apply {
                    level = HttpLoggingInterceptor.Level.HEADERS
                }
            ) // used if network off OR on
            .addNetworkInterceptor (networkInterceptor()) // only used when network is on
            .addInterceptor(offlineInterceptor())
            .build()
    }

    private fun cache(context: Context): Cache {
        return Cache(File(context.cacheDir, "someIdentifier"), cacheSize)
    }

    /**
     * This interceptor will be called both if the network is available and if the network is not available
     * @return
     */
    private fun offlineInterceptor() = Interceptor { chain ->
        Log.d(TAG, "offline interceptor: called.")
        var request = chain.request()

        // prevent caching when network is on. For that we use the "networkInterceptor"
        if (!MyApp.isConnected) {
            val cacheControl = CacheControl.Builder()
                .maxStale(7, TimeUnit.DAYS)
                .build()

            request = request.newBuilder()
                .removeHeader(HEADER_PRAGMA)
                .removeHeader(HEADER_CACHE_CONTROL)
                .cacheControl(cacheControl)
                .build()
        }
        chain.proceed(request)
    }

    /**
     * This interceptor will be called ONLY if the network is available
     * @return
     */
    private fun networkInterceptor() = Interceptor { chain ->

        Log.d(TAG, "network interceptor: called.")

        val response = chain.proceed(chain.request())

        val cacheControl = CacheControl.Builder()
            .maxAge(5, TimeUnit.SECONDS)
            .build()


        response.newBuilder()
            .removeHeader(HEADER_PRAGMA)
            .removeHeader(HEADER_CACHE_CONTROL)
            .header(HEADER_CACHE_CONTROL, cacheControl.toString())
            .build()
    }
}

private const val cacheSize = (5 * 1024 * 1024).toLong() // 5 MB
private const val HEADER_CACHE_CONTROL = "Cache-Control"
private const val HEADER_PRAGMA = "Pragma"