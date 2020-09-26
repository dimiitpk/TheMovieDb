package com.dimi.moviedatabase.framework.network.responses.common

import com.dimi.moviedatabase.framework.network.model.VideoNetworkEntity
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class VideoListResponse(

    @SerializedName("id")
    @Expose
    var id : Int,

    @SerializedName("results")
    @Expose
    var results: List<VideoNetworkEntity>
)