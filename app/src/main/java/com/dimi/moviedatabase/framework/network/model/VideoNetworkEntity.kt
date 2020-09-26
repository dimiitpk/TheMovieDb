package com.dimi.moviedatabase.framework.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class VideoNetworkEntity(

    @SerializedName("id")
    @Expose
    var id: String,

    @SerializedName("key")
    @Expose
    var key: String,

    @SerializedName("name")
    @Expose
    var name: String,

    @SerializedName("site")
    @Expose
    var site: String,

    @SerializedName("type")
    @Expose
    var type: String
)
