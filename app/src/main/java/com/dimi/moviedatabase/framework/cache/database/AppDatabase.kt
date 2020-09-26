package com.dimi.moviedatabase.framework.cache.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dimi.moviedatabase.framework.cache.model.*
import com.dimi.moviedatabase.framework.cache.model.junction.*

@Database(
    entities = [
        MovieCacheEntity::class,
        TvShowCacheEntity::class,
        SeasonCacheEntity::class,
        EpisodeCacheEntity::class,
        PersonCacheEntity::class,
        MovieActorsCrossRef::class,
        TvShowActorsCrossRef::class,
        NetworkCacheEntity::class,
        TvShowNetworkCrossRef::class,
        MediaListEntity::class,
        MovieListCrossRef::class,
        TvShowListCrossRef::class
    ],
    version = 44
)
@TypeConverters(MyTypeConverters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun movieDao(): MovieDao

    abstract fun tvShowDao(): TvShowDao

    abstract fun peopleDao(): PeopleDao

    companion object {
        const val DATABASE_NAME: String = "app_db"
    }
}