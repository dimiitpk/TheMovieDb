package com.dimi.moviedatabase.di

import android.content.ContentValues
import android.content.Context
import androidx.room.OnConflictStrategy
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.dimi.moviedatabase.framework.cache.database.MovieDao
import com.dimi.moviedatabase.framework.cache.database.AppDatabase
import com.dimi.moviedatabase.framework.cache.database.PeopleDao
import com.dimi.moviedatabase.framework.cache.database.TvShowDao
import com.dimi.moviedatabase.presentation.main.search.enums.MediaListType
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object RoomModule {

    @Singleton
    @Provides
    fun providePostDb(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .addCallback(object : RoomDatabase.Callback() {
                override fun onOpen(db: SupportSQLiteDatabase) {
                    super.onOpen(db)
                    CoroutineScope(IO).launch {
                        for( item in MediaListType.values()) {
                            val contentValues = ContentValues()
                            contentValues.put("id", item.code)
                            contentValues.put("name", item.name)
                            db.insert("media_list", OnConflictStrategy.IGNORE, contentValues)
                        }
                    }
                }
            })
            .build()
    }

    @Singleton
    @Provides
    fun provideMovieDao(appDatabase: AppDatabase): MovieDao {
        return appDatabase.movieDao()
    }

    @Singleton
    @Provides
    fun provideTvShowDao(appDatabase: AppDatabase): TvShowDao {
        return appDatabase.tvShowDao()
    }

    @Singleton
    @Provides
    fun providePeopleDao(appDatabase: AppDatabase): PeopleDao {
        return appDatabase.peopleDao()
    }
}