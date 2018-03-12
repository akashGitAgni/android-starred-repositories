package com.android.repos.repolist

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import com.android.repos.dao.ReposDao
import com.android.repos.dao.Repository
import io.realm.Realm
import io.realm.RealmChangeListener
import io.realm.RealmResults
import timber.log.Timber

class ReposActivityViewModel(application: Application) : AndroidViewModel(application) {

//    var workoutLiveData: MutableLiveData<WorkoutViewModel> = MutableLiveData()

    val realm: Realm = Realm.getDefaultInstance()
    val reposDao: ReposDao = ReposDao(realm)

    val reposList: RealmResults<Repository>?

    init {

        reposList = reposDao.getAllRepos();
        Timber.d("Size of list in viewmodel %s", reposList?.size);
        reposList?.addChangeListener(RealmChangeListener {
            Timber.d(" Listener getting fired here Size of list %s", it.size);
        })
    }

    override fun onCleared() {
        reposList?.removeAllChangeListeners()
        super.onCleared()
    }
}