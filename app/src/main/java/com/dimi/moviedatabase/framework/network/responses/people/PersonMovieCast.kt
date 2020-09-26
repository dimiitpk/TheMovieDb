package com.dimi.moviedatabase.framework.network.responses.people

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PersonMovieCast(

    @SerializedName("adult")
    @Expose
    val adult: Boolean,

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

    @SerializedName("original_title")
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

    @SerializedName("release_date")
    @Expose
    val releaseDate: String,

    @SerializedName("title")
    @Expose
    val title: String,

    @SerializedName("vote_average")
    @Expose
    val voteAverage: Float,

    @SerializedName("vote_count")
    @Expose
    val voteCount: Int
)