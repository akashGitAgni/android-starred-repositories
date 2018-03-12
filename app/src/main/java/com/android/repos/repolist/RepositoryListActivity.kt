package com.android.repos.repolist

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import com.android.repos.R
import com.android.repos.network.DataUpdateService
import io.realm.Realm
import android.support.v7.widget.helper.ItemTouchHelper
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.android.repos.dao.Repository
import io.realm.RealmChangeListener
import timber.log.Timber


class RepositoryListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repository_list)

        val intent = Intent(this, DataUpdateService::class.java)
        startService(intent)

        val androidViewModel = ViewModelProviders.of(this).get(ReposActivityViewModel::class
                .java)

        recyclerView = findViewById(R.id.recyler_view)
        realm = Realm.getDefaultInstance()
        setUpRecyclerView()
    }


    private lateinit var recyclerView: RecyclerView
    private lateinit var realm: Realm

    private fun setUpRecyclerView() {

        val list = realm.where(Repository::class.java).findAllAsync()

        list?.addChangeListener(RealmChangeListener {
            Timber.d(" Listener getting fired activity here Size of list %s", it.size);
            val adapter = MyRecyclerViewAdapter(it)
            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.adapter = adapter
            recyclerView.setHasFixedSize(true)
        })
    }
}
