package com.vahitkeskin.jetpackcomposegithubuserinfo.api

import com.vahitkeskin.jetpackcomposegithubuserinfo.utils.Constants.Companion.GITHUB_TOKEN
import com.vahitkeskin.jetpackcomposegithubuserinfo.model.ReposItem
import com.vahitkeskin.jetpackcomposegithubuserinfo.model.Users
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface GithubAPI {

    //https://stackoverflow.com/a/33668077/18201304
    //USE GITHUB TOKEN: https://stackoverflow.com/a/64722853/18201304

    @GET("users/{user}")
    fun userInfo(
        @Path("user") user: String?,
        @Header("Authorization") accessToken: String? = GITHUB_TOKEN
    ): Call<Users>

    @GET("users/{user}/repos")
    fun userRepos(
        @Path("user") user: String?,
        @Header("Authorization") accessToken: String? = GITHUB_TOKEN
    ): Call<List<ReposItem>>
}