/*
 * Copyright 2016 Realm Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.repos.repolist

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.android.repos.R
import com.android.repos.dao.Repository

import io.realm.OrderedRealmCollection
import io.realm.RealmRecyclerViewAdapter
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

internal class RepositoryRecyclerViewAdapter(data: OrderedRealmCollection<Repository>, val clickListener: OnClickListener) :
        RealmRecyclerViewAdapter<Repository, RepositoryRecyclerViewAdapter.MyViewHolder>(data, true) {

    private var inDeletionMode = false
    private val countersToDelete = HashSet<Int>()
    private lateinit var context: Context

    init {
        setHasStableIds(true)
    }

    fun enableDeletionMode(enabled: Boolean) {
        inDeletionMode = enabled
        if (!enabled) {
            countersToDelete.clear()
        }
        notifyDataSetChanged()
    }

    fun getCountersToDelete(): Set<Int> {
        return countersToDelete
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.row, parent, false)
        context = parent.context
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        getItem(position)?.apply {
            holder.data = this
            holder.title.text = name.toUpperCase()
            holder.description.text = description
            val format = context.getString(R.string.format_star_count)
            holder.starCount.text = String.format(format, position + 1, starsCount)

            val rankFormat = context.getString(R.string.format_rank)
            holder.id.text = String.format(rankFormat, id)

            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            val time = sdf.format(createdDate)
            val created = context.getString(R.string.format_created_At)
            holder.createdAt.text = String.format(created, time)

            val updateTime = sdf.format(lastPushDate)
            val updated = context.getString(R.string.format_updated_At)
            holder.updatedAt.text = String.format(updated, updateTime)

            holder.itemView.setOnClickListener({
                holder.data?.apply {
                    Timber.d("onlcick Url %s", this.htmlUrl)
                    clickListener.repositoryClicked(this.htmlUrl, this.name.toUpperCase())
                }

            })
        }
    }

    override fun getItemId(index: Int): Long {

        return getItem(index)!!.id
    }

    internal inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var title: TextView = view.findViewById(R.id.title)
        var description: TextView = view.findViewById(R.id.subtitle)

        var starCount: TextView = view.findViewById(R.id.star_count)
        var id: TextView = view.findViewById(R.id.repo_id)
        var updatedAt: TextView = view.findViewById(R.id.updated_date)
        var createdAt: TextView = view.findViewById(R.id.created_date)
        var data: Repository? = null
    }
}