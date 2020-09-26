package com.dimi.moviedatabase.framework.network.responses.tv_show

import com.dimi.moviedatabase.framework.network.model.TvShowCastNetworkEntity
import com.dimi.moviedatabase.framework.network.responses.common.CreditsResponse
import com.dimi.moviedatabase.framework.network.responses.common.GenreResponse
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TvShowDetailsResponse(

    @SerializedName("id")
    @Expose
    var id: Long,

    @SerializedName("name")
    @Expose
    var title: String,

    @SerializedName("popularity")
    @Expose
    var popularity: Double,

    @SerializedName("episode_run_time")
    @Expose
    val runtime: List<Int>,

    @SerializedName("homepage")
    @Expose
    val homepage: String,

    @SerializedName("status")
    @Expose
    val status: String,

    @SerializedName("type")
    @Expose
    val type: String,

    @SerializedName("original_name")
    @Expose
    val originalName: String,

    @SerializedName("seasons")
    @Expose
    val seasons: List<SeasonResponse>,

    @SerializedName("networks")
    @Expose
    val networks: List<NetworkResponse>,

    @SerializedName("vote_count")
    @Expose
    var voteCount: Int,

    @SerializedName("number_of_episodes")
    @Expose
    var numberOfEpisodes: Int,

    @SerializedName("number_of_seasons")
    @Expose
    var numberOfSeasons: Int,

    @SerializedName("first_air_date")
    @Expose
    var firstAirDate: String,

    @SerializedName("last_air_date")
    @Expose
    val lastAirDate: String,

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

    @SerializedName("genres")
    @Expose
    var genres: List<GenreResponse>,

    @SerializedName("credits")
    @Expose
    var creditsResponse: CreditsResponse<TvShowCastNetworkEntity>

//
//    @SerializedName("seasons")
//    @Expose
//    var seasonResponse: List<TVSeasonResponse>
)