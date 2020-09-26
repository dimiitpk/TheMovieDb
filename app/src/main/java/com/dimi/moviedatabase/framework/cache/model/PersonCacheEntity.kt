package com.dimi.moviedatabase.framework.cache.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "person")
data class PersonCacheEntity(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    var id: Long,

    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name = "gender")
    var gender: Int? = null,

    @ColumnInfo(name = "department")
    var department: String? = null,

    @ColumnInfo(name = "place_of_birth")
    var placeOfBirth: String? = null,

    @ColumnInfo(name = "popularity")
    var popularity: Double? = null,

    @ColumnInfo(name = "birthday")
    var birthday: Date? = null,

    @ColumnInfo(name = "deathday")
    var deathDay: Date? = null,

    @ColumnInfo(name = "biography")
    var biography: String? = null,

    @ColumnInfo(name = "also_known_as")
    var alsoKnownAs: String? = null,

    @ColumnInfo(name = "profile_path")
    var profilePath: String? = null
)