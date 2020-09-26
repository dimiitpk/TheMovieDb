package com.dimi.moviedatabase.framework.cache.database

import androidx.room.*
import com.dimi.moviedatabase.framework.cache.model.MovieCacheEntity
import com.dimi.moviedatabase.framework.cache.model.junction.MovieListCrossRef
import com.dimi.moviedatabase.framework.cache.model.relations.MovieCast
import com.dimi.moviedatabase.framework.cache.model.relations.MoviesByList
import com.dimi.moviedatabase.framework.network.NetworkConstants

@Dao
interface MovieDao : MediaDao<MovieCacheEntity> {

    /*
    *  if conflict appears in insert query, meaning that movie already exists in base
    *  and bcs onConflictStrategy is IGNORE it will return -1L
    *  afterwards we update movie and just save changed fields and save foreign keys
    *  bcs onCascade = DELETE will delete all foreign keys on REPLACE
    * */
    @Transaction
    suspend fun upsert(
        movie: MovieCacheEntity
    ): Long {
        insert(movie).let { returnValue ->
            if (returnValue == -1L) {
                update(movie)
            }
            return returnValue
        }
    }

    @Query(
        """
        SELECT * FROM movie 
        WHERE title LIKE '%' || :query || '%'
        ORDER BY popularity DESC
        LIMIT (:page * :pageSize)
        """
    )
    suspend fun getPopularMovies(
        query: String,
        page: Int,
        pageSize: Int = NetworkConstants.PAGE_SIZE
    ): List<MovieCacheEntity>

    @Query(
        """
        SELECT * FROM movie
        WHERE id = :movieId
    """
    )
    suspend fun getMovie(
        movieId: Int
    ): MovieCacheEntity

    @Query(
        """
        SELECT * FROM movie 
        WHERE genre_ids LIKE '%' || :genre || '%' 
        """
    )
    suspend fun getMoviesByGenre(
        genre: String
    ): List<MovieCacheEntity>

    @Transaction
    @Query("SELECT * FROM movie WHERE id = :movieId")
    suspend fun getMovieWithCast(movieId: Long): MovieCast


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(join: MovieListCrossRef): Long

    @Transaction
    @Query("SELECT * FROM media_list WHERE id = :mediaListId")
    suspend fun getMoviesByList(mediaListId: Int): MoviesByList
}












