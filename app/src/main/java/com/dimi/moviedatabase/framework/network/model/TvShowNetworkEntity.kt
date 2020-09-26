package com.dimi.moviedatabase.framework.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TvShowNetworkEntity(

    @SerializedName("id")
    @Expose
    var id: Int,

    @SerializedName("name")
    @Expose
    var title: String,

    @SerializedName("popularity")
    @Expose
    var popularity: Double,

    @SerializedName("vote_count")
    @Expose
    var voteCount: Int,

    @SerializedName("first_air_date")
    @Expose
    var firstAirDate: String? = null,

    @SerializedName("vote_average")
    @Expose
    var voteAverage: Float,

    @SerializedName("overview")
    @Expose
    var overview: String,

    @SerializedName("poster_path")
    @Expose
    var posterPath: String?,

    @SerializedName("backdrop_path")
    @Expose
    var backdropPath: String?,

    @SerializedName("genre_ids")
    @Expose
    var genres: List<Int>? = null

)