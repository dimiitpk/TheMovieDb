package com.dimi.moviedatabase.framework.cache.database

import androidx.room.*
import com.dimi.moviedatabase.framework.cache.model.*
import com.dimi.moviedatabase.framework.cache.model.junction.MovieActorsCrossRef
import com.dimi.moviedatabase.framework.cache.model.junction.MovieListCrossRef
import com.dimi.moviedatabase.framework.cache.model.junction.TvShowListCrossRef
import com.dimi.moviedatabase.framework.cache.model.junction.TvShowNetworkCrossRef
import com.dimi.moviedatabase.framework.cache.model.relations.*
import com.dimi.moviedatabase.framework.network.NetworkConstants

@Dao
interface TvShowDao : MediaDao<TvShowCacheEntity> {

    @Transaction
    suspend fun upsert(
        tvShow: TvShowCacheEntity
    ): Long {
        insert(tvShow).let { returnValue ->
            if (returnValue == -1L)
                update(tvShow)

            return returnValue
        }
    }

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(season: SeasonCacheEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(network: NetworkCacheEntity): Long

    @Update
    suspend fun update(season: SeasonCacheEntity)

    @Transaction
    suspend fun upsert(season: SeasonCacheEntity): Long {
        insert(season).let { returnValue ->
            if (returnValue == -1L)
                update(season)

            return returnValue
        }
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(episode: EpisodeCacheEntity): Long

    @Query(
        """
        SELECT * FROM tv_show 
        WHERE title LIKE '%' || :query || '%'
        ORDER BY popularity DESC
        LIMIT (:page * :pageSize)
        """
    )
    suspend fun getPopularTvShows(
        query: String,
        page: Int,
        pageSize: Int = NetworkConstants.PAGE_SIZE
    ): List<TvShowCacheEntity>

    @Query(
        """
        SELECT * FROM tv_show
        WHERE id = :tvShowId
    """
    )
    suspend fun getTvShow(
        tvShowId: Long
    ): TvShowCacheEntity

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(join: TvShowNetworkCrossRef): Long

    @Transaction
    @Query("SELECT * FROM tv_show WHERE id = :tvShowId")
    suspend fun getTvShowNetworks(tvShowId: Long): TvShowNetworks

    @Transaction
    @Query("SELECT * FROM network WHERE id = :networkId")
    suspend fun getNetworkTvShows(
        networkId: Long
    ): NetworkTvShows

    @Query(
        """
        SELECT * FROM tv_show 
        WHERE genre_ids LIKE '%' || :genre || '%'
        """
    )
    suspend fun getTvShowsByGenre(
        genre: String
    ): List<TvShowCacheEntity>

    @Query(
        """
        SELECT * FROM season
        WHERE tvShowId = :tvShowId
        ORDER BY `season_number` DESC
    """
    )
    suspend fun getSeasons(
        tvShowId: Long
    ): List<SeasonCacheEntity>

    @Query(
        """
        SELECT * FROM episode
        WHERE season_id = :season 
        AND show_id = :tvShowId
        ORDER BY `episode_number` ASC
    """
    )
    suspend fun getEpisodesPerSeason(
        tvShowId: Long,
        season: Long
    ): List<EpisodeCacheEntity>

    @Transaction
    @Query("SELECT * FROM tv_show WHERE id = :tvShowId")
    suspend fun getTvShowWithCast(tvShowId: Long): TvShowCast

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(join: TvShowListCrossRef): Long

    @Transaction
    @Query("SELECT * FROM media_list WHERE id = :mediaListId")
    suspend fun getTvShowsByList(mediaListId: Int): TvShowsByList
}












