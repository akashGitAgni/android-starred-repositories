package com.android.repos.network

import android.app.IntentService
import android.content.Context
import android.content.Intent
import android.support.v4.app.JobIntentService
import io.realm.Realm
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.newSingleThreadContext
import timber.log.Timber

class DataUpdateService : JobIntentService() {

    override fun onHandleWork(intent: Intent) {
        updateDatabase()
    }

    fun enqueueWork(context: Context, work: Intent) {
        enqueueWork(context, DataUpdateService::class.java, 1001, work)
    }

    fun updateDatabase() {
        newSingleThreadContext("SyncThread").use { syncThread ->

            Timber.d("newSingleThreadContext Started ");

            launch(syncThread) {
                try {

                    Timber.d("syncThread Started ");
                    val retrofit = AppNetwork.retrofit
                    val api = retrofit.create(GithubTopicApi::class.java)

                    val jsonData = api.getCycleDetail("topic:Android", "stars", "desc",1).execute()

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
                } catch (e: Exception) {
                    Timber.e(e)
                }
            }
        }
    }
}