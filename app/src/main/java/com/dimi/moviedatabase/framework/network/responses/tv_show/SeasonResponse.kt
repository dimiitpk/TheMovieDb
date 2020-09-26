package com.dimi.moviedatabase.framework.network.responses.tv_show

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SeasonResponse(

    @SerializedName("air_date")
    @Expose
    val airDate: String,

    @SerializedName("episode_count")
    @Expose
    val episodeCount: Int,

    @SerializedName("id")
    @Expose
    val id: Int,

    @SerializedName("name")
    @Expose
    val name: String,

    @SerializedName("overview")
    @Expose
    val overview: String,

    @SerializedName("poster_path")
    @Expose
    val posterPath: String,

    @SerializedName("season_number")
    @Expose
    val seasonNumber: Int,

    @SerializedName("episodes")
    @Expose
    val episodes: List<EpisodeResponse>,

)