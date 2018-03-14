package com.android.repos.repolist

import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout

import com.android.repos.R
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import com.android.repos.dao.Repository
import com.android.repos.network.SyncResponse
import io.realm.RealmResults


class RepositoryListActivity : AppCompatActivity(), OnClickListener {


    private lateinit var androidViewModel: ReposActivityViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var swipeToRefresh: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repository_list)

        progressBar = findViewById(R.id.progress_bar)
        swipeToRefresh = findViewById(R.id.swipe_layout)

        androidViewModel = ViewModelProviders.of(this).get(ReposActivityViewModel::class
                .java)

        androidViewModel.reposLoaded.observe(this, android.arch.lifecycle.Observer {

            if (it != null) {
                if (it) {
                    progressBar.visibility = View.GONE
                } else {
                    progressBar.visibility = View.VISIBLE
                }

            }

        })

        recyclerView = findViewById(R.id.recyler_view)
        setUpRecyclerView(androidViewModel.reposList)

        swipeToRefresh.setOnRefreshListener {
            androidViewModel.performFullSync()
        }

        androidViewModel.syncResponse.observe(this, android.arch.lifecycle.Observer {
            if (it != null && !it.equals(SyncResponse.IGNORE)) {
                swipeToRefresh.isRefreshing = false
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                androidViewModel.syncResponse.value = SyncResponse.IGNORE
            }
        })
    }


    private fun setUpRecyclerView(results: RealmResults<Repository>?) {
        if (results == null) {
            return
        }

        val adapter = RepositoryRecyclerViewAdapter(results, this)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        recyclerView.setHasFixedSize(true)
    }

    override fun repositoryClicked(url: String) {
        this.findViewById<View>(R.id.fragment_webview).visibility = View.VISIBLE
        val fm = supportFragmentManager
        val fragment = WebViewFragment.newInstance()
        fm.beginTransaction().replace(R.id.fragment_webview, fragment).addToBackStack(null).commit()
    }

}
