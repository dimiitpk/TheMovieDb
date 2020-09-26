package com.dimi.moviedatabase.framework.network

import com.dimi.moviedatabase.util.Keys

object NetworkConstants {

    const val BASE_URL = "https://api.themoviedb.org/3/"

    const val POPULAR_MOVIES_ENDPOINT = "movie/popular"
    const val TOP_RATED_MOVIES_ENDPOINT = "movie/top_rated"
    const val UPCOMING_MOVIES_ENDPOINT = "movie/upcoming"
    const val TRENDING_MOVIES_ENDPOINT = "trending/movie/week"

    const val POPULAR_TV_SHOW_ENDPOINT = "tv/popular"
    const val TOP_RATED_TV_SHOW_ENDPOINT = "tv/top_rated"
    const val ON_THE_AIR_TV_SHOW_ENDPOINT = "tv/on_the_air"
    const val TRENDING_TV_SHOW_ENDPOINT = "trending/tv/week"

    const val MOVIES_SEARCH_ENDPOINT = "search/movie"
    const val MOVIES_DISCOVER_ENDPOINT = "discover/movie"

    const val TV_SHOW_SEARCH_ENDPOINT = "search/tv"
    const val TV_SHOW_GENRE_ENDPOINT = "discover/tv"

    const val POPULAR_PEOPLE_ENDPOINT = "person/popular"
    const val PEOPLE_SEARCH_ENDPOINT = "search/person"


    private const val IMAGE_URL_PREFIX = "https://image.tmdb.org/t/p/"
    const val VERY_SMALL_IMAGE_URL_PREFIX = IMAGE_URL_PREFIX + "w92"
    const val SMALL_IMAGE_URL_PREFIX = IMAGE_URL_PREFIX + "w300"
    const val BIG_IMAGE_URL_PREFIX = IMAGE_URL_PREFIX + "w780"
    const val ORIGINAL_IMAGE_URL_PREFIX = IMAGE_URL_PREFIX + "original"

    const val API_KEY_REQUEST_PARAM = "api_key"
    const val LANGUAGE_REQUEST_PARAM = "language"
    const val PAGE_REQUEST_PARAM = "page"
    const val SORT_BY_REQUEST_PARAM = "sort_by"
    const val ADULT_REQUEST_PARAM = "include_adult"
    const val FIRST_AIR_DATES_REQUEST_PARAM = "include_null_first_air_dates"
    const val VIDEO_REQUEST_PARAM = "include_video"
    const val QUERY_REQUEST_PARAM = "query"
    const val TIMEZONE_REQUEST_PARAM = "timezone"
    const val GENRE_REQUEST_PARAM = "with_genres"
    const val NETWORK_REQUEST_PARAM = "with_networks"
    const val APPEND_TO_RESPONSE_PARAM = "append_to_response"
    const val INCLUDE_IMAGE_LANG_PARAM = "include_image_language"

    const val PAGE_SIZE = 20

    const val TIMEZONE_DEFAULT = "America%2FNew_York"
    const val LANGUAGE_DEFAULT = "en-US"
    const val ADULT_DEFAULT = false
    const val VIDEO_DEFAULT = false
    const val FIRST_AIR_DEFAULT = false
    const val SORT_BY_DEFAULT = "popularity.desc"
    const val VIDEOS_AND_CREDITS_APPEND = "videos,credits"
    const val INCLUDE_IMAGE_DEFAULT = "en,null"//%2C


    const val MOVIE_DISCOVER_SORT_BY_POPULARITY = "popularity"
    const val MOVIE_DISCOVER_SORT_BY_ORIGINAL_TITLE = "original_title"
    const val MOVIE_DISCOVER_SORT_BY_VOTE_AVERAGE = "vote_average"
    const val MOVIE_DISCOVER_SORT_BY_VOTE_COUNT = "vote_count"

    const val MOVIE_DISCOVER_SORT_ASC = "asc"
    const val MOVIE_DISCOVER_SORT_DESC = "desc"
}