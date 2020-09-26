package com.dimi.moviedatabase.framework.network.responses.common

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ImagesResponse(
    @SerializedName("id")
    @Expose
    var id: Int,

    @SerializedName("backdrops")
    @Expose
    var backdrops: List<ImageResponse>,

    @SerializedName("posters")
    @Expose
    var posters: List<ImageResponse>
)