package com.android.repos

import com.android.repos.network.AppNetwork
import com.android.repos.network.GithubTopicApi
import org.junit.Test

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull

class NetworkTest {
    @Test
    fun checkNetworkCallResponse() {
        val retrofit = AppNetwork.retrofit

        val api = retrofit.create(GithubTopicApi::class.java)

        val json = api.getCycleDetail("topic:Android", "stars", "desc", 2).execute()
        assertNotNull(json)
        val body = json.body()
        assertNotNull(body)
        println(body?.totalCount)
        assert(body?.totalCount == body?.items?.size)
    }
}