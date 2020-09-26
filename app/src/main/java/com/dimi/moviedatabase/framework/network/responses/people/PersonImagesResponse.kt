package com.dimi.moviedatabase.framework.network.responses.people

import com.dimi.moviedatabase.framework.network.responses.common.ImageResponse
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PersonImagesResponse(
    @SerializedName("id")
    @Expose
    val id: Int,
    @SerializedName("profiles")
    @Expose
    val profiles: List<ImageResponse>
)