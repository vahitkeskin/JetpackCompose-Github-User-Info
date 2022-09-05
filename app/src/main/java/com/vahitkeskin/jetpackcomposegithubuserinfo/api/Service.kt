package com.vahitkeskin.jetpackcomposegithubuserinfo.api

import com.facebook.flipper.plugins.network.FlipperOkhttpInterceptor
import com.vahitkeskin.jetpackcomposegithubuserinfo.BuildConfig
import com.vahitkeskin.jetpackcomposegithubuserinfo.utils.Constants.Companion.BASE_URL
import com.vahitkeskin.jetpackcomposegithubuserinfo.utils.FlipperNetworkObject.networkFlipperPlugin
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Service {

    fun getUsers(): GithubAPI {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client().build())
            .build()
        return retrofit.create(GithubAPI::class.java)
    }

    private fun client(): OkHttpClient.Builder {
        return if (BuildConfig.DEBUG && networkFlipperPlugin != null) {
            OkHttpClient.Builder()
                .addNetworkInterceptor(FlipperOkhttpInterceptor(networkFlipperPlugin))
        } else {
            OkHttpClient.Builder()
        }
    }

}