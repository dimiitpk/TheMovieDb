package com.dimi.moviedatabase.framework.network.responses.people

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PersonTvShowCast(

    @SerializedName("backdrop_path")
    @Expose
    val backdropPath: String?,

    @SerializedName("character")
    @Expose
    val character: String,

    @SerializedName("credit_id")
    @Expose
    val creditId: String,

    @SerializedName("genre_ids")
    @Expose
    val genres: List<Int>,

    @SerializedName("id")
    @Expose
    val id: Int,

    @SerializedName("original_name")
    @Expose
    val originalTitle: String,

    @SerializedName("overview")
    @Expose
    val overview: String,

    @SerializedName("popularity")
    @Expose
    val popularity: Double,

    @SerializedName("poster_path")
    @Expose
    val posterPath: String?,

    @SerializedName("first_air_date")
    @Expose
    val firstAirDate: String,

    @SerializedName("name")
    @Expose
    val title: String,

    @SerializedName("vote_average")
    @Expose
    val voteAverage: Float,

    @SerializedName("vote_count")
    @Expose
    val voteCount: Int
)