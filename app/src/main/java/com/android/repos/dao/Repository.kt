package com.android.repos.dao

import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

open class Repository : RealmObject() {

    @PrimaryKey
    var id: Long = 0

    var name: String = ""
    @SerializedName("full_name")
    var fullName: String? = ""

    @SerializedName("html_url")
    var htmlUrl: String? = ""

    var description: String? = ""

    @SerializedName("created_at")
    var createdDate: Date? = null

    @SerializedName("updated_at")
    var updatedDate: Date? = null

    @SerializedName("stargazers_count")
    var starsCount: Long = 0

    @SerializedName("pushed_at")
    var lastPushDate: Date? = null

}