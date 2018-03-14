package com.android.repos.network

import android.content.Context
import android.support.annotation.WorkerThread
import com.android.repos.checkInternet
import io.realm.Realm
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.newSingleThreadContext
import timber.log.Timber

class SyncRepositories {

    @WorkerThread
    fun updateDatabase(context: Context): String {
        if (!context.checkInternet()) {
            return SyncResponse.NOT_CONNECTED
        }
        
        try {
            val retrofit = AppNetwork.retrofit
            val api = retrofit.create(GithubTopicApi::class.java)

            for (i in 1..4) {
                val jsonData = api.getCycleDetail("topic:Android", "stars", "desc", i).execute()

                if (!jsonData.isSuccessful && jsonData.code() == 404) {
                    return SyncResponse.NOT_CONNECTED
                }

                Timber.d("syncThread Started %s", jsonData);
                jsonData?.body()?.let { body ->
                    Timber.d("body Started %s", body.totalCount);
                    Realm.getDefaultInstance()?.use { realm ->
                        realm.executeTransaction {
                            Timber.d("body Started size %s", body.items.size);
                            for (repo in body.items) {
                                realm.insertOrUpdate(repo)
                            }
                        }
                    }
                }
            }
            return SyncResponse.UPDATED
        } catch (e: Exception) {
            Timber.e(e)
        }

        return SyncResponse.ERROR
    }
}