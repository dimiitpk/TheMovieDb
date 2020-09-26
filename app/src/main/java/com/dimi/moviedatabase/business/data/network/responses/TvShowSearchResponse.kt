package com.dimi.moviedatabase.business.data.network.responses

import com.dimi.moviedatabase.business.domain.model.TvShow

data class TvShowSearchResponse(

    var page: Int,
    var totalResults: Int,
    var totalPages: Int,
    var results: List<TvShow>
)