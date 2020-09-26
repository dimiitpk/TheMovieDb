package com.dimi.moviedatabase.framework.network.responses.tv_show

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class NetworkResponse(

    @SerializedName("id")
    @Expose
    val id: Long,

    @SerializedName("logo_path")
    @Expose
    val logoPath: String,

    @SerializedName("name")
    @Expose
    val name: String,

    @SerializedName("origin_country")
    @Expose
    val originCountry: String
)