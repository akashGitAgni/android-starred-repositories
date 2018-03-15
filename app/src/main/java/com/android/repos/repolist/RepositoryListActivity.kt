package com.android.repos.repolist

import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout

import com.android.repos.R
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import com.android.repos.dao.Repository
import com.android.repos.network.SyncResponse
import io.realm.RealmResults
import timber.log.Timber


class RepositoryListActivity : AppCompatActivity(), OnClickListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repository_list)

        val fm = supportFragmentManager
        fm.findFragmentById(R.id.fragment) ?: let {
            val fragment = RepositoryListFragment.newInstance()
            fm.beginTransaction().add(R.id.fragment, fragment).commit()
        }

    }

    override fun repositoryClicked(url: String?, title: String) {
        Timber.d("onlcick Url %s", url)
        if (url != null) {
            val fm = supportFragmentManager
            val fragment = WebViewFragment.newInstance(url, title)
            fm.beginTransaction().replace(R.id.fragment, fragment).addToBackStack(null).commit()
        } else {
            Toast.makeText(this, SyncResponse.ERROR, Toast.LENGTH_SHORT).show()
        }

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        onBackPressed()
        return true
    }

}
