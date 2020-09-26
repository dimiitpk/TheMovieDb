package com.dimi.moviedatabase.framework.network.responses.tv_show

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class EpisodeResponse(

    @SerializedName("id")
    @Expose
    var id: Int,

    @SerializedName("show_id")
    @Expose
    var tvShowId: Int,

    @SerializedName("season_number")
    @Expose
    var seasonNumber: Int,

    @SerializedName("episode_number")
    @Expose
    var episodeNumber: Int,

    @SerializedName("air_date")
    @Expose
    var airDate: String?,

    @SerializedName("overview")
    @Expose
    var overview: String,

    @SerializedName("name")
    @Expose
    var name: String,

    @SerializedName("vote_average")
    @Expose
    var voteAverage: Float,

    @SerializedName("vote_count")
    @Expose
    var voteCount: Int,

    @SerializedName("still_path")
    @Expose
    var stillPath: String? = null
)