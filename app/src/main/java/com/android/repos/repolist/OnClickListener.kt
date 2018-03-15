package com.android.repos.repolist

interface OnClickListener {
    fun repositoryClicked(url: String?, title: String)
}