package com.dimi.moviedatabase.framework.network.responses.common

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CreditsResponse<CastResponse> (

    @SerializedName("cast")
    @Expose
    var cast: List<CastResponse>
)