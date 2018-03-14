package com.android.repos.repolist

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.android.repos.dao.ReposDao
import com.android.repos.dao.Repository
import com.android.repos.network.SyncRepositories
import io.realm.Realm
import io.realm.RealmChangeListener
import io.realm.RealmResults
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.newSingleThreadContext
import timber.log.Timber

class ReposActivityViewModel(application: Application) : AndroidViewModel(application) {

//    var workoutLiveData: MutableLiveData<WorkoutViewModel> = MutableLiveData()

    val realm: Realm = Realm.getDefaultInstance()
    val reposDao: ReposDao = ReposDao(realm)

    val singleThread = newSingleThreadContext("Sync")

    var reposList: RealmResults<Repository>?

    var reposLoaded: MutableLiveData<Boolean> = MutableLiveData()
    var syncResponse: MutableLiveData<String> = MutableLiveData()

    private var syncJob: Job? = null

    init {

        reposList = reposDao.getAllRepos();
        Timber.d("repoList Size of list in viewmodel %s", reposList?.size);
        reposList?.addChangeListener(RealmChangeListener {
            Timber.d("repoList Size of list in viewmodel %s", it.size);
            if (it.size > 0) {
                reposLoaded.value = true
            } else {
                reposLoaded.value = false
                performFullSync()
            }
        })
    }

    fun performFullSync() {
        //If Sync is already in progress then don't sync again
        if (syncJob?.isActive == true) {
            return;
        }

        syncJob = launch(singleThread) {
            syncResponse.postValue(SyncRepositories().updateDatabase(getApplication()))
        }
    }

    override fun onCleared() {
        reposList?.removeAllChangeListeners()
        syncJob?.cancel()
        singleThread.close()
        super.onCleared()
    }
}