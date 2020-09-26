package com.dimi.moviedatabase.framework.network.mappers

import com.dimi.moviedatabase.framework.network.mappers.movie.MovieDetailsNetworkMapper
import com.dimi.moviedatabase.framework.network.mappers.movie.MovieListNetworkMapper
import com.dimi.moviedatabase.framework.network.mappers.people.PeopleListNetworkMapper
import com.dimi.moviedatabase.framework.network.mappers.people.PersonMovieCastNetworkMapper
import com.dimi.moviedatabase.framework.network.mappers.people.PersonTvShowCastNetworkMapper
import com.dimi.moviedatabase.framework.network.mappers.tv_show.TvShowDetailsNetworkMapper
import com.dimi.moviedatabase.framework.network.mappers.tv_show.TvShowListNetworkMapper
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkMapper
@Inject
constructor(
    val movieListNetworkMapper: MovieListNetworkMapper,
    val movieDetailsNetworkMapper: MovieDetailsNetworkMapper,
    val tvShowListNetworkMapper: TvShowListNetworkMapper,
    val tvShowDetailsNetworkMapper: TvShowDetailsNetworkMapper,
    val imageMapper: ImageMapper,
    val peopleListNetworkMapper: PeopleListNetworkMapper,
    val personMovieCastNetworkMapper: PersonMovieCastNetworkMapper,
    val personTvShowCastNetworkMapper: PersonTvShowCastNetworkMapper
)