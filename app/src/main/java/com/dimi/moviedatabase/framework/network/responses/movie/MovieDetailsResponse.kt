package com.dimi.moviedatabase.framework.network.responses.movie

import com.dimi.moviedatabase.framework.network.model.MovieCastNetworkEntity
import com.dimi.moviedatabase.framework.network.responses.common.CreditsResponse
import com.dimi.moviedatabase.framework.network.responses.common.GenreResponse
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class MovieDetailsResponse(

    @SerializedName("id")
    @Expose
    var id: Long,

    @SerializedName("title")
    @Expose
    var title: String,

    @SerializedName("budget")
    @Expose
    val budget: Int,

    @SerializedName("revenue")
    @Expose
    val revenue: Long,

    @SerializedName("runtime")
    @Expose
    val runtime: Int,

    @SerializedName("status")
    @Expose
    val status: String,

    @SerializedName("original_title")
    @Expose
    val originalTitle: String,

    @SerializedName("homepage")
    @Expose
    val homepage: String,

    @SerializedName("imdb_id")
    @Expose
    val imdbId: String,

    @SerializedName("popularity")
    @Expose
    var popularity: Double,

    @SerializedName("vote_count")
    @Expose
    var voteCount: Int,

    @SerializedName("release_date")
    @Expose
    var releaseDate: String? = null,

    @SerializedName("vote_average")
    @Expose
    var voteAverage: Float,

    @SerializedName("overview")
    @Expose
    var overview: String,

    @SerializedName("tagline")
    @Expose
    var tagLine: String?,

    @SerializedName("poster_path")
    @Expose
    var posterPath: String?,

    @SerializedName("backdrop_path")
    @Expose
    var backdropPath: String?,

    @SerializedName("genres")
    @Expose
    var genres: List<GenreResponse>,

    @SerializedName("credits")
    @Expose
    var creditsResponse: CreditsResponse<MovieCastNetworkEntity>
)