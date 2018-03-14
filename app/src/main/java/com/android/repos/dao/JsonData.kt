package com.android.repos.dao

import com.google.gson.annotations.SerializedName
import io.realm.RealmList

class JsonData {

    var items: List<Repository> = ArrayList<Repository>();

    @SerializedName("total_count")
    var totalCount: Int = 0
}