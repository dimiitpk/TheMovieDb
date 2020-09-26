package com.dimi.moviedatabase.framework.cache.database

import androidx.room.*
import com.dimi.moviedatabase.framework.cache.model.*
import com.dimi.moviedatabase.framework.cache.model.junction.MovieActorsCrossRef
import com.dimi.moviedatabase.framework.cache.model.junction.TvShowActorsCrossRef
import com.dimi.moviedatabase.framework.cache.model.relations.PersonMovieCast
import com.dimi.moviedatabase.framework.cache.model.relations.PersonTvShowCast
import com.dimi.moviedatabase.framework.network.NetworkConstants


@Dao
interface PeopleDao : MediaDao<PersonCacheEntity> {

    /*
    *  if conflict appears in insert query, meaning that movie already exists in base
    *  and bcs onConflictStrategy is IGNORE it will return -1L
    *  afterwards we update movie and just save changed fields and save foreign keys
    *  bcs onCascade = DELETE will delete all foreign keys on REPLACE
    * */
    @Transaction
    suspend fun upsert(person: PersonCacheEntity): Long {
        insert(person).let { returnValue ->
            if (returnValue == -1L)
                update(person)

            return returnValue
        }
    }

    @Query(
        """
        SELECT * FROM person 
        WHERE name LIKE '%' || :query || '%' 
        ORDER BY popularity DESC
        LIMIT (:page * :pageSize)
        """
    )
    suspend fun getPopularPeoples(
        query: String,
        page: Int,
        pageSize: Int = NetworkConstants.PAGE_SIZE
    ): List<PersonCacheEntity>

    @Query(
        """
        SELECT * FROM person
        WHERE id = :personId
    """
    )
    suspend fun getPerson(
        personId: Int
    ): PersonCacheEntity

    @Transaction
    @Query("SELECT * FROM person WHERE id = :personId")
    suspend fun getMovieCast(personId: Long): PersonMovieCast

    @Transaction
    @Query("SELECT * FROM person WHERE id = :personId")
    suspend fun getTvShowCast(personId: Long): PersonTvShowCast

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(join: MovieActorsCrossRef): Long

    @Update
    suspend fun update( join : MovieActorsCrossRef )

    @Transaction
    suspend fun upsert(join: MovieActorsCrossRef): Long {
        insert(join).let { returnValue ->
            if (returnValue == -1L)
                update(join)

            return returnValue
        }
    }

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(join: TvShowActorsCrossRef): Long

    @Update
    suspend fun update( join : TvShowActorsCrossRef )

    @Transaction
    suspend fun upsert(join: TvShowActorsCrossRef): Long {
        insert(join).let { returnValue ->
            if (returnValue == -1L)
                update(join)

            return returnValue
        }
    }
}












