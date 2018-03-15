package com.android.repos.repolist

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import android.widget.Toast

import com.android.repos.R
import com.android.repos.dao.Repository
import com.android.repos.network.SyncResponse
import io.realm.RealmResults

class RepositoryListFragment : Fragment() {

    private lateinit var androidViewModel: ReposActivityViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var swipeToRefresh: SwipeRefreshLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val v = inflater.inflate(R.layout.fragment_recycler_view, container, false)

        activity?.let {
            (it as AppCompatActivity).supportActionBar?.apply {
                setDisplayShowHomeEnabled(false)
                setDisplayHomeAsUpEnabled(false)
                this.title = getString(R.string.app_name)
                show()
            }
        }

        progressBar = v.findViewById(R.id.progress_bar)
        swipeToRefresh = v.findViewById(R.id.swipe_layout)

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

        recyclerView = v.findViewById(R.id.recyler_view)
        setUpRecyclerView(androidViewModel.reposList)

        swipeToRefresh.setOnRefreshListener {
            androidViewModel.performFullSync()
        }

        androidViewModel.syncResponse.observe(this, android.arch.lifecycle.Observer {
            if (it != null && !it.equals(SyncResponse.IGNORE)) {
                swipeToRefresh.isRefreshing = false
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                androidViewModel.syncResponse.value = SyncResponse.IGNORE
            }
        })
        return v
    }

    private fun setUpRecyclerView(results: RealmResults<Repository>?) {
        if (results == null) {
            return
        }

        val adapter = RepositoryRecyclerViewAdapter(results, activity as RepositoryListActivity)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = adapter
        recyclerView.setHasFixedSize(true)
    }

    companion object {

        fun newInstance(): RepositoryListFragment? {
            return RepositoryListFragment()
        }
    }
}