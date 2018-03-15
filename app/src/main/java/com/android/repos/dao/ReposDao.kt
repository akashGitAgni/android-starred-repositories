package com.android.repos.dao

import io.realm.Realm
import io.realm.RealmList
import io.realm.RealmResults
import io.realm.Sort
import timber.log.Timber

public class ReposDao(val realm: Realm) {

    fun getAllRepos(): RealmResults<Repository>? {

        try {
            return realm.where(Repository::class.java).sort("starsCount", Sort.DESCENDING).findAllAsync()
        } catch (e: IllegalArgumentException) {
            Timber.e(e, "Error getting all cycle from Realm")
        }

        return null

    }

    fun getAllReposSynchronous(): RealmResults<Repository>? {

        try {
            return realm.where(Repository::class.java).sort("starsCount", Sort.DESCENDING).findAllAsync()
        } catch (e: IllegalArgumentException) {
            Timber.e(e, "Error getting all cycle from Realm")
        }

        return null

    }
}