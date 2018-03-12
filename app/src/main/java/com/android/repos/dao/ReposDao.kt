package com.android.repos.dao

import io.realm.Realm
import io.realm.RealmList
import io.realm.RealmResults
import timber.log.Timber

public class ReposDao(val realm: Realm) {

    fun getAllRepos(): RealmResults<Repository>? {

        try {
            return realm.where(Repository::class.java).findAllAsync()
        } catch (e: IllegalArgumentException) {
            Timber.e(e, "Error getting all cycle from Realm")
        }

        return null

    }
}