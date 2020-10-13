package com.dimi.moviedatabase.di

import android.app.Application
import android.content.ContentValues
import android.content.Context
import androidx.room.OnConflictStrategy
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.dimi.moviedatabase.framework.cache.database.AppDatabase
import com.dimi.moviedatabase.framework.data.MovieFactory
import com.dimi.moviedatabase.presentation.main.search.enums.MediaListType
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.*
import java.util.concurrent.Executors
import javax.inject.Named

@ExperimentalCoroutinesApi
@Module
@InstallIn(ApplicationComponent::class)
object TestAppModule {

    @Provides
    fun provideMovieFactory(
        application: Application
    ): MovieFactory {
        return MovieFactory(application)
    }

    @Provides
    @Named("test_db")
    fun provideInMemoryDb(
        @ApplicationContext context: Context
    ) = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
        .setTransactionExecutor(Executors.newSingleThreadExecutor())
        .addCallback(
            object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    CoroutineScope(Dispatchers.IO).launch {
                        for (item in MediaListType.values()) {
                            val contentValues = ContentValues()
                            contentValues.put("id", item.code)
                            contentValues.put("name", item.name)
                            db.insert("media_list", OnConflictStrategy.IGNORE, contentValues)
                        }
                    }
                }
            })
        .allowMainThreadQueries()
        .build()

}