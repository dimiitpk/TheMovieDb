package com.dimi.moviedatabase.business.data.network.responses

import com.dimi.moviedatabase.business.domain.model.Person

data class PeopleSearchResponse(

    var page: Int,
    var totalResults: Int,
    var totalPages: Int,
    var results: List<Person>
)