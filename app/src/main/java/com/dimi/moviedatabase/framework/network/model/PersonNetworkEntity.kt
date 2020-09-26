package com.dimi.moviedatabase.framework.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PersonNetworkEntity(

    @SerializedName("id")
    @Expose
    var id: Int,

    @SerializedName("gender")
    @Expose
    var gender: Int? = null,

    @SerializedName("known_for_department")
    @Expose
    var department: String? = null,

    @SerializedName("place_of_birth")
    @Expose
    var placeOfBirth: String? = null,

    @SerializedName("deathday")
    @Expose
    var deathDay: String? = null,

    @SerializedName("also_known_as")
    @Expose
    var alsoKnownAs: List<String>? = null,

    @SerializedName("biography")
    @Expose
    var biography: String? = null,

    @SerializedName("birthday")
    @Expose
    var birthday: String? = null,

    @SerializedName("name")
    @Expose
    var name: String,

    @SerializedName("popularity")
    @Expose
    var popularity: Double,

    @SerializedName("profile_path")
    @Expose
    var profilePath: String? = null
)