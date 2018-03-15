package com.android.repos

import android.app.Application
import com.android.repos.network.AppNetwork
import io.realm.Realm
import io.realm.RealmConfiguration
import timber.log.Timber

class ReposApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        Realm.init(this)
        val config = RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build()
        Realm.setDefaultConfiguration(config)

        if (BuildConfig.DEBUG) {

            // Timber debug
            Timber.plant(Timber.DebugTree())
        }
    }
}