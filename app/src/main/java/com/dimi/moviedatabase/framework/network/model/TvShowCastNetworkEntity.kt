package com.dimi.moviedatabase.framework.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TvShowCastNetworkEntity(

    @SerializedName("credit_id")
    @Expose
    var id: String,

    @SerializedName("character")
    @Expose
    var character: String,

    @SerializedName("id")
    @Expose
    var actorId: Long,

    @SerializedName("name")
    @Expose
    var name: String,

    @SerializedName("order")
    @Expose
    var priority: Int,

    @SerializedName("profile_path")
    @Expose
    var profile_path: String? = null
)