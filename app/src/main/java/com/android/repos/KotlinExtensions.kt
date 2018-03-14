package com.android.repos

import android.content.Context
import android.net.ConnectivityManager

fun Context.checkInternet(): Boolean {
    val manager = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    // I've "kotlinized" a bit your `if` statement
    return manager.activeNetworkInfo?.isConnectedOrConnecting ?: false
}