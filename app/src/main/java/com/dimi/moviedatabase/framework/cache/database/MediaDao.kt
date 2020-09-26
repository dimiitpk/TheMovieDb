package com.dimi.moviedatabase.framework.cache.database

import androidx.room.*

interface MediaDao<T> {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert( item: T): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(vararg item: T): List<Long>

    @Update
    suspend fun update( item : T )
}