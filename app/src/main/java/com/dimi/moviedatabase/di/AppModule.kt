package com.dimi.moviedatabase.di

import android.content.Context
import com.dimi.moviedatabase.business.data.cache.abstraction.MovieCacheDataSource
import com.dimi.moviedatabase.business.data.cache.abstraction.PeopleCacheDataSource
import com.dimi.moviedatabase.business.data.cache.abstraction.TvShowCacheDataSource
import com.dimi.moviedatabase.business.data.cache.implementation.MovieCacheDataSourceImpl
import com.dimi.moviedatabase.business.data.cache.implementation.PeopleCacheDataSourceImpl
import com.dimi.moviedatabase.business.data.cache.implementation.TvShowCacheDataSourceImpl
import com.dimi.moviedatabase.business.data.network.abstraction.MovieNetworkDataSource
import com.dimi.moviedatabase.business.data.network.abstraction.PeopleNetworkDataSource
import com.dimi.moviedatabase.business.data.network.abstraction.TvShowNetworkDataSource
import com.dimi.moviedatabase.business.data.network.implemention.MovieNetworkDataSourceImpl
import com.dimi.moviedatabase.business.data.network.implemention.PeopleNetworkDataSourceImpl
import com.dimi.moviedatabase.business.data.network.implemention.TvShowNetworkDataSourceImpl
import com.dimi.moviedatabase.business.interactors.movie.*
import com.dimi.moviedatabase.business.interactors.people.*
import com.dimi.moviedatabase.business.interactors.tv_show.*
import com.dimi.moviedatabase.di.qualifiers.BusinessSource
import com.dimi.moviedatabase.di.qualifiers.FrameworkSource
import com.dimi.moviedatabase.framework.cache.database.MovieDao
import com.dimi.moviedatabase.framework.cache.database.PeopleDao
import com.dimi.moviedatabase.framework.cache.database.TvShowDao
import com.dimi.moviedatabase.framework.cache.implementation.MovieDaoServiceImpl
import com.dimi.moviedatabase.framework.cache.implementation.PeopleDaoServiceImpl
import com.dimi.moviedatabase.framework.cache.implementation.TvShowDaoServiceImpl
import com.dimi.moviedatabase.framework.cache.mappers.CacheMapper
import com.dimi.moviedatabase.framework.cache.mappers.MovieCacheMapper
import com.dimi.moviedatabase.framework.cache.mappers.PeopleCacheMapper
import com.dimi.moviedatabase.framework.cache.mappers.TvShowCacheMapper
import com.dimi.moviedatabase.framework.network.api.MovieApi
import com.dimi.moviedatabase.framework.network.api.PeopleApi
import com.dimi.moviedatabase.framework.network.api.TvShowApi
import com.dimi.moviedatabase.framework.network.implementation.MovieApiServiceImpl
import com.dimi.moviedatabase.framework.network.implementation.PeopleApiServiceImpl
import com.dimi.moviedatabase.framework.network.implementation.TvShowApiServiceImpl
import com.dimi.moviedatabase.framework.network.mappers.NetworkMapper
import com.dimi.moviedatabase.presentation.main.search.UserPreferencesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.FlowPreview
import javax.inject.Singleton

