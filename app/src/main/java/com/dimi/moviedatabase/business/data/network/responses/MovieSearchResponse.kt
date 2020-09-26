package com.dimi.moviedatabase.business.data.network.responses

import com.dimi.moviedatabase.business.domain.model.Movie

data class MovieSearchResponse(

    var page: Int,
    var totalResults: Int,
    var totalPages: Int,
    var results: List<Movie>
)