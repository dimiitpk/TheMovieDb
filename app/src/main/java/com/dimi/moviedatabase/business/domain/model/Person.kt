package com.dimi.moviedatabase.business.domain.model

import android.os.Parcelable
import com.dimi.moviedatabase.business.domain.state.MediaType
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Person(
    override var id: Long,
    var name: String,
    var gender: Int? = null,
    var department: String? = null,
    var placeOfBirth: String? = null,
    var popular: Double? = null,
    var birthday: Date? = null,
    var deathDay: Date? = null,
    var biography: String? = null,
    var alsoKnownAs: List<String>? = null,
    var profilePath: String? = null,
    override var character: String? = null,
    var priority: Int? = null
) : Parcelable,
    Media(
        id,
        name,
        popular ?: 0.0,
        0,
        0f,
        biography ?: "",
        profilePath,
        "",
        null,
        null,
        null,
        null,
        null,
        null,
        character,
        birthday,
        MediaType.PERSON
    ) {


    override fun toString(): String {
        return "Person(id=$id, name='$name', gender=$gender, department=$department, placeOfBirth=$placeOfBirth, popular=$popular, birthday=$birthday, deathDay=$deathDay, biography=$biography, alsoKnownAs=$alsoKnownAs, profilePath=$profilePath, character=$character)"
    }
}