@FlowPreview
@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @FrameworkSource
    @Provides
    fun provideMovieApiService(
        movieApi: MovieApi,
        networkMapper: NetworkMapper
    ): MovieNetworkDataSource {
        return MovieApiServiceImpl(movieApi, networkMapper)
    }

    @Singleton
    @FrameworkSource
    @Provides
    fun provideTvShowApiService(
        tvShowApi: TvShowApi,
        networkMapper: NetworkMapper
    ): TvShowNetworkDataSource {
        return TvShowApiServiceImpl(tvShowApi, networkMapper)
    }

    @Singleton
    @FrameworkSource
    @Provides
    fun providePeopleApiService(
        peopleApi: PeopleApi,
        networkMapper: NetworkMapper
    ): PeopleNetworkDataSource {
        return PeopleApiServiceImpl(peopleApi, networkMapper)
    }

    @Singleton
    @Provides
    fun provideUseCases(
        @BusinessSource networkDataSource: MovieNetworkDataSource,
        @BusinessSource cacheDataSource: MovieCacheDataSource
    ): MovieUseCases {
        return MovieUseCases(
            MovieTrailersUseCase(networkDataSource),
            SearchMoviesUseCase(networkDataSource, cacheDataSource),
            MovieDetailsUseCase(networkDataSource, cacheDataSource),
            MovieImagesUseCase(networkDataSource),
            SimilarMoviesUseCase(networkDataSource, cacheDataSource),
            MovieRecommendationsUseCase(networkDataSource, cacheDataSource)
        )
    }


    @Singleton
    @Provides
    fun providePeopleUseCases(
        @BusinessSource networkDataSource: PeopleNetworkDataSource,
        @BusinessSource cacheDataSource: PeopleCacheDataSource
    ): PeopleUseCases {
        return PeopleUseCases(
            PeopleDetailsUseCase(networkDataSource, cacheDataSource),
            SearchPeopleUseCase(networkDataSource, cacheDataSource),
            PersonMovieCastUseCase(networkDataSource, cacheDataSource),
            PersonTvShowCastUseCase(networkDataSource, cacheDataSource),
            PersonImagesUseCase(networkDataSource)
        )
    }

    @Singleton
    @Provides
    fun provideTvShowUseCases(
        @BusinessSource networkDataSource: TvShowNetworkDataSource,
        @BusinessSource cacheDataSource: TvShowCacheDataSource
    ): TvShowUseCases {
        return TvShowUseCases(
            TvShowTrailersUseCase(networkDataSource),
            SearchTvShowsUseCase(networkDataSource, cacheDataSource),
            TvShowDetailsUseCase(networkDataSource, cacheDataSource),
            TvShowImagesUseCase(networkDataSource),
            TvShowRecommendationsUseCase(networkDataSource, cacheDataSource),
            SimilarTvShowsUseCase(networkDataSource, cacheDataSource),
            TvShowEpisodesUseCase(networkDataSource, cacheDataSource)
        )
    }

    @Singleton
    @Provides
    fun provideUserPreferencesRepository(
        @ApplicationContext context: Context
    ): UserPreferencesRepository {
        return UserPreferencesRepository(context)
    }

    @Singleton
    @BusinessSource
    @Provides
    fun provideMovieNetworkDataSource(
        @FrameworkSource movieApiService: MovieNetworkDataSource
    ): MovieNetworkDataSource {
        return MovieNetworkDataSourceImpl(movieApiService)
    }

    @Singleton
    @BusinessSource
    @Provides
    fun provideTvShowNetworkDataSource(
        @FrameworkSource tvShowApiService: TvShowNetworkDataSource
    ): TvShowNetworkDataSource {
        return TvShowNetworkDataSourceImpl(tvShowApiService)
    }

    @Singleton
    @BusinessSource
    @Provides
    fun providePeopleNetworkDataSource(
        @FrameworkSource peopleApiService: PeopleNetworkDataSource
    ): PeopleNetworkDataSource {
        return PeopleNetworkDataSourceImpl(peopleApiService)
    }

    @Singleton
    @FrameworkSource
    @Provides
    fun provideMovieDaoService(
        movieDao: MovieDao,
        peopleDao: PeopleDao,
        cacheMapper: CacheMapper
    ): MovieCacheDataSource {
        return MovieDaoServiceImpl(movieDao, peopleDao, cacheMapper)
    }

    @Singleton
    @FrameworkSource
    @Provides
    fun providePeopleDaoService(
        peopleDao: PeopleDao,
        movieDao: MovieDao,
        tvShowDao: TvShowDao,
        cacheMapper: CacheMapper
    ): PeopleCacheDataSource {
        return PeopleDaoServiceImpl(peopleDao, movieDao, tvShowDao, cacheMapper)
    }

    @Singleton
    @BusinessSource
    @Provides
    fun provideMovieCacheDataSource(
        @FrameworkSource movieDaoService: MovieCacheDataSource
    ): MovieCacheDataSource {
        return MovieCacheDataSourceImpl(movieDaoService)
    }

    @Singleton
    @BusinessSource
    @Provides
    fun providePeopleCacheDataSource(
        @FrameworkSource peopleDaoService: PeopleCacheDataSource
    ): PeopleCacheDataSource {
        return PeopleCacheDataSourceImpl(peopleDaoService)
    }

    @Singleton
    @FrameworkSource
    @Provides
    fun provideTvShowDaoService(
        tvShowDao: TvShowDao,
        peopleDao: PeopleDao,
        cacheMapper: CacheMapper
    ): TvShowCacheDataSource {
        return TvShowDaoServiceImpl(tvShowDao, peopleDao, cacheMapper)
    }

    @Singleton
    @BusinessSource
    @Provides
    fun provideTvShowCacheDataSource(
        @FrameworkSource tvShowDaoService: TvShowCacheDataSource
    ): TvShowCacheDataSource {
        return TvShowCacheDataSourceImpl(tvShowDaoService)
    }
}