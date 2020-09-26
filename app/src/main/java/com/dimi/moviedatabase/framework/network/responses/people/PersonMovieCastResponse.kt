package com.dimi.moviedatabase.framework.network.responses.people

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PersonMovieCastResponse(

    @SerializedName("id")
    @Expose
    var id: Int,

    @SerializedName("cast")
    @Expose
    var results: List<PersonMovieCast>
)