package com.dimi.moviedatabase.framework.cache.model

import androidx.room.*

@Entity(
    tableName = "network"
)
data class NetworkCacheEntity(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    var id: Long,

    @ColumnInfo(name = "name")
    var name: String
) {
    override fun toString(): String {
        return "NetworkCacheEntity(id=$id, name='$name')"
    }
}