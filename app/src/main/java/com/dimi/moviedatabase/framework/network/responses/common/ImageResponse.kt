package com.dimi.moviedatabase.framework.network.responses.common

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ImageResponse(

    @SerializedName("aspect_ratio")
    @Expose
    var aspectRatio: Float,

    @SerializedName("file_path")
    @Expose
    var filePath: String,

    @SerializedName("height")
    @Expose
    var height: Int

)