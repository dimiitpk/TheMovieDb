package com.dimi.moviedatabase.framework.network.responses.tv_show

import com.dimi.moviedatabase.framework.network.model.TvShowNetworkEntity
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TvShowListResponse(

    @SerializedName("page")
    @Expose
    var page: Int,

    @SerializedName("total_results")
    @Expose
    var totalResults: Int,

    @SerializedName("total_pages")
    @Expose
    var totalPages: Int,

    @SerializedName("results")
    @Expose
    var results: List<TvShowNetworkEntity>
)