package com.dimi.moviedatabase.presentation.main.home.state

import android.os.Parcelable
import com.dimi.moviedatabase.business.domain.model.Movie
import com.dimi.moviedatabase.business.domain.model.TvShow
import com.dimi.moviedatabase.business.domain.state.MediaType
import com.dimi.moviedatabase.business.domain.state.ViewState
import com.dimi.moviedatabase.presentation.main.search.enums.MediaListType
import kotlinx.android.parcel.Parcelize

@Parcelize
data class HomeViewState(

    var popularMovies: List<Movie>? = null,
    var topRatedMovies: List<Movie>? = null,
    var upcomingMovies: List<Movie>? = null,
    var popularTvShows: List<TvShow>? = null,
    var topRatedTvShows: List<TvShow>? = null,
    var onTheAirTvShows: List<TvShow>? = null,
    var trendingMovies: List<Movie>? = null,
    var trendingTvShows: List<TvShow>? = null

) : Parcelable, ViewState {

    override fun setData(vararg hashMap: HashMap<String, Any>) {
        for( data in hashMap ) {

            (data[MediaListType.POPULAR.name.plus(MediaType.MOVIE.name)] as List<*>?)?.filterIsInstance<Movie>().let { list ->
                if (list != null) {
                    if (list.isNotEmpty())
                        popularMovies = list
                }
            }
            (data[MediaListType.TOP_RATED.name.plus(MediaType.MOVIE.name)] as List<*>?)?.filterIsInstance<Movie>().let { list ->
                if (list != null) {
                    if (list.isNotEmpty())
                        topRatedMovies = list
                }
            }
            (data[MediaListType.UPCOMING_OR_ON_THE_AIR.name.plus(MediaType.MOVIE.name)] as List<*>?)?.filterIsInstance<Movie>().let { list ->
                if (list != null) {
                    if (list.isNotEmpty())
                        upcomingMovies = list
                }
            }
            (data[MediaListType.POPULAR.name.plus(MediaType.TV_SHOW.name)] as List<*>?)?.filterIsInstance<TvShow>().let { list ->
                if (list != null) {
                    if (list.isNotEmpty())
                        popularTvShows = list
                }
            }
            (data[MediaListType.TOP_RATED.name.plus(MediaType.TV_SHOW.name)] as List<*>?)?.filterIsInstance<TvShow>().let { list ->
                if (list != null) {
                    if (list.isNotEmpty())
                        topRatedTvShows = list
                }
            }
            (data[MediaListType.UPCOMING_OR_ON_THE_AIR.name.plus(MediaType.TV_SHOW.name)] as List<*>?)?.filterIsInstance<TvShow>().let { list ->
                if (list != null) {
                    if (list.isNotEmpty())
                        onTheAirTvShows = list
                }
            }
            (data[MediaListType.TRENDING.name.plus(MediaType.MOVIE.name)] as List<*>?)?.filterIsInstance<Movie>().let { list ->
                if (list != null) {
                    if (list.isNotEmpty())
                        trendingMovies = list
                }
            }
            (data[MediaListType.TRENDING.name.plus(MediaType.TV_SHOW.name)] as List<*>?)?.filterIsInstance<TvShow>().let { list ->
                if (list != null) {
                    if (list.isNotEmpty())
                        trendingTvShows = list
                }
            }
        }
    }

    companion object {
        const val BUNDLE_KEY = "com.dimi.moviedatabase.presentation.main.home.state.HomeViewState"
    }
}