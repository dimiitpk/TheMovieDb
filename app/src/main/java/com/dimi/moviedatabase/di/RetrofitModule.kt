package com.dimi.moviedatabase.di

import com.dimi.moviedatabase.framework.network.api.MovieApi
import com.dimi.moviedatabase.framework.network.NetworkConstants
import com.dimi.moviedatabase.framework.network.api.PeopleApi
import com.dimi.moviedatabase.framework.network.api.TvShowApi
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object RetrofitModule {

    @Singleton
    @Provides
    fun provideGsonBuilder(): Gson {
        return GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .create()
    }

    @Singleton
    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(gson: Gson, okHttpClient: OkHttpClient): Retrofit.Builder {
        return Retrofit.Builder()
            .baseUrl(NetworkConstants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
    }

    @Singleton
    @Provides
    fun provideMovieApiService(
        retrofit: Retrofit.Builder
    ): MovieApi {
        return retrofit
            .build()
            .create(MovieApi::class.java)
    }

    @Singleton
    @Provides
    fun provideTvShowApiService(
        retrofit: Retrofit.Builder
    ): TvShowApi {
        return retrofit
            .build()
            .create(TvShowApi::class.java)
    }

    @Singleton
    @Provides
    fun providePeopleApiService(
        retrofit: Retrofit.Builder
    ): PeopleApi {
        return retrofit
            .build()
            .create(PeopleApi::class.java)
    }
}