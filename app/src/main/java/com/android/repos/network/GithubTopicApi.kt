package com.android.repos.network

import com.android.repos.dao.JsonData
import com.android.repos.dao.Repository
import com.google.gson.JsonObject
import io.realm.RealmList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubTopicApi {

    @Headers(
            "Accept: application/vnd.github.mercy-preview+json"
    )
    @GET("repositories")
    fun getCycleDetail(@Query("q") topic: String,
                       @Query("sort") sort: String,
                       @Query("order") order: String,
                       @Query("page") page: Int): Call<JsonData>

}

